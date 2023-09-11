package icbt.oas.backend.service;

import icbt.oas.backend.exception.EmailServiceException;
import icbt.oas.backend.model.JobSeeker;
import icbt.oas.backend.model.UserRole;
import icbt.oas.backend.payload.request.JobSeekerRequest;
import icbt.oas.backend.repository.JobSeekerRepository;
import icbt.oas.backend.util.Constants;
import icbt.oas.backend.util.JwtTokenUtil;
import icbt.oas.backend.util.OASUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service("jobSeekerCreateService")
public class JobSeekerSignUpService {
    @Autowired
    private OASUtil oasUtil;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private JobSeekerRepository jobSeekerRepository;

    public ResponseEntity createJobSeeker(JobSeekerRequest jobSeekerRequest) throws RuntimeException {
        String email = jobSeekerRequest.getEmail();
        String password = jobSeekerRequest.getPassword();
        if(jobSeekerRequest.getPassword() == null){
            password = oasUtil.generatePassword(10);
        }
        JobSeeker newJobSeeker = new JobSeeker(jobSeekerRequest.getFirstName(),
                jobSeekerRequest.getLastName(),
                jobSeekerRequest.getEmail(), password, UserRole.JOB_SEEKER.name(),
                jobSeekerRequest.getContactNumber(), jobSeekerRequest.getPostalAddress(),
                jobSeekerRequest.getHighestQualification(), jobSeekerRequest.getJob(),
                jobSeekerRequest.getJobExperience(), jobSeekerRequest.getInterestedCountries(),
                jobSeekerRequest.getInterestedJobs(),
                false);
        jobSeekerRepository.save(newJobSeeker);
        return sendActivationEmail(email, password);
    }

    public ResponseEntity sendActivationEmail(String email, String password) throws EmailServiceException {
        Map<String, Object> claims = new HashMap<>();
        String token = jwtTokenUtil.generateToken(email, claims, oasUtil.findActivationTokenValidPeriod());
        String baseURI = oasUtil.getFrontEndBaseURI();
        Date expiryDate = new Date(Long.parseLong(claims.get(JwtTokenUtil.CLAIM_KEY_EXPIRE).toString()) * 1000);
        String link = baseURI + "/job-seeker-creation/"  + "?token=" + token;
        String subject = Constants.DOMAIN_HIGHLIGHTER + " Please verify your email address.";
        String message = "To complete your sign up, we just need to verify your email address: "
                + email + " before " + expiryDate +
                "<br/><br/>" +
                "<a href=\"" + link + "\" target=\"blank\" data-saferedirecturl=\"" + link + "\">" +
                "<button style=\"background-color: #199319; color: white; padding: 15px 25px; " +
                "text-decoration: none; cursor: pointer; border: none;\">Verify email address</button></a>" +
                "<br/><br/>" +
                "Link not working? Paste the following link into your browser: " + link +
                "<br/><br/>" + "Password: " + password;
        oasUtil.sendEmail(email, subject, message, "activation email");
        return oasUtil.buildResponseEntity(HttpStatus.OK, Constants.JOB_SEEKER_CREATED_SUCCESSFULLY
                + " and " +  Constants.ACTIVATION_EMAIL_SENT);
    }
}
