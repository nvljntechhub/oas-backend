package icbt.oas.backend.service;

import icbt.oas.backend.exception.EmailServiceException;
import icbt.oas.backend.model.Consultant;
import icbt.oas.backend.model.UserRole;
import icbt.oas.backend.payload.request.ConsultantRequest;
import icbt.oas.backend.repository.ConsultantRepository;
import icbt.oas.backend.util.Constants;
import icbt.oas.backend.util.JwtTokenUtil;
import icbt.oas.backend.util.OASUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service("consultantCreateService")
public class ConsultantSignUpService {
    @Autowired
    private OASUtil oasUtil;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private ConsultantRepository consultantRepository;

    @Transactional(rollbackFor = {RuntimeException.class, EmailServiceException.class})
    public ResponseEntity createConsultant(ConsultantRequest consultantDetails) throws RuntimeException {
        String email = consultantDetails.getEmail();
        String password = consultantDetails.getPassword();
        if(consultantDetails.getPassword() == null){
            password = oasUtil.generatePassword(10);
        }

        Consultant newConsultant = new Consultant( consultantDetails.getFirstName(),
                consultantDetails.getLastName(),
                consultantDetails.getEmail(), password, UserRole.CONSULTANT.name(),
                consultantDetails.getContactNumber(), consultantDetails.getPostalAddress(),
                consultantDetails.getMorningAvailabilityStartTime(),
                consultantDetails.getMorningAvailabilityEndTime(),
                consultantDetails.getEveningAvailabilityStartTime(),
                consultantDetails.getEveningAvailabilityEndTime(),consultantDetails.getExperience(),
                consultantDetails.getEducationalQualification(), consultantDetails.getSpecializedCountries(),
                false);
        consultantRepository.save(newConsultant);
        return sendActivationEmail(email, password);
    }

    public ResponseEntity sendActivationEmail(String email, String generatedPassword) throws EmailServiceException {
        Map<String, Object> claims = new HashMap<>();
        String token = jwtTokenUtil.generateToken(email, claims, oasUtil.findActivationTokenValidPeriod());
        String baseURI = oasUtil.getFrontEndBaseURI();
        Date expiryDate = new Date(Long.parseLong(claims.get(JwtTokenUtil.CLAIM_KEY_EXPIRE).toString()) * 1000);
        String link = baseURI + "/consultant-creation/"  + "?token=" + token;
        String subject = Constants.DOMAIN_HIGHLIGHTER + " Please verify your email address.";
        String message = "To complete your sign up, we just need to verify your email address: "
                + email + " before " + expiryDate +
                "<br/><br/>" +
                "<a href=\"" + link + "\" target=\"blank\" data-saferedirecturl=\"" + link + "\">" +
                "<button style=\"background-color: #199319; color: white; padding: 15px 25px; " +
                "text-decoration: none; cursor: pointer; border: none;\">Verify email address</button></a>" +
                "<br/><br/>" +
                "Link not working? Paste the following link into your browser: " + link +
                "<br/><br/>" + "Password: " + generatedPassword;
        oasUtil.sendEmail(email, subject, message, "activation email");
        return oasUtil.buildResponseEntity(HttpStatus.OK, Constants.CONSULTANT_CREATED_SUCCESSFULLY
                + " and " +  Constants.ACTIVATION_EMAIL_SENT);
    }
}
