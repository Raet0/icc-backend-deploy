package ec.edu.ups.icc.portafolio_backend.admin.dto;

public record UserResponse(
    Long id,
    String name,
    String email,
    String role
) {}