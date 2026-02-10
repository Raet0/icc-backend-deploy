package ec.edu.ups.icc.portafolio_backend.programmer.dto;

public record AdvisoryResponse(
    Long id,
    Long programmerProfileId,
    String programmerName,
    String requesterName,
    String requesterEmail,
    String scheduledAt,
    String comment,
    String status,
    String response
) {}