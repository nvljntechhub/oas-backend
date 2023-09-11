package icbt.oas.backend.payload.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConfirmationRequest {
    private String password;
    private String token;
}
