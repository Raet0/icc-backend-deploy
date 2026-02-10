package ec.edu.ups.icc.portafolio_backend.user.controller;

import ec.edu.ups.icc.portafolio_backend.user.dto.AuthResponse;
import ec.edu.ups.icc.portafolio_backend.user.dto.LoginRequest;
import ec.edu.ups.icc.portafolio_backend.user.dto.RegisterRequest;
import ec.edu.ups.icc.portafolio_backend.user.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import ec.edu.ups.icc.portafolio_backend.user.dto.GoogleLoginRequest;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // @PostMapping("/register")
    // public AuthResponse register(@RequestBody @Valid RegisterRequest request) {
    //     // Este método debe guardar al usuario con un ROL básico (USER)
    //     return authService.register(request);
    // }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody @Valid LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/register-admin")
    public AuthResponse registerAdmin(@RequestBody @Valid RegisterRequest request) {
        return authService.registerFirstAdmin(request);
    }

    @PostMapping("/google")
    public AuthResponse google(@RequestBody GoogleLoginRequest request) {
        return authService.loginWithGoogle(request.idToken());
    }
}