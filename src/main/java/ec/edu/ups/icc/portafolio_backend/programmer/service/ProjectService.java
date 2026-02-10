package ec.edu.ups.icc.portafolio_backend.programmer.service;

import ec.edu.ups.icc.portafolio_backend.programmer.dto.ProjectRequest;
import ec.edu.ups.icc.portafolio_backend.programmer.dto.ProjectResponse;
import ec.edu.ups.icc.portafolio_backend.programmer.entity.Participation;
import ec.edu.ups.icc.portafolio_backend.programmer.entity.Project;
import ec.edu.ups.icc.portafolio_backend.programmer.entity.ProjectSection;
import ec.edu.ups.icc.portafolio_backend.programmer.repository.ProgrammerProfileRepository;
import ec.edu.ups.icc.portafolio_backend.programmer.repository.ProjectRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProgrammerProfileRepository profileRepository;

    public ProjectService(ProjectRepository projectRepository, ProgrammerProfileRepository profileRepository) {
        this.projectRepository = projectRepository;
        this.profileRepository = profileRepository;
    }

    public ProjectResponse create(Long profileId, ProjectRequest request) {
        validate(request);
        var profile = profileRepository.findById(profileId).orElseThrow();
        Project p = new Project();
        p.setProfile(profile);
        apply(p, request);
        projectRepository.save(p);
        return toResponse(p);
    }

    public ProjectResponse update(Long projectId, ProjectRequest request) {
        validate(request);
        var p = projectRepository.findById(projectId).orElseThrow();
        apply(p, request);
        projectRepository.save(p);
        return toResponse(p);
    }

    public void delete(Long projectId) {
        projectRepository.deleteById(projectId);
    }

    public List<ProjectResponse> listByProfile(Long profileId, Integer page, Integer size) {
        if (page == null || size == null) {
            return projectRepository.findByProfileId(profileId).stream().map(this::toResponse).toList();
        }
        return projectRepository.findByProfileId(profileId, PageRequest.of(page, size))
            .getContent().stream().map(this::toResponse).toList();
    }

    private void apply(Project p, ProjectRequest r) {
        p.setName(r.name());
        p.setDescription(r.description());
        p.setParticipation(Participation.valueOf(r.participation()));
        p.setTechnologies(r.technologies());
        p.setRepoUrl(r.repoUrl());
        p.setDemoUrl(r.demoUrl());
        p.setImageUrl(r.imageUrl());
        p.setSection(ProjectSection.valueOf(r.section()));
        p.setActive(r.active());
    }

    private ProjectResponse toResponse(Project p) {
        return new ProjectResponse(
            p.getId(), p.getName(), p.getDescription(),
            p.getParticipation().name(), p.getTechnologies(),
            p.getRepoUrl(), p.getDemoUrl(), p.getImageUrl(),
            p.getSection().name(), p.isActive()
        );
    }

    private void validate(ProjectRequest r) {
        if (r == null) throw new RuntimeException("Solicitud inválida");
        if (isBlank(r.name())) throw new RuntimeException("Nombre requerido");
        if (isBlank(r.description())) throw new RuntimeException("Descripción requerida");
        if (r.technologies() == null || r.technologies().isEmpty()) {
            throw new RuntimeException("Tecnologías requeridas");
        }
        if (isBlank(r.participation())) throw new RuntimeException("Participación requerida");
        if (isBlank(r.section())) throw new RuntimeException("Sección requerida");
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}