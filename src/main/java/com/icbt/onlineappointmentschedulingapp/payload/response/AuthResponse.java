package com.icbt.onlineappointmentschedulingapp.payload.response;

import com.icbt.onlineappointmentschedulingapp.model.Role;
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
