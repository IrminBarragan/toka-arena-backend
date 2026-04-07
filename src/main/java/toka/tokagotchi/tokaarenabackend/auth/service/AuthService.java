package toka.tokagotchi.tokaarenabackend.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import toka.tokagotchi.tokaarenabackend.auth.dto.AuthRequest;
import toka.tokagotchi.tokaarenabackend.auth.dto.AuthResponse;
import toka.tokagotchi.tokaarenabackend.auth.dto.RegisterRequest;
import toka.tokagotchi.tokaarenabackend.config.security.JwtService;
import toka.tokagotchi.tokaarenabackend.user.model.User;
import toka.tokagotchi.tokaarenabackend.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthResponse register(RegisterRequest request) {

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .essences(0)
                .enabled(true)
                .firstToka(false)
                .build();

        userRepository.save(user);

        String token = jwtService.generateToken(user.getUsername());


        return new AuthResponse(token, false);
    }

    public AuthResponse login(AuthRequest request) {

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtService.generateToken(user.getUsername());

        return new AuthResponse(token, user.isFirstToka());
    }
}