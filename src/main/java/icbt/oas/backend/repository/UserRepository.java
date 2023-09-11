package icbt.oas.backend.repository;

import icbt.oas.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);
    User save(User user);

    @Query(value = "SELECT u.* " +
            "FROM users u " +
            "WHERE u.company_id = ?1 " +
            "ORDER BY u.is_active DESC, FIELD(u.role, 'SUPER_ADMIN', 'ADMIN', 'USER'), u.first_name",
            nativeQuery = true)
    Optional<List<User>> findUsersByCompanyId(String companyId);

    @Query(value = "SELECT u.* " +
            "FROM users u " +
            "WHERE u.company_id = ?1 AND u.role = 'SUPER_ADMIN'",
            nativeQuery = true)
    Optional<User> findSuperAdminByCompanyId(String companyId);

    @Query(value = "SELECT u.* FROM users u " +
            "INNER JOIN companies c ON c.company_id = u.company_id AND c.is_active = true AND c.is_suspended = false " +
            "AND u.email = ?1 AND u.is_active = true",
            nativeQuery = true)
    Optional<User> isUserAllowed(String email);

    @Query(value = "SELECT u.* " +
            "FROM users u " +
            "WHERE u.id = ?1 AND u.company_id = ?2",
            nativeQuery = true)
    Optional<User> findById(long userId, String companyId);
}
