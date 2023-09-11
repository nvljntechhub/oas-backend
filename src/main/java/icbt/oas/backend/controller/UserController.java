package icbt.oas.backend.controller;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseStatus;

import icbt.oas.backend.model.User;
import icbt.oas.backend.model.UserRole;
import icbt.oas.backend.payload.request.UserRequest;
import icbt.oas.backend.payload.response.SignInResponse;
import icbt.oas.backend.repository.UserRepository;
import icbt.oas.backend.util.OASUtil;
import icbt.oas.backend.util.Constants;
import icbt.oas.backend.util.JwtTokenUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("${base.uri}")
public class UserController {
    @Value("${jwt.token.expiration.in.seconds}")
    private Long expiration;
    @Value("${jwt.http.request.header}")
    private String tokenHeader;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private OASUtil cpdUtil;

    private static List<String> ALLOWED_ROLES = Arrays.asList(UserRole.ADMIN.name());

    /**
     * Get all users
     */
    @GetMapping("${users.uri}")
    public ResponseEntity<?> getAll(HttpServletRequest request) {
        try {
            Claims claims = cpdUtil.checkAndReturnClaims(request, ALLOWED_ROLES);
            if (claims == null) {
                return cpdUtil.buildResponseEntity(HttpStatus.FORBIDDEN, Constants.PERMISSION_DENIED);
            }
            String companyIdClaim = claims.get(Constants.JWT_CLAIM_COMPANY_ID).toString();
            return ResponseEntity.ok(userRepository.findUsersByCompanyId(companyIdClaim));
        } catch (RuntimeException e) {
            return cpdUtil.buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, Constants.SYSTEM_ERROR);
        }
    }

    /**
     * Get user by ID
     */
    @GetMapping("${user.uri}")
    public ResponseEntity<?> get(HttpServletRequest request, @PathVariable Long id) {
        try {
            Claims claims = cpdUtil.checkAndReturnClaims(request, ALLOWED_ROLES);
            if (claims == null) {
                return cpdUtil.buildResponseEntity(HttpStatus.FORBIDDEN, Constants.PERMISSION_DENIED);
            }
            String companyId = claims.get(Constants.JWT_CLAIM_COMPANY_ID).toString();
            User user = findUser(id, companyId);
            if (user == null) {
                return cpdUtil.buildResponseEntity(HttpStatus.NOT_FOUND, Constants.INVALID_USER_ID);
            }
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return cpdUtil.buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, Constants.SYSTEM_ERROR);
        }
    }

    /**
     * Create user
     */
    @PostMapping("${users.uri}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> create(HttpServletRequest request, @RequestBody UserRequest userDetails) {
        try {
            Claims claims = cpdUtil.checkAndReturnClaims(request, ALLOWED_ROLES);
            if (claims == null) {
                return cpdUtil.buildResponseEntity(HttpStatus.FORBIDDEN, Constants.PERMISSION_DENIED);
            }
            if (userRepository.existsByEmail(userDetails.getEmail())) {
                return cpdUtil.buildResponseEntity(HttpStatus.BAD_REQUEST, Constants.EMAIL_EXISTS);
            }
            String secret = new GoogleAuthenticator().createCredentials().getKey();
            User newUser = new User( userDetails.getFirstName(), userDetails.getLastName(),
                    userDetails.getEmail(), userDetails.getPassword(), userDetails.getRole());
            return ResponseEntity.ok(userRepository.save(newUser));
        } catch (RuntimeException e) {
            return cpdUtil.buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, Constants.SYSTEM_ERROR);
        }
    }

    /**
     * Update user
     */
    @PutMapping("${user.uri}")
    public ResponseEntity<?> update(HttpServletRequest request, @PathVariable Long id,
                                    @RequestBody UserRequest userDetails) {
        try {
            Claims claims = cpdUtil.checkAndReturnClaims(request, ALLOWED_ROLES);
            if (claims == null) {
                return cpdUtil.buildResponseEntity(HttpStatus.FORBIDDEN, Constants.PERMISSION_DENIED);
            }
            String companyId = claims.get(Constants.JWT_CLAIM_COMPANY_ID).toString();
            User user = findUser(id, companyId);
            if (user == null) {
                return cpdUtil.buildResponseEntity(HttpStatus.NOT_FOUND, Constants.INVALID_USER_ID);
            }
            String firstName = userDetails.getFirstName();
            String lastName = userDetails.getLastName();
            String email = userDetails.getEmail();
            String password = userDetails.getPassword();
            String role = userDetails.getRole();

            if (firstName != null) {
                user.setFirstName(firstName);
            }
            if (lastName != null) {
                user.setLastName(lastName);
            }
            if (email != null) {
                user.setEmail(email);
            }
            if (password != null) {
                user.setPassword(password);
            }

            if (role != null) {
                user.setRole(role);
            }
            return ResponseEntity.ok(userRepository.save(user));
        } catch (RuntimeException e) {
            return cpdUtil.buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, Constants.SYSTEM_ERROR);
        }
    }


    /**
     * Reset my password
     */
    @PutMapping("${reset.password.uri}")
    public ResponseEntity<?> changeMyPassword(HttpServletRequest request, @PathVariable Long id,
                                              @RequestBody UserRequest userDetails) {
        try {
            Claims claims = cpdUtil.checkAndReturnClaims(request, ALLOWED_ROLES);
            if (claims == null) {
                return cpdUtil.buildResponseEntity(HttpStatus.FORBIDDEN, Constants.PERMISSION_DENIED);
            }
            String companyId = claims.get(Constants.JWT_CLAIM_COMPANY_ID).toString();
            User user = findUser(id, companyId);
            if (user == null) {
                return cpdUtil.buildResponseEntity(HttpStatus.NOT_FOUND, Constants.INVALID_USER_ID);
            }
            String password = userDetails.getPassword();
            if (password != null) {
                user.setPassword(password);
            }
            User updatedUser = userRepository.save(user);
            Long interval;
            String expirationInterval = System.getenv(Constants.JWT_TOKEN_EXPIRATION);
            if (expirationInterval == null) {
                interval = expiration;
            } else {
                interval = Long.parseLong(expirationInterval);
            }
            String token = jwtTokenUtil.generateToken(user.getEmail(), claims, interval);
            SignInResponse signInResponse = new SignInResponse(updatedUser.getId(),
                    updatedUser.getFirstName(), updatedUser.getLastName(), updatedUser.getEmail(),
                    updatedUser.getRole(), token);
            return ResponseEntity.ok(signInResponse);
        } catch (RuntimeException e) {
            return cpdUtil.buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, Constants.SYSTEM_ERROR);
        }
    }

    /**
     * Delete user by ID
     */
    @DeleteMapping("${user.uri}")
    public ResponseEntity<?> delete(HttpServletRequest request, @PathVariable Long id) {
        try {
            Claims claims = cpdUtil.checkAndReturnClaims(request, ALLOWED_ROLES);
            if (claims == null) {
                return cpdUtil.buildResponseEntity(HttpStatus.FORBIDDEN, Constants.PERMISSION_DENIED);
            }
            String companyId = claims.get(Constants.JWT_CLAIM_COMPANY_ID).toString();
            User user = findUser(id, companyId);
            if (user == null) {
                return cpdUtil.buildResponseEntity(HttpStatus.NOT_FOUND, Constants.INVALID_USER_ID);
            }
            userRepository.delete(user);
            return cpdUtil.buildResponseEntity(HttpStatus.OK, Constants.STATUS_SUCCESS);
        } catch (RuntimeException e) {
            return cpdUtil.buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, Constants.SYSTEM_ERROR);
        }
    }

    public User findUser(Long id, String companyId) {
        return userRepository.findById(id, companyId)
                .orElse(null);
    }
}
