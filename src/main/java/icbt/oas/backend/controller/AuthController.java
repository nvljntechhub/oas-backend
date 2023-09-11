package icbt.oas.backend.controller;

import icbt.oas.backend.exception.OASException;
import icbt.oas.backend.model.User;
import icbt.oas.backend.payload.request.AuthRequest;
import icbt.oas.backend.payload.response.SignInResponse;
import icbt.oas.backend.repository.ConsultantRepository;
import icbt.oas.backend.repository.UserRepository;
import icbt.oas.backend.service.ConsultantSignUpService;
import icbt.oas.backend.service.SignupService;
import icbt.oas.backend.service.UserDetailsImpl;
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
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("${auth.api.uri}")
public class AuthController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Value("${jwt.http.request.header}")
    private String tokenHeader;
    @Value("${jwt.token.expiration.in.seconds}")
    private Long expiration;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ConsultantRepository consultantRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private OASUtil oasUtil;
    @Autowired
    private SignupService signupService;
    @Autowired
    private ConsultantSignUpService consultantSignUpService;

    @PostMapping("${signIn.uri}")
    public ResponseEntity<?> signIn(@RequestBody AuthRequest request) {
        try {
            String email = request.getEmail();
            User user = userRepository.findByEmail(email).orElse(null);
            if (user == null) {
                return oasUtil.buildResponseEntity(HttpStatus.NOT_FOUND, Constants.INVALID_EMAIL);
            }

            UserDetailsImpl userDetails = authenticate(request);
            String role = user.getRole();
            Map<String, Object> claims = new HashMap<>();
            claims.put(Constants.JWT_CLAIM_ROLE, role);
            Long interval;
            String expirationInterval = System.getenv(Constants.JWT_TOKEN_EXPIRATION);
            if (expirationInterval == null) {
                interval = expiration;
            } else {
                interval = Long.parseLong(expirationInterval);
            }
            String token = jwtTokenUtil.generateToken(userDetails.getEmail(), claims, interval);

            SignInResponse signInResponse = new SignInResponse(user.getId(), user.getFirstName(),
                    user.getLastName(), user.getEmail(), role, token);
            return ResponseEntity.ok().body(signInResponse);
        } catch (OASException e) {
            return oasUtil.buildResponseEntity(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (RuntimeException e) {
            return oasUtil.buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, Constants.SYSTEM_ERROR);
        }
    }

    public UserDetailsImpl authenticate(AuthRequest request) throws OASException {
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(),
                            request.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return (UserDetailsImpl) authentication.getPrincipal();
        } catch (DisabledException e) {
            throw new OASException(Constants.ACCESS_DENIED, e);
        } catch (BadCredentialsException e) {
            throw new OASException(Constants.INVALID_CREDENTIALS, e);
        }
    }
}
