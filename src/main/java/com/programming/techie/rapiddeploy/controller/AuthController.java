package com.programming.techie.rapiddeploy.controller;

import com.programming.techie.rapiddeploy.dto.AuthenticationResponse;
import com.programming.techie.rapiddeploy.dto.LoginRequest;
import com.programming.techie.rapiddeploy.security.JwtProvider;
import com.programming.techie.rapiddeploy.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtProvider jwtProvider;

    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody LoginRequest loginRequest, Authentication authentication) {
        AuthenticationResponse authenticationResponse = authService.login(loginRequest);
        authenticationResponse.setAuthenticationToken(jwtProvider.generateToken(authentication));
        return authenticationResponse;
    }

    @PostMapping("/token")
    public String getToken(Authentication authentication) {
        return jwtProvider.generateToken(authentication);
    }
}
