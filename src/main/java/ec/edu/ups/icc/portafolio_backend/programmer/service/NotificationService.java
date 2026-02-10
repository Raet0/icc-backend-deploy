package ec.edu.ups.icc.portafolio_backend.programmer.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import ec.edu.ups.icc.portafolio_backend.programmer.entity.Advisory;
import ec.edu.ups.icc.portafolio_backend.shared.util.MailTemplates;

@Service
public class NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username:}")
    private String mailFrom;

    public NotificationService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void notifyByEmail(String to, String subject, String body) {
        if (mailFrom == null || mailFrom.isBlank()) {
            logger.warn("Correo no enviado: falta spring.mail.username");
            return;
        }
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(mailFrom);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
        } catch (Exception ex) {
            logger.error("Error enviando correo a {}", to, ex);
        }
    }

    public void notifyAdvisoryCreated(Advisory advisory) {
        notifyByEmail(
            advisory.getRequesterEmail(),
            "Asesoría creada",
            MailTemplates.advisoryCreatedForRequester(advisory)
        );
        notifyByEmail(
            advisory.getProfile().getUser().getEmail(),
            "Nueva asesoría solicitada",
            MailTemplates.advisoryCreatedForProgrammer(advisory)
        );
    }

    public void notifyAdvisoryUpdated(Advisory advisory) {
        notifyByEmail(
            advisory.getRequesterEmail(),
            "Asesoría actualizada",
            MailTemplates.advisoryUpdatedForRequester(advisory)
        );
        notifyByEmail(
            advisory.getProfile().getUser().getEmail(),
            "Estado de asesoría actualizado",
            MailTemplates.advisoryUpdatedForProgrammer(advisory)
        );
    }

    public void notifyAdvisoryReminder(Advisory advisory) {
        notifyByEmail(
            advisory.getRequesterEmail(),
            "Recordatorio de asesoría",
            MailTemplates.advisoryReminderForRequester(advisory)
        );
        notifyByEmail(
            advisory.getProfile().getUser().getEmail(),
            "Recordatorio de asesoría",
            MailTemplates.advisoryReminderForProgrammer(advisory)
        );
    }
}
