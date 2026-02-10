package ec.edu.ups.icc.portafolio_backend.user.dto;

public record UpdateUserProfileRequest(
    String name,
    String photoUrl,
    String headline,
    String bio
) {}