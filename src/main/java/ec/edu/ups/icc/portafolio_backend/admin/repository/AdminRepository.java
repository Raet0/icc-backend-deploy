package ec.edu.ups.icc.portafolio_backend.admin.repository;

import ec.edu.ups.icc.portafolio_backend.admin.entity.AdminAudit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<AdminAudit, Long> {}