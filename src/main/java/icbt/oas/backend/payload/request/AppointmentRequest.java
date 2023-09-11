package icbt.oas.backend.payload.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppointmentRequest {
    private Long consultantId;
    private Long jobSeekerId;
    private LocalDateTime appointmentTime;

    public Long getConsultantId() {
        return consultantId;
    }

    public Long getJobSeekerId() {
        return jobSeekerId;
    }

    public LocalDateTime getAppointmentTime() {
        return appointmentTime;
    }


}
