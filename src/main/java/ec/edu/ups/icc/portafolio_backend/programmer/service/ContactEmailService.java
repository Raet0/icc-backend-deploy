package ec.edu.ups.icc.portafolio_backend.programmer.service;

import ec.edu.ups.icc.portafolio_backend.programmer.dto.ContactEmailRequest;
import ec.edu.ups.icc.portafolio_backend.programmer.repository.ProgrammerProfileRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class ContactEmailService {

    private final ProgrammerProfileRepository profileRepository;
    private final JavaMailSender mailSender;
    private final String fromAddress;

    public ContactEmailService(
        ProgrammerProfileRepository profileRepository,
        JavaMailSender mailSender,
        @Value("${app.mail.from:${spring.mail.username}}") String fromAddress
    ) {
        this.profileRepository = profileRepository;
        this.mailSender = mailSender;
        this.fromAddress = fromAddress;
    }

    public void sendContactEmail(Long profileId, ContactEmailRequest request) {
        var profile = profileRepository.findById(profileId).orElseThrow();
        var programmerName = profile.getUser().getName();
        var programmerEmail = profile.getUser().getEmail();

        var messageBody = request.message();
        if (messageBody == null || messageBody.isBlank()) {
            messageBody = "Me gustaria contactarte para una asesoria.";
        }

        var body = "Hola " + programmerName + ",\n\n"
            + messageBody + "\n\n"
            + "Datos de contacto del solicitante:\n"
            + "Nombre: " + request.requesterName() + "\n"
            + "Email: " + request.requesterEmail() + "\n";

        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setFrom(fromAddress);
        mail.setTo(programmerEmail);
        mail.setReplyTo(request.requesterEmail());
        mail.setSubject("Solicitud de contacto");
        mail.setText(body);

        mailSender.send(mail);
    }
}