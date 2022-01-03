package com.programming.techie.rapiddeploy.controller;

import com.programming.techie.rapiddeploy.dto.AuthenticationResponse;
import com.programming.techie.rapiddeploy.dto.LoginRequest;
import com.programming.techie.rapiddeploy.dto.RegisterRequest;
import com.programming.techie.rapiddeploy.security.JwtProvider;
import com.programming.techie.rapiddeploy.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtProvider jwtProvider;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody RegisterRequest registerRequest) {
        authService.signup(registerRequest);
        return new ResponseEntity<>("User Registration Successful",
                OK);
    }

    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @PostMapping("/token")
    public String getToken(Authentication authentication) {
        return jwtProvider.generateToken(authentication);
    }
}
