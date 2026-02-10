package ec.edu.ups.icc.portafolio_backend.programmer.service;

import ec.edu.ups.icc.portafolio_backend.programmer.dto.AdvisoryRequest;
import ec.edu.ups.icc.portafolio_backend.programmer.dto.AdvisoryResponse;
import ec.edu.ups.icc.portafolio_backend.programmer.dto.UpdateAdvisoryStatusRequest;
import ec.edu.ups.icc.portafolio_backend.programmer.entity.Advisory;
import ec.edu.ups.icc.portafolio_backend.programmer.entity.AdvisoryStatus;
import ec.edu.ups.icc.portafolio_backend.programmer.repository.AdvisoryRepository;
import ec.edu.ups.icc.portafolio_backend.programmer.repository.AvailabilityRepository;
import ec.edu.ups.icc.portafolio_backend.programmer.repository.ProgrammerProfileRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class AdvisoryService {

    private final AdvisoryRepository advisoryRepository;
    private final ProgrammerProfileRepository profileRepository;
    private final AvailabilityRepository availabilityRepository;
    private final NotificationService notificationService;

    public AdvisoryService(AdvisoryRepository advisoryRepository,
                           ProgrammerProfileRepository profileRepository,
                           AvailabilityRepository availabilityRepository,
                           NotificationService notificationService) {
        this.advisoryRepository = advisoryRepository;
        this.profileRepository = profileRepository;
        this.availabilityRepository = availabilityRepository;
        this.notificationService = notificationService;
    }

    public AdvisoryResponse create(AdvisoryRequest request) {
        if (request == null) throw new RuntimeException("Solicitud inválida");
        if (request.programmerProfileId() == null) throw new RuntimeException("Programador requerido");
        if (isBlank(request.requesterName())) throw new RuntimeException("Nombre requerido");
        if (isBlank(request.requesterEmail())) throw new RuntimeException("Email requerido");
        if (isBlank(request.scheduledAt())) throw new RuntimeException("Fecha requerida");

        LocalDateTime scheduledAt = parseDateTime(request.scheduledAt());
        if (scheduledAt.isBefore(LocalDateTime.now().plusMinutes(5))) {
            throw new RuntimeException("La fecha debe ser futura");
        }

        var profile = profileRepository.findById(request.programmerProfileId()).orElseThrow();
        ensureAvailability(profile.getId(), scheduledAt);

        Advisory advisory = new Advisory();
        advisory.setProfile(profile);
        advisory.setRequesterName(request.requesterName());
        advisory.setRequesterEmail(request.requesterEmail());
        advisory.setScheduledAt(scheduledAt);
        advisory.setComment(request.comment());
        advisoryRepository.save(advisory);

        notificationService.notifyAdvisoryCreated(advisory);

        return toResponse(advisory);
    }

    public List<AdvisoryResponse> listByProfile(Long profileId, Integer page, Integer size) {
        if (page == null || size == null) {
            return advisoryRepository.findByProfileId(profileId).stream().map(this::toResponse).toList();
        }
        return advisoryRepository.findByProfileId(profileId, PageRequest.of(page, size))
            .getContent().stream().map(this::toResponse).toList();
    }

    public List<AdvisoryResponse> listByRequester(String email, Integer page, Integer size) {
        if (page == null || size == null) {
            return advisoryRepository.findByRequesterEmail(email).stream().map(this::toResponse).toList();
        }
        return advisoryRepository.findByRequesterEmail(email, PageRequest.of(page, size))
            .getContent().stream().map(this::toResponse).toList();
    }

    public AdvisoryResponse updateStatus(Long id, UpdateAdvisoryStatusRequest request) {
        if (request == null || isBlank(request.status())) {
            throw new RuntimeException("Estado requerido");
        }

        Advisory advisory = advisoryRepository.findById(id).orElseThrow();

        if (advisory.getStatus() != AdvisoryStatus.PENDIENTE) {
            throw new RuntimeException("La asesoría ya fue procesada");
        }

        AdvisoryStatus nextStatus = parseStatus(request.status());
        advisory.setStatus(nextStatus);
        advisory.setResponse(request.response());
        advisoryRepository.save(advisory);

        notificationService.notifyAdvisoryUpdated(advisory);

        return toResponse(advisory);
    }

    private AdvisoryResponse toResponse(Advisory a) {
        return new AdvisoryResponse(
            a.getId(),
            a.getProfile().getId(),
            a.getProfile().getUser().getName(),
            a.getRequesterName(),
            a.getRequesterEmail(),
            a.getScheduledAt().toString(),
            a.getComment(),
            a.getStatus().name(),
            a.getResponse()
        );
    }

    private void ensureAvailability(Long profileId, LocalDateTime scheduledAt) {
        var slots = availabilityRepository.findByProfileId(profileId);
        if (slots.isEmpty()) {
            throw new RuntimeException("El programador no tiene disponibilidad registrada");
        }

        DayOfWeek day = scheduledAt.getDayOfWeek();
        LocalTime time = scheduledAt.toLocalTime();

        boolean matches = slots.stream().anyMatch(s ->
            s.getDay() == day &&
            !time.isBefore(s.getStartTime()) &&
            time.isBefore(s.getEndTime())
        );

        if (!matches) {
            throw new RuntimeException("Horario fuera de la disponibilidad del programador");
        }
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private LocalDateTime parseDateTime(String raw) {
        try {
            return LocalDateTime.parse(raw);
        } catch (Exception e) {
            throw new RuntimeException("Formato de fecha inválido");
        }
    }

    private AdvisoryStatus parseStatus(String raw) {
        try {
            return AdvisoryStatus.valueOf(raw);
        } catch (Exception e) {
            throw new RuntimeException("Estado inválido");
        }
    }
}