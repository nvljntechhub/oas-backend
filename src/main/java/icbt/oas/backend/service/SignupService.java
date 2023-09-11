package icbt.oas.backend.service;

import icbt.oas.backend.exception.EmailServiceException;
import icbt.oas.backend.model.User;
import icbt.oas.backend.model.UserRole;
import icbt.oas.backend.payload.request.AuthRequest;
import icbt.oas.backend.repository.UserRepository;
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

@Service("signupService")
public class SignupService {

    @Autowired
    private OASUtil cpdUtil;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UserRepository userRepository;

    public User signup(AuthRequest request) throws RuntimeException {
        String email = request.getEmail();
        User user = new User(request.getFirstName(), request.getLastName(), email,
                request.getPassword(),UserRole.ADMIN.name());
        return userRepository.save(user);
    }

    public ResponseEntity sendActivationEmail(String email) throws EmailServiceException {
        Map<String, Object> claims = new HashMap<>();
        String token = jwtTokenUtil.generateToken(email, claims, cpdUtil.findActivationTokenValidPeriod());
        String baseURI = cpdUtil.getFrontEndBaseURI();
        Date expiryDate = new Date(Long.parseLong(claims.get(JwtTokenUtil.CLAIM_KEY_EXPIRE).toString()) * 1000);
        String link = baseURI + "/signin/" + "?token=" + token;
        String subject = Constants.DOMAIN_HIGHLIGHTER + " Please verify your email address.";
        String message = "To complete your sign up, we just need to verify your email address: "
                + email + " before " + expiryDate +
                "<br/><br/>" +
                "<a href=\"" + link + "\" target=\"blank\" data-saferedirecturl=\"" + link + "\">" +
                "<button style=\"background-color: #199319; color: white; padding: 15px 25px; " +
                "text-decoration: none; cursor: pointer; border: none;\">Verify email address</button></a>" +
                "<br/><br/>" +
                "Link not working? Paste the following link into your browser: " + link;
        cpdUtil.sendEmail(email, subject, message, "activation email");
        return cpdUtil.buildResponseEntity(HttpStatus.OK, Constants.ACTIVATION_EMAIL_SENT);
    }
}
