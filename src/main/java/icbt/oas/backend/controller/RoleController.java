package icbt.oas.backend.controller;

import icbt.oas.backend.model.Role;
import icbt.oas.backend.repository.RoleRepository;
import icbt.oas.backend.util.Constants;
import icbt.oas.backend.util.OASUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("${base.uri}")
public class RoleController {
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private OASUtil OASUtil;

    /**
     * Get all roles
     */
    @GetMapping("${roles.uri}")
    public List<Role> getRoles() {
        return roleRepository.findAll();
    }

    /**
     * Create role
     */
    @PostMapping("${roles.uri}")
    public ResponseEntity<?> create(@RequestBody Role role) {
        try {
            if (roleRepository.existsByName(role.getName().name())) {
                return OASUtil.buildResponseEntity(HttpStatus.BAD_REQUEST, Constants.ROLE_EXISTS);
            }
            return ResponseEntity.ok(roleRepository.save(role));
        } catch (RuntimeException e) {
            return OASUtil.buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, Constants.SYSTEM_ERROR);
        }
    }
}
