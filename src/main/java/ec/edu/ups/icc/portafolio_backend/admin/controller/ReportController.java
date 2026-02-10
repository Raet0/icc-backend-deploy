package ec.edu.ups.icc.portafolio_backend.admin.controller;

import ec.edu.ups.icc.portafolio_backend.programmer.dto.AdvisoryReportItem;
import ec.edu.ups.icc.portafolio_backend.programmer.dto.ProjectReportItem;
import ec.edu.ups.icc.portafolio_backend.programmer.entity.AdvisoryStatus;
import ec.edu.ups.icc.portafolio_backend.programmer.service.ReportService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/advisories")
    public List<AdvisoryReportItem> advisorySummary(
        @RequestParam(required = false) String from,
        @RequestParam(required = false) String to,
        @RequestParam(required = false) String status
    ) {
        return reportService.advisorySummary(parseDate(from), parseDate(to), parseStatus(status));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/projects")
    public List<ProjectReportItem> projectSummary() {
        return reportService.projectSummary();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/advisories/pdf")
    public ResponseEntity<byte[]> advisoryPdf(
        @RequestParam(required = false) String from,
        @RequestParam(required = false) String to,
        @RequestParam(required = false) String status
    ) {
        byte[] pdf = reportService.advisoryPdf(parseDate(from), parseDate(to), parseStatus(status));
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=advisories.pdf")
            .contentType(MediaType.APPLICATION_PDF)
            .body(pdf);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/projects/excel")
    public ResponseEntity<byte[]> projectExcel() {
        byte[] excel = reportService.projectExcel();
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=projects.xlsx")
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(excel);
    }

    private LocalDate parseDate(String raw) {
        if (raw == null || raw.isBlank()) return null;
        return LocalDate.parse(raw);
    }

    private AdvisoryStatus parseStatus(String raw) {
        if (raw == null || raw.isBlank()) return null;
        return AdvisoryStatus.valueOf(raw);
    }
}