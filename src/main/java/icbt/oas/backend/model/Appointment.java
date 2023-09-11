package icbt.oas.backend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table()
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Getter
    @Column(name = "consultant_id", nullable = false)
    private Long consultantId;
    @Getter
    @Column(name = "job_seeker_id",nullable = false)
    private Long jobSeekerId;
    @Getter
    @Column(name = "appointment_time",length = 100, nullable = false)
    private LocalDateTime appointmentTime;
    @Column(nullable = false, columnDefinition = "tinyint(1) not null default 0")
    private boolean isAccepted;
    @Column(nullable = false, columnDefinition = "tinyint(1) not null default 0")
    private boolean isCompleted;
    @Column(nullable = false, columnDefinition = "tinyint(1) not null default 0")
    private boolean isDeclined;
    @Column(updatable = false)
    protected LocalDateTime createdAt;
    @UpdateTimestamp
    protected LocalDateTime updatedAt;

    public Long getId() {
        return id;
    }

    public Appointment() {
    }

    public Appointment(Long consultantId, Long jobSeekerId, LocalDateTime appointmentTime, boolean isAccepted,
                       boolean isCompleted, boolean isDeclined) {
        this.consultantId = consultantId;
        this.jobSeekerId = jobSeekerId;
        this.appointmentTime = appointmentTime;
        this.isAccepted = isAccepted;
        this.isCompleted = isCompleted;
        this.isDeclined = isDeclined;
    }

    public Long getConsultantId() {
        return consultantId;
    }

    public Long getJobSeekerId() {
        return jobSeekerId;
    }

    public LocalDateTime getAppointmentTime() {
        return appointmentTime;
    }

    public void setConsultantId(Long consultantId) {
        this.consultantId = consultantId;
    }

    public void setJobSeekerId(Long jobSeekerId) {
        this.jobSeekerId = jobSeekerId;
    }

    public void setAppointmentTime(LocalDateTime appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    @JsonProperty("isAccepted")
    public boolean isAccepted() {
        return isAccepted;
    }

    public void setAccepted(boolean accepted) {
        isAccepted = accepted;
    }

    @JsonProperty("isCompleted")
    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    @JsonProperty("isDeclined")
    public boolean isDeclined() {
        return isDeclined;
    }

    public void setDeclined(boolean declined) {
        isDeclined = declined;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
