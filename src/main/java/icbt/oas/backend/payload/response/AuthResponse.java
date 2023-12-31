package icbt.oas.backend.payload.response;

import icbt.oas.backend.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String token;
    private Role role;

}
