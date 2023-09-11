package icbt.oas.backend.payload.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConsultantRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String role;
    private String contactNumber;
    private String postalAddress;
    private LocalTime morningAvailabilityStartTime;
    private LocalTime morningAvailabilityEndTime;
    private LocalTime eveningAvailabilityStartTime;
    private LocalTime eveningAvailabilityEndTime;
    private Integer experience;
    private String educationalQualification;
    private String specializedCountries;

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

    public LocalTime getMorningAvailabilityStartTime() {
        return morningAvailabilityStartTime;
    }

    public LocalTime getMorningAvailabilityEndTime() {
        return morningAvailabilityEndTime;
    }

    public LocalTime getEveningAvailabilityStartTime() {
        return eveningAvailabilityStartTime;
    }

    public LocalTime getEveningAvailabilityEndTime() {
        return eveningAvailabilityEndTime;
    }

    public Integer getExperience() {
        return experience;
    }

    public String getEducationalQualification() {
        return educationalQualification;
    }

    public String getSpecializedCountries() {
        return specializedCountries;
    }
}
