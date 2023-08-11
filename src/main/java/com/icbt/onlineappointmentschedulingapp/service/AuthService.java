package com.icbt.onlineappointmentschedulingapp.service;

import com.icbt.onlineappointmentschedulingapp.model.Admin;
import com.icbt.onlineappointmentschedulingapp.model.Role;
import com.icbt.onlineappointmentschedulingapp.payload.request.AdminRegisterRequest;
import com.icbt.onlineappointmentschedulingapp.payload.request.AuthRequest;
import com.icbt.onlineappointmentschedulingapp.payload.response.AuthResponse;
import com.icbt.onlineappointmentschedulingapp.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    public AuthResponse register(AdminRegisterRequest request) {
        var user = Admin.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ADMIN)
                .build();
        adminRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthResponse authenticate(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = adminRepository.findByEmail(request.getEmail()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(request.getEmail())
                .token(jwtToken)
                .role(request.getRole())
                .build();
    }
}
