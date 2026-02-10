package ec.edu.ups.icc.portafolio_backend.user.repository;

import ec.edu.ups.icc.portafolio_backend.user.entity.User;
import ec.edu.ups.icc.portafolio_backend.programmer.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    long countByRole(Role role);
}