package ec.edu.ups.icc.portafolio_backend.programmer.controller;

import ec.edu.ups.icc.portafolio_backend.programmer.dto.*;
import ec.edu.ups.icc.portafolio_backend.programmer.service.*;
import ec.edu.ups.icc.portafolio_backend.user.repository.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/programmer")
public class ProgrammerController {

    private final ProgrammerService programmerService;
    private final ProjectService projectService;
    private final AvailabilityService availabilityService;
    private final AdvisoryService advisoryService;
    private final UserRepository userRepository;

    public ProgrammerController(ProgrammerService programmerService, ProjectService projectService,
                                AvailabilityService availabilityService, AdvisoryService advisoryService,
                                UserRepository userRepository) {
        this.programmerService = programmerService;
        this.projectService = projectService;
        this.availabilityService = availabilityService;
        this.advisoryService = advisoryService;
        this.userRepository = userRepository;
    }

    private Long currentUserId(Authentication auth) {
        return userRepository.findByEmail(auth.getName()).orElseThrow().getId();
    }

    @PreAuthorize("hasRole('PROGRAMMER')")
    @GetMapping("/me")
    public ProgrammerProfileResponse getProfile(Authentication auth) {
        return programmerService.getByUserId(currentUserId(auth));
    }

    @PreAuthorize("hasRole('PROGRAMMER')")
    @PutMapping("/profile")
    public ProgrammerProfileResponse updateProfile(Authentication auth, @RequestBody ProgrammerProfileRequest request) {
        return programmerService.createOrUpdateProfile(currentUserId(auth), request);
    }

    @PreAuthorize("hasRole('PROGRAMMER')")
    @PostMapping("/projects")
    public ProjectResponse createProject(Authentication auth, @RequestBody ProjectRequest request) {
        Long profileId = programmerService.getByUserId(currentUserId(auth)).id();
        return projectService.create(profileId, request);
    }

    @PreAuthorize("hasRole('PROGRAMMER')")
    @GetMapping("/projects")
    public List<ProjectResponse> listProjects(
        Authentication auth,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "50") int size
    ) {
        Long profileId = programmerService.getByUserId(currentUserId(auth)).id();
        return projectService.listByProfile(profileId, page, size);
    }

    @PreAuthorize("hasRole('PROGRAMMER')")
    @PutMapping("/projects/{id}")
    public ProjectResponse updateProject(@PathVariable Long id, @RequestBody ProjectRequest request) {
        return projectService.update(id, request);
    }

    @PreAuthorize("hasRole('PROGRAMMER')")
    @DeleteMapping("/projects/{id}")
    public void deleteProject(@PathVariable Long id) {
        projectService.delete(id);
    }

    @PreAuthorize("hasRole('PROGRAMMER')")
    @PostMapping("/availability")
    public AvailabilityResponse addAvailability(Authentication auth, @RequestBody AvailabilityRequest r) {
        Long profileId = programmerService.getByUserId(currentUserId(auth)).id();
        return availabilityService.add(profileId, r);
    }

    @PreAuthorize("hasRole('PROGRAMMER')")
    @GetMapping("/availability")
    public List<AvailabilityResponse> listAvailability(
        Authentication auth,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "50") int size
    ) {
        Long profileId = programmerService.getByUserId(currentUserId(auth)).id();
        return availabilityService.list(profileId, page, size);
    }

    @PreAuthorize("hasRole('PROGRAMMER')")
    @DeleteMapping("/availability/{id}")
    public void deleteAvailability(@PathVariable Long id) {
        availabilityService.delete(id);
    }

    @PreAuthorize("hasRole('PROGRAMMER')")
    @GetMapping("/advisories")
    public List<AdvisoryResponse> listAdvisories(
        Authentication auth,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "50") int size
    ) {
        Long profileId = programmerService.getByUserId(currentUserId(auth)).id();
        return advisoryService.listByProfile(profileId, page, size);
    }

    @PreAuthorize("hasRole('PROGRAMMER')")
    @PatchMapping("/advisories/{id}")
    public AdvisoryResponse updateAdvisory(@PathVariable Long id, @RequestBody UpdateAdvisoryStatusRequest request) {
        return advisoryService.updateStatus(id, request);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/advisories/requester")
    public List<AdvisoryResponse> listAdvisoriesByRequester(
        Authentication auth,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "50") int size
    ) {
        return advisoryService.listByRequester(auth.getName(), page, size);
    }
}