package toka.tokagotchi.tokaarenabackend.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import toka.tokagotchi.tokaarenabackend.auth.dto.AuthRequest;
import toka.tokagotchi.tokaarenabackend.auth.dto.AuthResponse;
import toka.tokagotchi.tokaarenabackend.auth.dto.RegisterRequest;
import toka.tokagotchi.tokaarenabackend.auth.service.AuthService;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public AuthResponse register(@RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request) {
        return authService.login(request);
    }

    @GetMapping("/test")
    public String test() {
        return "OK";
    }
}