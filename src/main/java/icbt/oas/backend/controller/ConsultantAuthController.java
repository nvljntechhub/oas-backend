package icbt.oas.backend.controller;

import icbt.oas.backend.exception.OASException;
import icbt.oas.backend.model.Consultant;
import icbt.oas.backend.payload.request.AuthRequest;
import icbt.oas.backend.payload.request.ConsultantRequest;
import icbt.oas.backend.payload.response.SignInResponse;
import icbt.oas.backend.repository.ConsultantRepository;
import icbt.oas.backend.service.ConsultantSignUpService;
import icbt.oas.backend.util.Constants;
import icbt.oas.backend.util.JwtTokenUtil;
import icbt.oas.backend.util.OASUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("${auth.api.uri}")
public class ConsultantAuthController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    public static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();
    @Value("${jwt.http.request.header}")
    private String tokenHeader;
    @Value("${jwt.token.expiration.in.seconds}")
    private Long expiration;
    @Autowired
    private ConsultantRepository consultantRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private OASUtil oasUtil;
    @Autowired
    private ConsultantSignUpService consultantSignUpService;

    @PostMapping("${consultant.signin.uri}")
    public ResponseEntity<?> consultantSignIn(@RequestBody AuthRequest request) {
        try {
            String email = request.getEmail();
            Consultant consultant = consultantRepository.findByEmail(email).orElse(null);
            if (consultant == null) {
                return oasUtil.buildResponseEntity(HttpStatus.NOT_FOUND, Constants.INVALID_EMAIL);
            }
            boolean passwordCheck = PASSWORD_ENCODER.matches(request.getPassword(),consultant.getPassword());
            if(!passwordCheck) {
                return oasUtil.buildResponseEntity(HttpStatus.UNAUTHORIZED, Constants.INVALID_PASSWORD);
            }

            String role = consultant.getRole();
            Map<String, Object> claims = new HashMap<>();
            claims.put(Constants.JWT_CLAIM_ROLE, role);
            Long interval;
            String expirationInterval = System.getenv(Constants.JWT_TOKEN_EXPIRATION);
            if (expirationInterval == null) {
                interval = expiration;
            } else {
                interval = Long.parseLong(expirationInterval);
            }
            String token = jwtTokenUtil.generateToken(request.getEmail(), claims, interval);

            SignInResponse signInResponse = new SignInResponse(consultant.getId(), consultant.getFirstName(),
                    consultant.getLastName(), consultant.getEmail(), role, token);
            return ResponseEntity.ok().body(signInResponse);
        } catch (OASException e) {
            System.out.println(e.getMessage());
            return oasUtil.buildResponseEntity(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (RuntimeException e) {
            return oasUtil.buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, Constants.SYSTEM_ERROR);
        }
    }

    @PostMapping("${consultant.signup.uri}")
    public ResponseEntity<?> consultantSignUp(@RequestBody ConsultantRequest request) {
        try {
            String email = request.getEmail();
            if (consultantRepository.existsByEmail(email)) {
                return oasUtil.buildResponseEntity(HttpStatus.BAD_REQUEST, Constants.EMAIL_EXISTS);
            }
            return consultantSignUpService.createConsultant(request);
        } catch (RuntimeException e) {
            logger.debug("[ROLLBACK] " + e.getMessage());
            return oasUtil.buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, Constants.SIGNUP_FAILED);
        }
    }

    public String refreshAuthenticationToken(String token, Long interval) {
        return jwtTokenUtil.refreshToken(token, interval);
    }
}
