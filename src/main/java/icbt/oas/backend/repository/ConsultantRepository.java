package icbt.oas.backend.repository;


import icbt.oas.backend.model.Consultant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConsultantRepository extends JpaRepository<Consultant,Long> {
    Boolean existsByEmail(String email);
    Optional<Consultant> findByEmail(String email);
    Optional<Consultant> findById(Long id);
}
