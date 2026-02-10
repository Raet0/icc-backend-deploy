package ec.edu.ups.icc.portafolio_backend.user.service;

import ec.edu.ups.icc.portafolio_backend.user.dto.UpdateUserProfileRequest;
import ec.edu.ups.icc.portafolio_backend.user.dto.UserProfileResponse;
import ec.edu.ups.icc.portafolio_backend.user.entity.User;
import ec.edu.ups.icc.portafolio_backend.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserProfileService {

    private final UserRepository userRepository;

    public UserProfileService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserProfileResponse getByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow();
        return toResponse(user);
    }

    public UserProfileResponse update(String email, UpdateUserProfileRequest request) {
        User user = userRepository.findByEmail(email).orElseThrow();

        if (request.name() != null) user.setName(request.name());
        if (request.photoUrl() != null) user.setPhotoUrl(request.photoUrl());
        if (request.headline() != null) user.setHeadline(request.headline());
        if (request.bio() != null) user.setBio(request.bio());

        userRepository.save(user);
        return toResponse(user);
    }

    private UserProfileResponse toResponse(User user) {
        return new UserProfileResponse(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getRole().name(),
            user.getPhotoUrl(),
            user.getHeadline(),
            user.getBio(),
            user.getCreatedAt()
        );
    }
}