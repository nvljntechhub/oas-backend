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
import java.time.LocalTime;

@Data
@Entity
@Table(uniqueConstraints = {
                @UniqueConstraint(columnNames = "email")
        })
public class Consultant {
    public static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(length = 100, nullable = false)
    private String firstName;
    @Column(length = 100, nullable = false)
    private String lastName;
    @Column(length = 100, nullable = false)
    private String email;
    @Column(length = 100, nullable = false)
    @ToString.Exclude private @JsonIgnore String password;
    @Column(length = 15, nullable = false)
    private String role;
    @Column(name = "contact_number", length = 15, nullable = false)
    private String contactNumber;
    @Column(name = "postal_address",length = 100, nullable = false)
    private String postalAddress;
    @Column(name = "morning_availability_start_time")
    private LocalTime morningAvailabilityStartTime;
    @Column(name = "morning_availability_end_time")
    private LocalTime morningAvailabilityEndTime;
    @Column(name = "evening_availability_start_time")
    private LocalTime eveningAvailabilityStartTime;
    @Column(name = "evening_availability_end_time")
    private LocalTime eveningAvailabilityEndTime;
    @Column()
    private int experience;
    @Column(name = "educational_qualification",length = 500)
    private String educationalQualification;
    @Column(name = "specialized_countries",length = 500)
    private String specializedCountries;
    @Column(nullable = false, columnDefinition = "tinyint(1) not null default 0")
    private boolean isActive;


    @CreationTimestamp
    @Column(updatable = false)
    protected LocalDateTime createdAt;
    @UpdateTimestamp
    protected LocalDateTime updatedAt;

    public Consultant() {
    }

    public Consultant( String firstName, String lastName, String email, String password, String role,
                       String contactNumber, String postalAddress, LocalTime morningAvailabilityStartTime,
                       LocalTime morningAvailabilityEndTime, LocalTime eveningAvailabilityStartTime,
                       LocalTime eveningAvailabilityEndTime, int experience, String educationalQualification,
                       String specializedCountries, boolean isActive) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = PASSWORD_ENCODER.encode(password);
        this.role = role;
        this.contactNumber = contactNumber;
        this.postalAddress = postalAddress;
        this.morningAvailabilityStartTime = morningAvailabilityStartTime;
        this.morningAvailabilityEndTime = morningAvailabilityEndTime;
        this.eveningAvailabilityStartTime = eveningAvailabilityStartTime;
        this.eveningAvailabilityEndTime = eveningAvailabilityEndTime;
        this.experience = experience;
        this.educationalQualification = educationalQualification;
        this.specializedCountries = specializedCountries;
        this.isActive = isActive;

    }

    public long getId() {
        return id;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = UserRole.CONSULTANT.name();
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

    public LocalTime getMorningAvailabilityStartTime() {
        return morningAvailabilityStartTime;
    }

    public void setMorningAvailabilityStartTime(LocalTime morningAvailabilityStartTime) {
        this.morningAvailabilityStartTime = morningAvailabilityStartTime;
    }

    public LocalTime getMorningAvailabilityEndTime() {
        return morningAvailabilityEndTime;
    }

    public void setMorningAvailabilityEndTime(LocalTime morningAvailabilityEndTime) {
        this.morningAvailabilityEndTime = morningAvailabilityEndTime;
    }

    public LocalTime getEveningAvailabilityStartTime() {
        return eveningAvailabilityStartTime;
    }

    public void setEveningAvailabilityStartTime(LocalTime eveningAvailabilityStartTime) {
        this.eveningAvailabilityStartTime = eveningAvailabilityStartTime;
    }

    public LocalTime getEveningAvailabilityEndTime() {
        return eveningAvailabilityEndTime;
    }

    public void setEveningAvailabilityEndTime(LocalTime eveningAvailabilityEndTime) {
        this.eveningAvailabilityEndTime = eveningAvailabilityEndTime;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public String getEducationalQualification() {
        return educationalQualification;
    }

    public void setEducationalQualification(String educationalQualification) {
        this.educationalQualification = educationalQualification;
    }

    public String getSpecializedCountries() {
        return specializedCountries;
    }

    public void setSpecializedCountries(String specializedCountries) {
        this.specializedCountries = specializedCountries;
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
