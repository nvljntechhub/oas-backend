package icbt.oas.backend.controller;

import icbt.oas.backend.model.JobSeeker;
import icbt.oas.backend.model.UserRole;
import icbt.oas.backend.payload.request.JobSeekerRequest;
import icbt.oas.backend.repository.JobSeekerRepository;
import icbt.oas.backend.service.JobSeekerSignUpService;
import icbt.oas.backend.util.Constants;
import icbt.oas.backend.util.JwtTokenUtil;
import icbt.oas.backend.util.OASUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("${base.uri}")
public class JobSeekerController {
    @Value("${jwt.token.expiration.in.seconds}")
    private Long expiration;
    @Value("${jwt.http.request.header}")
    private String tokenHeader;
    @Autowired
    private JobSeekerRepository jobSeekerRepository;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private OASUtil oasUtil;
    @Autowired
    private JobSeekerSignUpService jobSeekerSignUpService;

    private static List<String> ALLOWED_ROLES = Arrays.asList(UserRole.ADMIN.name(), UserRole.CONSULTANT.name(), UserRole.JOB_SEEKER.name());
    private static List<String> MODERATOR_ROLES = Arrays.asList(UserRole.ADMIN.name(), UserRole.CONSULTANT.name());

    /**
     * Get all jobseekers
     */
    @GetMapping("${job.seeker.uri}")
    public ResponseEntity<?> getAll(HttpServletRequest request) {
        try {
            Claims claims = oasUtil.checkAndReturnClaims(request, ALLOWED_ROLES);
            if (claims == null) {
                return oasUtil.buildResponseEntity(HttpStatus.FORBIDDEN, Constants.PERMISSION_DENIED);
            }
            return ResponseEntity.ok(jobSeekerRepository.findAll());
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            return oasUtil.buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, Constants.SYSTEM_ERROR);
        }
    }

    /**
     * Get jobseeker by id
     */
    @GetMapping("${job.seeker.id.uri}")
    public ResponseEntity<?> getAll(HttpServletRequest request,@PathVariable Long id) {
        try {
            Claims claims = oasUtil.checkAndReturnClaims(request, ALLOWED_ROLES);
            if (claims == null) {
                return oasUtil.buildResponseEntity(HttpStatus.FORBIDDEN, Constants.PERMISSION_DENIED);
            }

            JobSeeker jobSeeker = findById(id);
            if (jobSeeker == null) {
                return oasUtil.buildResponseEntity(HttpStatus.NOT_FOUND, Constants.INVALID_EMAIL);
            }
            return ResponseEntity.ok(jobSeeker);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            return oasUtil.buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, Constants.SYSTEM_ERROR);
        }
    }


    /**
     * Create consultant
     */
    @PostMapping("${job.seeker.uri}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> create(HttpServletRequest request, @RequestBody JobSeekerRequest jobSeekerRequest) {
        try {
            Claims claims = oasUtil.checkAndReturnClaims(request, ALLOWED_ROLES);
            if (claims == null) {
                return oasUtil.buildResponseEntity(HttpStatus.FORBIDDEN, Constants.PERMISSION_DENIED);
            }
            if (jobSeekerRepository.existsByEmail(jobSeekerRequest.getEmail())) {
                return oasUtil.buildResponseEntity(HttpStatus.BAD_REQUEST, Constants.EMAIL_EXISTS);
            }
            return jobSeekerSignUpService.createJobSeeker(jobSeekerRequest);
        } catch (RuntimeException e) {
            return oasUtil.buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, Constants.SYSTEM_ERROR);
        }
    }

    /**
     * Update jobseeker
     */
    @PutMapping("${job.seeker.id.uri}")
    public ResponseEntity<?> update(HttpServletRequest request, @PathVariable Long id,
                                    @RequestBody JobSeekerRequest jobSeekerRequest) {
        try {
            Claims claims = oasUtil.checkAndReturnClaims(request, ALLOWED_ROLES);
            if (claims == null) {
                return oasUtil.buildResponseEntity(HttpStatus.FORBIDDEN, Constants.PERMISSION_DENIED);
            }
            JobSeeker jobSeeker = findById(id);
            if (jobSeeker == null) {
                return oasUtil.buildResponseEntity(HttpStatus.NOT_FOUND, Constants.INVALID_EMAIL);
            }

            String firstName = jobSeekerRequest.getFirstName();
            String lastName = jobSeekerRequest.getLastName();
            String email = jobSeekerRequest.getEmail();
            String contactNumber = jobSeekerRequest.getContactNumber();
            String postalAddress = jobSeekerRequest.getPostalAddress();
            String highestQualification = jobSeekerRequest.getHighestQualification();
            String job = jobSeekerRequest.getJob();
            Integer jobExperience = jobSeekerRequest.getJobExperience();
            String interestedCountries = jobSeekerRequest.getInterestedCountries();
            String interestedJobs = jobSeekerRequest.getInterestedJobs();
            boolean isActive = jobSeekerRequest.isActive();

            if (firstName != null) jobSeeker.setFirstName(firstName);
            if (lastName != null) jobSeeker.setLastName(lastName);
            if (email != null) jobSeeker.setEmail(email);
            if (contactNumber != null) jobSeeker.setContactNumber(contactNumber);
            if(postalAddress != null) jobSeeker.setPostalAddress(postalAddress);
            if(highestQualification != null)
                jobSeeker.setHighestQualification(highestQualification);
            if(job != null)
                jobSeeker.setJob(job);
            if(jobExperience != null)
                jobSeeker.setJobExperience(jobExperience);
            if(interestedCountries != null) jobSeeker.setInterestedCountries(interestedCountries);
            if(interestedJobs != null) jobSeeker.setInterestedJobs(interestedJobs);
            if(isActive ) jobSeeker.setActive(!isActive);

            jobSeekerRepository.save(jobSeeker);

            return oasUtil.buildResponseEntity(HttpStatus.OK, Constants.JOB_SEEKER_UPDATED_SUCCESSFULLY);
        }catch (DataIntegrityViolationException e) {
            if (Objects.requireNonNull(e.getMessage()).contains("job_seeker.UNIQUE_KEY")) {
                return oasUtil.buildResponseEntity(HttpStatus.BAD_REQUEST, Constants.EMAIL_EXISTS);
            } else {
                return oasUtil.buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, Constants.SYSTEM_ERROR);
            }
        }  catch (RuntimeException e) {
            System.out.println(e.getMessage());
            return oasUtil.buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, Constants.SYSTEM_ERROR);
        }
    }

    /**
     * Delete jobseeker by ID
     */
    @DeleteMapping("${job.seeker.id.uri}")
    public ResponseEntity<?> delete(HttpServletRequest request, @PathVariable Long id) {
        try {
            Claims claims = oasUtil.checkAndReturnClaims(request, ALLOWED_ROLES);
            if (claims == null) {
                return oasUtil.buildResponseEntity(HttpStatus.FORBIDDEN, Constants.PERMISSION_DENIED);
            }

            JobSeeker jobSeeker = findById(id);
            if (jobSeeker == null) {
                return oasUtil.buildResponseEntity(HttpStatus.NOT_FOUND, Constants.INVALID_ID);
            }
            jobSeekerRepository.delete(jobSeeker);
            return oasUtil.buildResponseEntity(HttpStatus.OK, Constants.JOB_SEEKER_DELETED_SUCCESSFULLY);
        } catch (RuntimeException e) {
            return oasUtil.buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, Constants.SYSTEM_ERROR);
        }
    }

    private JobSeeker findById(Long id) {
        return jobSeekerRepository.findById(id).orElse(null);
    }
}
