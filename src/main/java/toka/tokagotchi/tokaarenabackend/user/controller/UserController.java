package toka.tokagotchi.tokaarenabackend.user.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import toka.tokagotchi.tokaarenabackend.user.dto.UserProfileResponse;
import toka.tokagotchi.tokaarenabackend.user.service.UserService;

@RestController
@RequestMapping("/api/v1/user")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    public UserProfileResponse getProfile() {
        return userService.getProfile();
    }
}