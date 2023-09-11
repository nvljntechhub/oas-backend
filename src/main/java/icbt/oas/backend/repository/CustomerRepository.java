package icbt.oas.backend.repository;


import icbt.oas.backend.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByEmail(String email);
    Boolean existsByEmail(String email);
    Customer save(Customer customer);

    @Query(value = "SELECT c.* " +
            "FROM customers c " +
            "WHERE c.email = ?1 AND c.is_active = true",
    nativeQuery = true)
    Optional<Customer> findByEmailAndIsActive(String email);
}
