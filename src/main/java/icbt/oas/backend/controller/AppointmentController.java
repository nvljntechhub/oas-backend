package icbt.oas.backend.controller;


import icbt.oas.backend.model.Appointment;
import icbt.oas.backend.model.Consultant;
import icbt.oas.backend.model.JobSeeker;
import icbt.oas.backend.model.UserRole;
import icbt.oas.backend.payload.request.AppointmentDetails;
import icbt.oas.backend.payload.request.AppointmentRequest;
import icbt.oas.backend.repository.AppointmentRepository;
import icbt.oas.backend.repository.ConsultantRepository;
import icbt.oas.backend.repository.JobSeekerRepository;
import icbt.oas.backend.util.JwtTokenUtil;
import icbt.oas.backend.util.OASUtil;
import icbt.oas.backend.util.Constants;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("${base.uri}")
public class AppointmentController {
    @Value("${jwt.token.expiration.in.seconds}")
    private Long expiration;
    @Value("${jwt.http.request.header}")
    private String tokenHeader;
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private ConsultantRepository consultantRepository;
    @Autowired
    private JobSeekerRepository jobSeekerRepository;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private OASUtil oasUtil;
    private static List<String> ALLOWED_ROLES = Arrays.asList(UserRole.ADMIN.name(), UserRole.CONSULTANT.name(),
            UserRole.JOB_SEEKER.name());
    private static List<String> MODERATOR_ROLES = Arrays.asList(UserRole.ADMIN.name(), UserRole.CONSULTANT.name());

    /**
     * Get all appointments
     */
    @GetMapping("${appointment.uri}")
    public ResponseEntity<?> getAll(HttpServletRequest request) {
        try {
            Claims claims = oasUtil.checkAndReturnClaims(request, ALLOWED_ROLES);
            if (claims == null) {
                return oasUtil.buildResponseEntity(HttpStatus.FORBIDDEN, Constants.PERMISSION_DENIED);
            }

            List<AppointmentDetails> appointments = appointmentRepository.findAll().stream().map(appointment -> {
                Optional<Consultant> consultant = consultantRepository.findById(appointment.getConsultantId());
                Optional<JobSeeker> jobSeeker = jobSeekerRepository.findById(appointment.getJobSeekerId());

                // Create an object to represent the desired structure
                AppointmentDetails appointmentInfo = new AppointmentDetails();
                appointmentInfo.setAppointment(appointment);
                appointmentInfo.setConsultant(consultant.orElse(null));
                appointmentInfo.setJobSeeker(jobSeeker.orElse(null));

                return appointmentInfo;
            }).collect(Collectors.toList());

            System.out.println(1);
            System.out.println(appointments);

            return ResponseEntity.ok(appointments);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            return oasUtil.buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, Constants.SYSTEM_ERROR);
        }
    }

    /**
     * Create appointment
     */
    @PostMapping("${appointment.uri}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> create(HttpServletRequest request, @RequestBody AppointmentRequest appointmentRequest) {
        try {
            Claims claims = oasUtil.checkAndReturnClaims(request, MODERATOR_ROLES);
            if (claims == null) {
                return oasUtil.buildResponseEntity(HttpStatus.FORBIDDEN, Constants.PERMISSION_DENIED);
            }

            Appointment newAppointment = new Appointment(appointmentRequest.getConsultantId(),
                    appointmentRequest.getJobSeekerId(), appointmentRequest.getAppointmentTime(),false,
                    false,false);
            appointmentRepository.save(newAppointment);
            return oasUtil.buildResponseEntity(HttpStatus.OK, Constants.APPOINTMENT_CREATED_SUCCESSFULLY);
        } catch (RuntimeException e) {
            return oasUtil.buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, Constants.SYSTEM_ERROR);
        }
    }

    /**
     * Update appointment
     */
    @PutMapping("${appointment.id.uri}")
    public ResponseEntity<?> update(HttpServletRequest request, @PathVariable Long id,
                                    @RequestBody AppointmentRequest appointmentRequest) {
        try {
            Claims claims = oasUtil.checkAndReturnClaims(request, ALLOWED_ROLES);
            if (claims == null) {
                return oasUtil.buildResponseEntity(HttpStatus.FORBIDDEN, Constants.PERMISSION_DENIED);
            }
            Appointment appointment = findById(id);

            if (appointment == null) {
                return oasUtil.buildResponseEntity(HttpStatus.NOT_FOUND, Constants.APPOINTMENT_NOT_FOUND);
            }

            Long consultantId = appointmentRequest.getConsultantId();
            Long jobSeekerId = appointmentRequest.getJobSeekerId();
            LocalDateTime appointmentTime = appointmentRequest.getAppointmentTime();

            if (consultantId != null) appointment.setConsultantId(consultantId);
            if (jobSeekerId != null) appointment.setJobSeekerId(jobSeekerId);
            if (appointmentTime != null) appointment.setAppointmentTime(appointmentTime);

            appointmentRepository.save(appointment);

            return oasUtil.buildResponseEntity(HttpStatus.OK, Constants.APPOINTMENT_UPDATED_SUCCESSFULLY);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            return oasUtil.buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, Constants.SYSTEM_ERROR);
        }
    }

    /**
     * accept appointment by ID
     */
    @PutMapping("${appointment.accept.status.uri}")
    public ResponseEntity<?> acceptAppointment(HttpServletRequest request, @PathVariable Long id) {
        try {
            Claims claims = oasUtil.checkAndReturnClaims(request, ALLOWED_ROLES);
            if (claims == null) {
                return oasUtil.buildResponseEntity(HttpStatus.FORBIDDEN, Constants.PERMISSION_DENIED);
            }

            Appointment appointment = findById(id);

            if (appointment == null) {
                return oasUtil.buildResponseEntity(HttpStatus.NOT_FOUND, Constants.APPOINTMENT_NOT_FOUND);
            }

            appointment.setAccepted(true);

            appointmentRepository.save(appointment);

            return oasUtil.buildResponseEntity(HttpStatus.OK, Constants.APPOINTMENT_ACCEPTED_SUCCESSFULLY);
        } catch (RuntimeException e) {
            e.getMessage();
            return oasUtil.buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, Constants.SYSTEM_ERROR);
        }
    }

    /**
     * decline appointment by ID
     */
    @PutMapping("${appointment.decline.status.uri}")
    public ResponseEntity<?> declineStatus(HttpServletRequest request, @PathVariable Long id) {
        try {
            Claims claims = oasUtil.checkAndReturnClaims(request, ALLOWED_ROLES);
            if (claims == null) {
                return oasUtil.buildResponseEntity(HttpStatus.FORBIDDEN, Constants.PERMISSION_DENIED);
            }

            Appointment appointment = findById(id);

            if (appointment == null) {
                return oasUtil.buildResponseEntity(HttpStatus.NOT_FOUND, Constants.APPOINTMENT_NOT_FOUND);
            }

            appointment.setAccepted(false);
            appointment.setDeclined(true);

            appointmentRepository.save(appointment);

            return oasUtil.buildResponseEntity(HttpStatus.OK, Constants.APPOINTMENT_DECLINED_SUCCESSFULLY);
        } catch (RuntimeException e) {
            return oasUtil.buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, Constants.SYSTEM_ERROR);
        }
    }

    /**
     * Delete by ID
     */
    @DeleteMapping("${appointment.id.uri}")
    public ResponseEntity<?> delete(HttpServletRequest request, @PathVariable Long id) {
        try {
            Claims claims = oasUtil.checkAndReturnClaims(request, ALLOWED_ROLES);
            if (claims == null) {
                return oasUtil.buildResponseEntity(HttpStatus.FORBIDDEN, Constants.PERMISSION_DENIED);
            }

            Appointment appointment = findById(id);
            if (appointment == null) {
                return oasUtil.buildResponseEntity(HttpStatus.NOT_FOUND, Constants.INVALID_ID);
            }
            appointmentRepository.delete(appointment);
            return oasUtil.buildResponseEntity(HttpStatus.OK, Constants.APPOINTMENT_DELETED_SUCCESSFULLY);
        } catch (RuntimeException e) {
            return oasUtil.buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, Constants.SYSTEM_ERROR);
        }
    }
    private Appointment findById(Long id) {
        return appointmentRepository.findById(id).orElse(null);
    }


}
