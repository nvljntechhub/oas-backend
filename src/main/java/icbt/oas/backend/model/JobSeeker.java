package icbt.oas.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "job_seeker",uniqueConstraints = {
        @UniqueConstraint(columnNames = "email")
})
public class JobSeeker {
    public static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Getter
    @Column(length = 100, nullable = false)
    private String firstName;
    @Getter
    @Column(length = 100, nullable = false)
    private String lastName;
    @Getter
    @Column(length = 100, nullable = false)
    private String email;
    @Getter
    @Column(length = 100, nullable = false)
    @ToString.Exclude private @JsonIgnore String password;
    @Getter
    @Column(length = 15, nullable = false)
    private String role;
    @Getter
    @Column(name = "contact_number", length = 15, nullable = false)
    private String contactNumber;
    @Getter
    @Column(name = "postal_address",length = 250, nullable = false)
    private String postalAddress;
    @Getter
    @Column(name = "highest_qualification",length = 500, nullable = false)
    private String highestQualification;
    @Getter
    @Column(length = 500, nullable = false)
    private String job;
    @Getter
    @Column(name = "job_experience",length = 500, nullable = false)
    private Integer jobExperience;
    @Getter
    @Column(name = "interested_countries",length = 500, nullable = false)
    private String interestedCountries;
    @Getter
    @Column(name = "interested_jobs",length = 200, nullable = false)
    private String interestedJobs;
    @Getter
    @Column(nullable = false, columnDefinition = "tinyint(1) not null default 0")
    private boolean isActive;
    @Column(updatable = false)
    protected LocalDateTime createdAt;
    @UpdateTimestamp
    protected LocalDateTime updatedAt;

    public JobSeeker() {
    }

    public JobSeeker(String firstName, String lastName, String email, String password, String role,
                     String contactNumber, String postalAddress, String highestQualification, String job,
                     Integer jobExperience, String interestedCountries, String interestedJobs, boolean isActive) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = PASSWORD_ENCODER.encode(password);
        this.role = role;
        this.contactNumber = contactNumber;
        this.postalAddress = postalAddress;
        this.highestQualification = highestQualification;
        this.job = job;
        this.jobExperience = jobExperience;
        this.interestedCountries = interestedCountries;
        this.interestedJobs = interestedJobs;
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
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
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

    public String getHighestQualification() {
        return highestQualification;
    }

    public void setHighestQualification(String highestQualification) {
        this.highestQualification = highestQualification;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public Integer getJobExperience() {
        return jobExperience;
    }

    public void setJobExperience(Integer jobExperience) {
        this.jobExperience = jobExperience;
    }

    public String getInterestedCountries() {
        return interestedCountries;
    }

    public void setInterestedCountries(String interestedCountries) {
        this.interestedCountries = interestedCountries;
    }

    public String getInterestedJobs() {
        return interestedJobs;
    }

    public void setInterestedJobs(String interestedJobs) {
        this.interestedJobs = interestedJobs;
    }

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
