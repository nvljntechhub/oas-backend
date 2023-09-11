package icbt.oas.backend.util;

import icbt.oas.backend.exception.EmailServiceException;
import icbt.oas.backend.repository.UserRepository;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Random;

import org.springframework.mail.javamail.MimeMessageHelper;

public class OASUtil {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Value("${jwt.http.request.header}")
    private String tokenHeader;
    @Value("${spring.mail.username}")
    private String fromAddress;
    @Value("${front.end.baseURI}")
    private String frontEndBaseURI;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JavaMailSender javaMailSender;

    public ResponseEntity buildResponseEntity(HttpStatus httpStatus, String message) {
        String key = httpStatus == HttpStatus.OK ? Constants.STATUS : Constants.ERROR;
        return ResponseEntity.status(httpStatus).contentType(MediaType.APPLICATION_JSON)
                .body("{\"" + key + "\" : \"" + message + "\"}");
    }

    public boolean isBlocked(HttpServletRequest request, String companyId, List<String> roles) {
        Claims claims = getClaims(request);
        String roleClaim = claims.get(Constants.JWT_CLAIM_ROLE).toString();
        String companyIdClaim = claims.get(Constants.JWT_CLAIM_COMPANY_ID).toString();
        return !roles.contains(roleClaim) || !companyIdClaim.equals(companyId);
    }

    public Claims checkAndReturnClaims(HttpServletRequest request, List<String> roles) {
        Claims claims = getClaims(request);
        String roleClaim = claims.get(Constants.JWT_CLAIM_ROLE).toString();
        if (roles.contains(roleClaim)) {
            return claims;
        } else {
            return null;
        }
    }

    public Long findActivationTokenValidPeriod() {
        Long interval;
        String expirationInterval = System.getenv(Constants.ACTIVATION_TOKEN_EXPIRATION);
        if (expirationInterval == null) {
            interval = Constants.ACTIVATION_TOKEN_EXPIRATION_INTERVAL;
        } else {
            interval = Long.parseLong(expirationInterval);
        }
        return interval;
    }

    public Claims getClaims(HttpServletRequest request) {
        String authToken = request.getHeader(tokenHeader);
        String jwtToken = null;
        if (authToken != null && authToken.startsWith(Constants.JWT_TOKEN_TYPE)) {
            jwtToken = authToken.substring(7);
        }
        return jwtTokenUtil.getAllClaimsFromToken(jwtToken);
    }

    public void sendEmail(String email, String subject, String message, String emailType) throws EmailServiceException {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setFrom(fromAddress);
            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setText(message, true);
            javaMailSender.send(mimeMessageHelper.getMimeMessage());
        } catch (RuntimeException | MessagingException e) {
            logger.debug("Failed to send the email");
            throw new EmailServiceException(Constants.EMAIL_SENDING_FAILED + emailType + ". " + Constants.CONTACT_ADMIN, e);
        }
    }

//    public ResponseEntity sendPurchaseConfirmationEmail(ProjectCustomerRequest projectCustomer)
//            throws EmailServiceException {
//        String email = projectCustomer.getEmail();
//        Map<String, Object> claims = new HashMap<>();
//        claims.put(Constants.JWT_CLAIM_COMPANY_ID, projectCustomer.getCompanyId());
//        claims.put(Constants.JWT_CLAIM_PROJECT_CUSTOMER_ID, projectCustomer.getProjectCustomerId());
//        String token = jwtTokenUtil.generateToken(email, claims, findActivationTokenValidPeriod());
//        String baseURI = getFrontEndBaseURI();
//        Date expiryDate = new Date(Long.parseLong(claims.get(JwtTokenUtil.CLAIM_KEY_EXPIRE).toString()) * 1000);
//        String link = baseURI + "/" + Constants.PATH_PURCHASE_CONFIRMATION + "?token=" + token;
//        String subject = Constants.DOMAIN_HIGHLIGHTER + " Please confirm the purchase";
//        String message = "To complete the property allocation, we just need your confirmation "
//                + "before " + expiryDate +
//                "<br/><br/>" +
//                "<a href=\"" + link + "\" target=\"blank\" data-saferedirecturl=\"" + link + "\">" +
//                "<button style=\"background-color: #199319; color: white; padding: 15px 25px; " +
//                "text-decoration: none; cursor: pointer; border: none;\">" +
//                "Confirm The Property Details</button></a>" +
//                "<br/><br/>" +
//                "Link not working? Paste the following link into your browser: " + link;
//        sendEmail(email, subject, message, "confirmation email");
//        return buildResponseEntity(HttpStatus.OK, Constants.CONFIRMATION_EMAIL_SENT);
//    }
//
//    public ResponseEntity sendDeletionConfirmationEmail(ProjectCustomerRequest projectCustomer)
//            throws EmailServiceException {
//        String customerEmail = projectCustomer.getEmail();
//        Map<String, Object> claims = new HashMap<>();
//        claims.put(Constants.JWT_CLAIM_PROJECT_CUSTOMER_ID, projectCustomer.getProjectCustomerId());
//        claims.put(Constants.JWT_CLAIM_COMPANY_ID, projectCustomer.getCompanyId());
//        claims.put(Constants.JWT_CLAIM_CONTRACT_REMOVAL_REASON, projectCustomer.getRemovedReason());
//        String token = jwtTokenUtil.generateToken(customerEmail, claims, findActivationTokenValidPeriod());
//        String baseURI = getFrontEndBaseURI();
//        Date expiryDate = new Date(Long.parseLong(claims.get(JwtTokenUtil.CLAIM_KEY_EXPIRE).toString()) * 1000);
//        String link = baseURI + "/" + Constants.PATH_CONTRACT_DELETION_CONFIRMATION + "?token=" + token;
//        String subject = Constants.DOMAIN_HIGHLIGHTER + " Please confirm the contract removal";
//        String message = "To complete the contract removal, we just need your confirmation "
//                + "before " + expiryDate +
//                "<br/><br/>" +
//                "<a href=\"" + link + "\" target=\"blank\" data-saferedirecturl=\"" + link + "\">" +
//                "<button style=\"background-color: #199319; color: white; padding: 15px 25px; " +
//                "text-decoration: none; cursor: pointer; border: none;\">" +
//                "Confirm The Property Details</button></a>" +
//                "<br/><br/>" +
//                "Link not working? Paste the following link into your browser: " + link;
//        sendEmail(customerEmail, subject, message, "confirmation email");
//        return buildResponseEntity(HttpStatus.OK, Constants.DELETION_CONFIRMATION_EMAIL_SENT);
//    }

    public String getFrontEndBaseURI() {
        String frontEndURIFromEnv = System.getenv(Constants.FRONT_END_URI);
        if (frontEndURIFromEnv == null) {
            return frontEndBaseURI;
        } else {
            return frontEndURIFromEnv;
        }
    }
    public String generatePassword(int len) {
        String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random rnd = new Random();

        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        }
        return sb.toString();
    }

}
