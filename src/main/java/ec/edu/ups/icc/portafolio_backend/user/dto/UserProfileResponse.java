package ec.edu.ups.icc.portafolio_backend.user.dto;

import java.time.LocalDateTime;

public record UserProfileResponse(
    Long id,
    String name,
    String email,
    String role,
    String photoUrl,
    String headline,
    String bio,
    LocalDateTime createdAt
) {}