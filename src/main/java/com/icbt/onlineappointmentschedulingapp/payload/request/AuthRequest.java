package com.icbt.onlineappointmentschedulingapp.payload.request;

import com.icbt.onlineappointmentschedulingapp.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequest {
    private String email;
    String password;
    private Role role;
}
