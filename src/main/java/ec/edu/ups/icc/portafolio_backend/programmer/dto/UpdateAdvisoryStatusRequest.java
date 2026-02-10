package ec.edu.ups.icc.portafolio_backend.programmer.dto;

public record UpdateAdvisoryStatusRequest(
    String status,
    String response
) {}