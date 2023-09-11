package icbt.oas.backend.payload.request;

import lombok.Data;

@Data
public class TOTPRequest {
    private String email;
    private int code;
}
