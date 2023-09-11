package icbt.oas.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "customers")
public class Customer {
    public static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "created_company_id", length = 100, nullable = false)
    private String createdCompanyId;
    @Column(length = 100, nullable = false)
    private String firstName;
    @Column(length = 100, nullable = false)
    private String lastName;
    @Column(length = 100, nullable = false)
    private String email;
    @Column(length = 100)
    @ToString.Exclude private @JsonIgnore String password;
    @Column(length = 15, nullable = false)
    private String contactNumber;
    @Column(name = "postal_address",length = 100)
    private String postalAddress;
    @Column(name = "id_type",length = 50)
    private String idType;
    @Column(name = "id_number",length = 50)
    private String idNumber;
    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;
    @Column(nullable = false, columnDefinition = "tinyint(1) not null default 0")
    private boolean isActive;
    @CreationTimestamp
    @Column(updatable = false)
    protected LocalDateTime createdAt;
    @UpdateTimestamp
    protected LocalDateTime updatedAt;

    public Customer() {
    }

    public Customer(String createdCompanyId, String firstName, String lastName, String email,
                    String contactNumber, String postalAddress, String password, String idType, String idNumber,
                    LocalDateTime lastLoginAt, boolean isActive) {
        super();
        this.createdCompanyId = createdCompanyId;
        this.firstName = firstName.trim();
        this.lastName = lastName.trim();
        this.email = email.trim();
        this.password = password != null ? PASSWORD_ENCODER.encode(password) : null;
        this.contactNumber = contactNumber.trim();
        this.postalAddress = postalAddress;
        this.idType = idType;
        this.idNumber = idNumber.trim();
        this.lastLoginAt = lastLoginAt;
        this.isActive = isActive;
    }

    public Long getId() {
        return id;
    }

    public String getCreatedCompanyId() {
        return createdCompanyId;
    }

    public void setCreatedCompanyId(String createdCompanyId) {
        this.createdCompanyId = createdCompanyId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = PASSWORD_ENCODER.encode(password);
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getPostalAddress() {
        return postalAddress;
    }

    public void setPostalAddress(String postalAddress) {
        this.postalAddress = postalAddress;
    }

    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public LocalDateTime getLastLoginAt() {
        return lastLoginAt;
    }

    public void setLastLoginAt(LocalDateTime lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }

    @JsonProperty("isActive")
    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
