package com.icbt.onlineappointmentschedulingapp.controller;

import com.icbt.onlineappointmentschedulingapp.payload.request.AuthRequest;
import com.icbt.onlineappointmentschedulingapp.payload.response.AuthResponse;
import com.icbt.onlineappointmentschedulingapp.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
@RestController
@RequestMapping("${auth.api.uri}")
@RequiredArgsConstructor
//@CrossOrigin(origins = "*")
public class AdminAuthController {
    private final AuthService authService;

//    @PostMapping("/register")
//    public ResponseEntity<AuthResponse> register(
//            @RequestBody AdminRegisterRequest request
//    ){
//        return ResponseEntity.ok(authService.register(request));
//    }

    @PostMapping("${signIn.api.uri}")
    public ResponseEntity<AuthResponse> authenticate(
            @RequestBody AuthRequest request
    ){
        return ResponseEntity.ok(authService.authenticate(request));
    }
}
