package icbt.oas.backend.repository;

import icbt.oas.backend.model.JobSeeker;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JobSeekerRepository extends JpaRepository<JobSeeker,Long> {
    Boolean existsByEmail(String email);

    Optional<JobSeeker> findByEmail(String email);
    Optional<JobSeeker> findById(Long id);
}
