package ec.edu.ups.icc.portafolio_backend.user.dto;

public record AuthResponse(
    String token,
    String name,
    String role
) {}