package ec.edu.ups.icc.portafolio_backend.programmer.service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import ec.edu.ups.icc.portafolio_backend.programmer.dto.ContactEmailRequest;
import ec.edu.ups.icc.portafolio_backend.programmer.repository.ProgrammerProfileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ContactEmailService {

    private static final Logger logger = LoggerFactory.getLogger(ContactEmailService.class);

    private final ProgrammerProfileRepository profileRepository;

    @Value("${sendgrid.api.key:}")
    private String sendGridApiKey;

    @Value("${spring.mail.username:}")
    private String fromAddress;

    public ContactEmailService(ProgrammerProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    public void sendContactEmail(Long profileId, ContactEmailRequest request) {
        if (sendGridApiKey == null || sendGridApiKey.isBlank()) {
            logger.warn("Email no enviado: falta sendgrid.api.key");
            throw new RuntimeException("Configuración de email no disponible");
        }

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

        try {
            Email from = new Email(fromAddress);
            Email to = new Email(programmerEmail);
            Content content = new Content("text/plain", body);
            Mail mail = new Mail(from, "Solicitud de contacto", to, content);
            mail.setReplyTo(new Email(request.requesterEmail()));

            SendGrid sg = new SendGrid(sendGridApiKey);
            Request sgRequest = new Request();
            sgRequest.setMethod(Method.POST);
            sgRequest.setEndpoint("mail/send");
            sgRequest.setBody(mail.build());

            Response response = sg.api(sgRequest);

            if (response.getStatusCode() >= 200 && response.getStatusCode() < 300) {
                logger.info("Email de contacto enviado a {} (status: {})", programmerEmail, response.getStatusCode());
            } else {
                logger.error("Error enviando email: Status {}, Body: {}", response.getStatusCode(), response.getBody());
                throw new RuntimeException("Error al enviar email");
            }
        } catch (Exception e) {
            logger.error("Excepción enviando email: {}", e.getMessage(), e);
            throw new RuntimeException("Error al enviar email: " + e.getMessage());
        }
    }
}