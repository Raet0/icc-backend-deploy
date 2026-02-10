package ec.edu.ups.icc.portafolio_backend.programmer.dto;

public record AvailabilityResponse(
    Long id,
    String day,
    String startTime,
    String endTime,
    String modality
) {}