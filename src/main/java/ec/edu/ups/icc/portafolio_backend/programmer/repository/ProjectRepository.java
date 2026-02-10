package ec.edu.ups.icc.portafolio_backend.programmer.repository;

import ec.edu.ups.icc.portafolio_backend.programmer.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByProfileId(Long profileId);
    Page<Project> findByProfileId(Long profileId, Pageable pageable);
}