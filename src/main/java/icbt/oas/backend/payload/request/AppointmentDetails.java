package icbt.oas.backend.payload.request;

import icbt.oas.backend.model.Appointment;
import icbt.oas.backend.model.Consultant;
import icbt.oas.backend.model.JobSeeker;

public class AppointmentDetails {
    private Appointment appointment;
    private Consultant consultant;
    private JobSeeker jobSeeker;

    public AppointmentDetails() {
    }


    public AppointmentDetails(Appointment appointment, Consultant consultant, JobSeeker jobSeeker) {
        this.appointment = appointment;
        this.consultant = consultant;
        this.jobSeeker = jobSeeker;
    }


    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    public Consultant getConsultant() {
        return consultant;
    }

    public void setConsultant(Consultant consultant) {
        this.consultant = consultant;
    }

    public JobSeeker getJobSeeker() {
        return jobSeeker;
    }

    public void setJobSeeker(JobSeeker jobSeeker) {
        this.jobSeeker = jobSeeker;
    }


}
