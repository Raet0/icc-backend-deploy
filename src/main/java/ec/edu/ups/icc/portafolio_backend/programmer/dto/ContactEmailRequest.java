package ec.edu.ups.icc.portafolio_backend.programmer.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ContactEmailRequest(
    @NotBlank String requesterName,
    @Email @NotBlank String requesterEmail,
    String message
) {}