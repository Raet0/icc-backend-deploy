package ec.edu.ups.icc.portafolio_backend.programmer.controller;

import ec.edu.ups.icc.portafolio_backend.programmer.dto.AdvisoryRequest;
import ec.edu.ups.icc.portafolio_backend.programmer.dto.AdvisoryResponse;
import ec.edu.ups.icc.portafolio_backend.programmer.dto.ProgrammerProfileResponse;
import ec.edu.ups.icc.portafolio_backend.programmer.service.AdvisoryService;
import ec.edu.ups.icc.portafolio_backend.programmer.service.ProgrammerService;
import org.springframework.web.bind.annotation.*;
import ec.edu.ups.icc.portafolio_backend.programmer.dto.ProjectResponse;
import ec.edu.ups.icc.portafolio_backend.programmer.dto.AvailabilityResponse;
import ec.edu.ups.icc.portafolio_backend.programmer.service.ProjectService;
import ec.edu.ups.icc.portafolio_backend.programmer.service.AvailabilityService;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PublicController {
    private final ProgrammerService programmerService;
    private final AdvisoryService advisoryService;
    private final ProjectService projectService;
    private final AvailabilityService availabilityService;

    @GetMapping("/programmers")
    public List<ProgrammerProfileResponse> listProgrammers(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "50") int size
    ) {
        return programmerService.listAll(page, size);
    }

    @GetMapping("/programmers/{id}")
    public ProgrammerProfileResponse getProgrammer(@PathVariable Long id) {
        return programmerService.getByProfileId(id);
    }

    @PostMapping("/advisories")
    public AdvisoryResponse createAdvisory(@RequestBody AdvisoryRequest request) {
        return advisoryService.create(request);
    }

    @GetMapping("/advisories")
    public List<AdvisoryResponse> listByRequester(
        @RequestParam String email,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "50") int size
    ) {
        return advisoryService.listByRequester(email, page, size);
    }

    @GetMapping("/programmers/{id}/projects")
    public List<ProjectResponse> listProjects(
        @PathVariable Long id,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "50") int size
    ) {
        return projectService.listByProfile(id, page, size);
    }

    @GetMapping("/programmers/{id}/availability")
    public List<AvailabilityResponse> listAvailability(
        @PathVariable Long id,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "50") int size
    ) {
        return availabilityService.list(id, page, size);
    }
}