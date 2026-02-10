package ec.edu.ups.icc.portafolio_backend.programmer.dto;

public record AdvisoryReportItem(
    String programmerName,
    String status,
    long total
) {}