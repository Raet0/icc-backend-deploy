package ec.edu.ups.icc.portafolio_backend.programmer.dto;

public record AdvisoryRequest(
    Long programmerProfileId,
    String requesterName,
    String requesterEmail,
    String scheduledAt,
    String comment
) {}