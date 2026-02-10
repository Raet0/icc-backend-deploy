package ec.edu.ups.icc.portafolio_backend.admin.dto;

public record UpdateUserRequest(
    String name,
    String email,
    String role
) {}