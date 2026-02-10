package ec.edu.ups.icc.portafolio_backend.programmer.dto;

public record AvailabilityRequest(
    String day,
    String startTime,
    String endTime,
    String modality
) {}