package ec.edu.ups.icc.portafolio_backend.programmer.dto;

import java.util.List;

public record ProjectRequest(
    String name,
    String description,
    String participation,
    List<String> technologies,
    String repoUrl,
    String demoUrl,
    String imageUrl,
    String section,
    boolean active
) {}