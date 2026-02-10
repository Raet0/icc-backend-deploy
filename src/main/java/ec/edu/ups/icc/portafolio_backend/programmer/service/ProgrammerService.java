package ec.edu.ups.icc.portafolio_backend.programmer.service;

import ec.edu.ups.icc.portafolio_backend.programmer.dto.ProgrammerProfileRequest;
import ec.edu.ups.icc.portafolio_backend.programmer.dto.ProgrammerProfileResponse;
import ec.edu.ups.icc.portafolio_backend.programmer.dto.SocialLinkDto;
import ec.edu.ups.icc.portafolio_backend.programmer.entity.ProgrammerProfile;
import ec.edu.ups.icc.portafolio_backend.programmer.entity.SocialLink;
import ec.edu.ups.icc.portafolio_backend.programmer.repository.ProgrammerProfileRepository;
import ec.edu.ups.icc.portafolio_backend.user.repository.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProgrammerService {

    private final ProgrammerProfileRepository profileRepository;
    private final UserRepository userRepository;

    public ProgrammerService(ProgrammerProfileRepository profileRepository, UserRepository userRepository) {
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
    }

    public ProgrammerProfileResponse createOrUpdateProfile(Long userId, ProgrammerProfileRequest request) {
        var user = userRepository.findById(userId).orElseThrow();
        var profile = profileRepository.findByUserId(userId).orElseGet(() -> {
            ProgrammerProfile p = new ProgrammerProfile();
            p.setUser(user);
            return p;
        });

        profile.setUser(user);
        profile.setSpecialty(request.specialty());
        profile.setDescription(request.description());
        profile.setSkills(request.skills());
        profile.setPhotoUrl(request.photoUrl());

        if (request.socials() != null) {
            profile.setSocials(
                request.socials().stream()
                    .map(dto -> new SocialLink(dto.name(), dto.url()))
                    .collect(Collectors.toList())
            );
        }

        profileRepository.save(profile);
        return toResponse(profile);
    }

    public ProgrammerProfileResponse getByUserId(Long userId) {
        var user = userRepository.findById(userId).orElseThrow();
        var profile = profileRepository.findByUserId(userId).orElseGet(() -> {
            ProgrammerProfile p = new ProgrammerProfile();
            p.setUser(user);
            p.setSpecialty("");
            p.setDescription("");
            p.setPhotoUrl("");
            return profileRepository.save(p);
        });
        return toResponse(profile);
    }

    public List<ProgrammerProfileResponse> listAll(Integer page, Integer size) {
        List<ProgrammerProfile> list;
        if (page == null || size == null) {
            list = profileRepository.findAll();
        } else {
            list = profileRepository.findAll(PageRequest.of(page, size)).getContent();
        }
        return list.stream().map(this::toResponse).toList();
    }

    public ProgrammerProfileResponse getByProfileId(Long profileId) {
        var profile = profileRepository.findById(profileId).orElseThrow();
        return toResponse(profile);
    }

    private ProgrammerProfileResponse toResponse(ProgrammerProfile p) {
        return new ProgrammerProfileResponse(
                p.getId(),
                p.getUser().getName(),
                p.getUser().getEmail(),
                p.getSpecialty(),
                p.getDescription(),
                p.getSkills(),
                p.getPhotoUrl(),
                p.getSocials().stream()
                    .map(s -> new SocialLinkDto(s.getName(), s.getUrl()))
                    .collect(Collectors.toList())
        );
    }
}