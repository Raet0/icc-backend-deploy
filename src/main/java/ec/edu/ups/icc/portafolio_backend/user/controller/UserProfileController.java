package ec.edu.ups.icc.portafolio_backend.user.controller;

import ec.edu.ups.icc.portafolio_backend.user.dto.UpdateUserProfileRequest;
import ec.edu.ups.icc.portafolio_backend.user.dto.UserProfileResponse;
import ec.edu.ups.icc.portafolio_backend.user.service.UserProfileService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserProfileController {

    private final UserProfileService userProfileService;

    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @GetMapping("/me")
    public UserProfileResponse me(Authentication auth) {
        return userProfileService.getByEmail(auth.getName());
    }

    @PutMapping("/me")
    public UserProfileResponse updateMe(Authentication auth, @RequestBody UpdateUserProfileRequest request) {
        return userProfileService.update(auth.getName(), request);
    }
}