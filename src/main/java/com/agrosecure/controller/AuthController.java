package com.agrosecure.controller;

import com.agrosecure.model.RefreshToken;
import com.agrosecure.payload.JwtResponse;
import com.agrosecure.payload.LoginRequest;
import com.agrosecure.security.jwt.JwtUtils;
import com.agrosecure.security.services.RefreshTokenService;
import com.agrosecure.security.services.UserDetailsImpl;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    RefreshTokenService refreshTokenService;

    @Autowired
    JwtUtils jwtUtils;

    @Value("${app.jwt.refresh-expiration-ms}")
    private Long refreshTokenDurationMs;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String jwt = jwtUtils.generateJwtToken(authentication);

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

        // Create HttpOnly cookie
        ResponseCookie jwtCookie = ResponseCookie.from("refresh_token", refreshToken.getToken())
                .httpOnly(true)
                .secure(false) // Set to true in production (HTTPS)
                .path("/auth/refresh-token")
                .maxAge(refreshTokenDurationMs / 1000)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, jwtCookie.toString());

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getEmail(),
                userDetails.getTenantId(),
                roles));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(HttpServletRequest request) {
        String refreshToken = null;
        
        // Get refresh token from cookie
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("refresh_token".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }

        if (refreshToken != null) {
            return refreshTokenService.findByToken(refreshToken)
                    .map(refreshTokenService::verifyExpiration)
                    .map(RefreshToken::getUser)
                    .map(user -> {
                        String token = jwtUtils.generateTokenFromUsername(user.getEmail());
                        return ResponseEntity.ok(new JwtResponse(token, 
                                user.getId(), 
                                user.getEmail(), 
                                user.getTenantId(), 
                                null)); // Roles not refreshed here for brevity
                    })
                    .orElseThrow(() -> new RuntimeException("Refresh token is not in database!"));
        }
        return ResponseEntity.badRequest().body("Refresh Token is empty!");
    }
}

