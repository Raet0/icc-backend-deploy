package ec.edu.ups.icc.portafolio_backend.user.service;

import ec.edu.ups.icc.portafolio_backend.user.dto.AuthResponse;
import ec.edu.ups.icc.portafolio_backend.user.dto.LoginRequest;
import ec.edu.ups.icc.portafolio_backend.user.dto.RegisterRequest;
import ec.edu.ups.icc.portafolio_backend.programmer.entity.Role;
import ec.edu.ups.icc.portafolio_backend.user.entity.User;
import ec.edu.ups.icc.portafolio_backend.user.repository.UserRepository;
import ec.edu.ups.icc.portafolio_backend.shared.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.http.javanet.NetHttpTransport;

import java.util.List;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Value("${google.oauth.clientId}")
    private String googleClientId;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService,
                       AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );
        var user = userRepository.findByEmail(request.email()).orElseThrow();
        String token = jwtService.generateToken(user.getEmail());
        return new AuthResponse(token, user.getName(), user.getRole().name());
    }

    public AuthResponse registerFirstAdmin(RegisterRequest request) {
        if (userRepository.countByRole(Role.ADMIN) > 0) {
            throw new RuntimeException("Ya existe un administrador.");
        }
        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(Role.ADMIN);
        userRepository.save(user);

        String token = jwtService.generateToken(user.getEmail());
        return new AuthResponse(token, user.getName(), user.getRole().name());
    }

    public AuthResponse loginWithGoogle(String idToken) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                new NetHttpTransport(),
                new GsonFactory()
            )
            .setAudience(List.of(googleClientId))
            .build();

            GoogleIdToken token = verifier.verify(idToken);
            if (token == null) throw new RuntimeException("Token inválido");

            String email = token.getPayload().getEmail();
            String name = (String) token.getPayload().get("name");

            var userOpt = userRepository.findByEmail(email);
            User user;
            if (userOpt.isEmpty()) {
                user = new User();
                user.setName(name);
                user.setEmail(email);
                user.setPassword(passwordEncoder.encode("GOOGLE_OAUTH"));
                user.setRole(Role.USER);
                userRepository.save(user);
            } else {
                user = userOpt.get();
            }

            String jwt = jwtService.generateToken(user.getEmail());
            return new AuthResponse(jwt, user.getName(), user.getRole().name());
        } catch (Exception e) {
            throw new RuntimeException("Google login failed");
        }
    }
}