package ec.edu.ups.icc.portafolio_backend.programmer.service;

import ec.edu.ups.icc.portafolio_backend.programmer.dto.AvailabilityRequest;
import ec.edu.ups.icc.portafolio_backend.programmer.dto.AvailabilityResponse;
import ec.edu.ups.icc.portafolio_backend.programmer.entity.AvailabilitySlot;
import ec.edu.ups.icc.portafolio_backend.programmer.entity.Modality;
import ec.edu.ups.icc.portafolio_backend.programmer.repository.AvailabilityRepository;
import ec.edu.ups.icc.portafolio_backend.programmer.repository.ProgrammerProfileRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

@Service
public class AvailabilityService {

    private final AvailabilityRepository availabilityRepository;
    private final ProgrammerProfileRepository profileRepository;

    public AvailabilityService(AvailabilityRepository availabilityRepository, ProgrammerProfileRepository profileRepository) {
        this.availabilityRepository = availabilityRepository;
        this.profileRepository = profileRepository;
    }

    public AvailabilityResponse add(Long profileId, AvailabilityRequest r) {
        if (r == null) throw new RuntimeException("Solicitud inválida");

        DayOfWeek day = parseDay(r.day());
        LocalTime start = parseTime(r.startTime());
        LocalTime end = parseTime(r.endTime());

        if (!start.isBefore(end)) {
            throw new RuntimeException("La hora de inicio debe ser menor a la hora de fin");
        }

        var existing = availabilityRepository.findByProfileId(profileId);
        for (AvailabilitySlot slot : existing) {
            if (slot.getDay() == day && overlaps(start, end, slot.getStartTime(), slot.getEndTime())) {
                throw new RuntimeException("La disponibilidad se cruza con un horario existente");
            }
        }

        var profile = profileRepository.findById(profileId).orElseThrow();
        AvailabilitySlot slot = new AvailabilitySlot();
        slot.setProfile(profile);
        slot.setDay(day);
        slot.setStartTime(start);
        slot.setEndTime(end);
        slot.setModality(Modality.valueOf(r.modality()));
        availabilityRepository.save(slot);
        return toResponse(slot);
    }

    public List<AvailabilityResponse> list(Long profileId, Integer page, Integer size) {
        if (page == null || size == null) {
            return availabilityRepository.findByProfileId(profileId).stream().map(this::toResponse).toList();
        }
        return availabilityRepository.findByProfileId(profileId, PageRequest.of(page, size))
            .getContent().stream().map(this::toResponse).toList();
    }

    public void delete(Long id) {
        availabilityRepository.deleteById(id);
    }

    private AvailabilityResponse toResponse(AvailabilitySlot slot) {
        return new AvailabilityResponse(
            slot.getId(),
            slot.getDay().name(),
            slot.getStartTime().toString(),
            slot.getEndTime().toString(),
            slot.getModality().name()
        );
    }

    private DayOfWeek parseDay(String raw) {
        try {
            return DayOfWeek.valueOf(raw);
        } catch (Exception e) {
            throw new RuntimeException("Día inválido");
        }
    }

    private LocalTime parseTime(String raw) {
        try {
            return LocalTime.parse(raw);
        } catch (Exception e) {
            throw new RuntimeException("Hora inválida");
        }
    }

    private boolean overlaps(LocalTime start, LocalTime end, LocalTime otherStart, LocalTime otherEnd) {
        return start.isBefore(otherEnd) && end.isAfter(otherStart);
    }
}