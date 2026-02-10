package ec.edu.ups.icc.portafolio_backend.programmer.dto;

import java.util.List;

public record ProgrammerProfileRequest(
        String specialty,
        String description,
        List<String> skills,
        String photoUrl,
        List<SocialLinkDto> socials
) {}