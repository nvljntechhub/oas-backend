package icbt.oas.backend.seeder;

import icbt.oas.backend.model.Role;
import icbt.oas.backend.model.UserRole;
import icbt.oas.backend.repository.RoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class DatabaseSeeder {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private RoleRepository roleRepository;
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public DatabaseSeeder(RoleRepository roleRepository, JdbcTemplate jdbcTemplate) {
        this.roleRepository = roleRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @EventListener
    public void seed(ContextRefreshedEvent event) {
        seedRoleTable();
    }
    private void seedRoleTable() {
        List<UserRole> userRoles = Arrays.asList(UserRole.values());
        List<String> stringRoles = new ArrayList<>();
        for (UserRole userRole : userRoles) {
            stringRoles.add(userRole.name());
        }
        String inSql = String.join(",", Collections.nCopies(stringRoles.size(), "?"));
        List roleList = jdbcTemplate.query(
                String.format("SELECT r.name FROM roles r WHERE r.name IN (%s)", inSql),
                (rs, rowNum) -> rs.getString("name"),
                stringRoles.toArray());
        for (UserRole userRole : userRoles) {
            if (!roleList.contains(userRole.name())) {
                Role role = new Role(userRole);
                roleRepository.save(role);
                logger.debug("Roles table is seeded with " + userRole.name());
            }
        }
    }
}
