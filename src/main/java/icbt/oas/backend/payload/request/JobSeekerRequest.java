package icbt.oas.backend.payload.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JobSeekerRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String role;
    private String contactNumber;
    private String postalAddress;
    private String highestQualification;
    private String job;
    private Integer jobExperience;
    private String interestedCountries;
    private String interestedJobs;
    private boolean isActive;

    void store(MultipartFile file) {

    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public String getPostalAddress() {
        return postalAddress;
    }

    public String getHighestQualification() {
        return highestQualification;
    }

    public String getJob() {
        return job;
    }

    public Integer getJobExperience() {
        return jobExperience;
    }

    public String getInterestedCountries() {
        return interestedCountries;
    }

    public String getInterestedJobs() {
        return interestedJobs;
    }

    public boolean isActive() {
        return isActive;
    }
}
