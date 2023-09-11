package icbt.oas.backend.controller;

import icbt.oas.backend.model.Consultant;
import icbt.oas.backend.model.UserRole;
import icbt.oas.backend.payload.request.ConsultantRequest;
import icbt.oas.backend.repository.ConsultantRepository;
import icbt.oas.backend.service.ConsultantSignUpService;
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
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("${base.uri}")
public class ConsultantController {
    @Value("${jwt.token.expiration.in.seconds}")
    private Long expiration;
    @Value("${jwt.http.request.header}")
    private String tokenHeader;
    @Autowired
    private ConsultantRepository consultantRepository;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private OASUtil oasUtil;
    @Autowired
    private ConsultantSignUpService consultantSignUpService;

    private static List<String> ALLOWED_ROLES = Arrays.asList(UserRole.ADMIN.name(), UserRole.CONSULTANT.name());
    private static List<String> MODERATOR_ROLES = Arrays.asList(UserRole.ADMIN.name(), UserRole.CONSULTANT.name());


    /**
     * Get all consultants
     */
    @GetMapping("${consultant.uri}")
    public ResponseEntity<?> getAll(HttpServletRequest request) {
        try {
            Claims claims = oasUtil.checkAndReturnClaims(request, ALLOWED_ROLES);
            if (claims == null) {
                return oasUtil.buildResponseEntity(HttpStatus.FORBIDDEN, Constants.PERMISSION_DENIED);
            }
            return ResponseEntity.ok(consultantRepository.findAll());
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            return oasUtil.buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, Constants.SYSTEM_ERROR);
        }
    }
    /**
     * Create consultant
     */
    @PostMapping("${consultant.uri}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> create(HttpServletRequest request, @RequestBody ConsultantRequest consultantDetails) {
        try {
            Claims claims = oasUtil.checkAndReturnClaims(request, ALLOWED_ROLES);
            if (claims == null) {
                return oasUtil.buildResponseEntity(HttpStatus.FORBIDDEN, Constants.PERMISSION_DENIED);
            }
            if (consultantRepository.existsByEmail(consultantDetails.getEmail())) {
                return oasUtil.buildResponseEntity(HttpStatus.BAD_REQUEST, Constants.EMAIL_EXISTS);
            }
            return consultantSignUpService.createConsultant(consultantDetails);
        } catch (RuntimeException e) {
            return oasUtil.buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, Constants.SYSTEM_ERROR);
        }
    }


    /**
     * Update consultant
     */
    @PutMapping("${consultant.id.uri}")
    public ResponseEntity<?> update(HttpServletRequest request, @PathVariable Long id,
                                    @RequestBody ConsultantRequest consultantRequest) {
        try {
            Claims claims = oasUtil.checkAndReturnClaims(request, ALLOWED_ROLES);
            if (claims == null) {
                return oasUtil.buildResponseEntity(HttpStatus.FORBIDDEN, Constants.PERMISSION_DENIED);
            }
            Consultant consultant = findById(id);

            if (consultant == null) {
                return oasUtil.buildResponseEntity(HttpStatus.NOT_FOUND, Constants.INVALID_EMAIL);
            }

            String firstName = consultantRequest.getFirstName();
            String lastName = consultantRequest.getLastName();
            String email = consultantRequest.getEmail();
            String contactNumber = consultantRequest.getContactNumber();
            String postalAddress = consultantRequest.getPostalAddress();
            LocalTime morningAvailabilityStartTime = consultantRequest.getMorningAvailabilityStartTime();
            LocalTime morningAvailabilityEndTime = consultantRequest.getMorningAvailabilityEndTime();
            LocalTime eveningAvailabilityStartTime = consultantRequest.getEveningAvailabilityStartTime();
            LocalTime eveningAvailabilityEndTime = consultantRequest.getEveningAvailabilityEndTime();
            Integer experience = consultantRequest.getExperience();
            String educationalQualification = consultantRequest.getEducationalQualification();
            String specializedCountries = consultantRequest.getSpecializedCountries();

            if (firstName != null) consultant.setFirstName(firstName);
            if (lastName != null) consultant.setLastName(lastName);
            if (email != null) consultant.setEmail(email);
            if (contactNumber != null) consultant.setContactNumber(contactNumber);
            if(postalAddress != null) consultant.setPostalAddress(postalAddress);
            if(morningAvailabilityStartTime != null)
                consultant.setMorningAvailabilityStartTime(morningAvailabilityStartTime);
            if(morningAvailabilityEndTime != null)
                consultant.setMorningAvailabilityEndTime(morningAvailabilityEndTime);
            if(eveningAvailabilityStartTime != null)
                consultant.setEveningAvailabilityStartTime(eveningAvailabilityStartTime);
            if(eveningAvailabilityEndTime != null) consultant.setEveningAvailabilityEndTime(eveningAvailabilityEndTime);
            if(experience != null) consultant.setExperience(experience);
            if(educationalQualification != null) consultant.setEducationalQualification(educationalQualification);
            if(specializedCountries != null) consultant.setSpecializedCountries(specializedCountries);

            consultantRepository.save(consultant);

            return oasUtil.buildResponseEntity(HttpStatus.OK, Constants.CONSULTANT_UPDATED_SUCCESSFULLY);
        }catch (DataIntegrityViolationException e) {
            if (Objects.requireNonNull(e.getMessage()).contains("consultant.UNIQUE_KEY")) {
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
     * Delete user by ID
     */
    @DeleteMapping("${consultant.id.uri}")
    public ResponseEntity<?> delete(HttpServletRequest request, @PathVariable Long id) {
        try {
            Claims claims = oasUtil.checkAndReturnClaims(request, ALLOWED_ROLES);
            if (claims == null) {
                return oasUtil.buildResponseEntity(HttpStatus.FORBIDDEN, Constants.PERMISSION_DENIED);
            }

            Consultant consultant = findById(id);
            if (consultant == null) {
                return oasUtil.buildResponseEntity(HttpStatus.NOT_FOUND, Constants.INVALID_ID);
            }
            consultantRepository.delete(consultant);
            return oasUtil.buildResponseEntity(HttpStatus.OK, Constants.CONSULTANT_DELETED_SUCCESSFULLY);
        } catch (RuntimeException e) {
            return oasUtil.buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, Constants.SYSTEM_ERROR);
        }
    }
    private Consultant findById(Long id) {
        return consultantRepository.findById(id).orElse(null);
    }
}
