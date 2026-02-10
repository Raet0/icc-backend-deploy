package ec.edu.ups.icc.portafolio_backend.admin.service;

import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import ec.edu.ups.icc.portafolio_backend.admin.dto.CreateUserRequest;
import ec.edu.ups.icc.portafolio_backend.admin.dto.UpdateUserRequest;
import ec.edu.ups.icc.portafolio_backend.admin.dto.UserResponse;
import ec.edu.ups.icc.portafolio_backend.admin.entity.AdminAudit;
import ec.edu.ups.icc.portafolio_backend.admin.repository.AdminRepository;
import ec.edu.ups.icc.portafolio_backend.programmer.entity.Role;
import ec.edu.ups.icc.portafolio_backend.user.entity.User;
import ec.edu.ups.icc.portafolio_backend.user.repository.UserRepository;

@Service
public class AdminService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AdminRepository adminRepository;

    public AdminService(UserRepository userRepository, PasswordEncoder passwordEncoder,
            AdminRepository adminRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.adminRepository = adminRepository;
    }

    public UserResponse createUser(Long adminId, CreateUserRequest request) {
        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(Role.valueOf(request.role()));
        userRepository.save(user);

        AdminAudit audit = new AdminAudit();
        audit.setAdminId(adminId);
        audit.setAction("CREATE_USER:" + user.getEmail());
        adminRepository.save(audit);

        return new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getRole().name());
    }

    public List<UserResponse> listUsers(int page, int size) {
        return userRepository.findAll(PageRequest.of(page, size)).stream()
                .map(u -> new UserResponse(u.getId(), u.getName(), u.getEmail(), u.getRole().name()))
                .toList();
    }

    public UserResponse updateUser(Long userId, UpdateUserRequest request) {
        User user = userRepository.findById(userId).orElseThrow();
        if (request.name() != null)
            user.setName(request.name());
        if (request.email() != null)
            user.setEmail(request.email());
        if (request.role() != null)
            user.setRole(Role.valueOf(request.role()));
        userRepository.save(user);
        return new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getRole().name());
    }

    public void deleteUser(Long adminId, Long userId) {
        if (Objects.equals(adminId, userId)) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Un administrador no puede eliminarse a si mismo"
            );
        }
        userRepository.deleteById(userId);
    }
}
