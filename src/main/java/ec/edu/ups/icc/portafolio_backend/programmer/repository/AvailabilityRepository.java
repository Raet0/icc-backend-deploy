package ec.edu.ups.icc.portafolio_backend.programmer.repository;

import ec.edu.ups.icc.portafolio_backend.programmer.entity.AvailabilitySlot;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AvailabilityRepository extends JpaRepository<AvailabilitySlot, Long> {
    List<AvailabilitySlot> findByProfileId(Long profileId);
    Page<AvailabilitySlot> findByProfileId(Long profileId, Pageable pageable);
}