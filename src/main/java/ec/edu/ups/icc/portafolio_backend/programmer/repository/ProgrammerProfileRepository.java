package ec.edu.ups.icc.portafolio_backend.programmer.repository;

import ec.edu.ups.icc.portafolio_backend.programmer.entity.ProgrammerProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProgrammerProfileRepository extends JpaRepository<ProgrammerProfile, Long> {
    Optional<ProgrammerProfile> findByUserId(Long userId);
}