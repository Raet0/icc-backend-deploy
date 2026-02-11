package ec.edu.ups.icc.portafolio_backend.programmer.service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import ec.edu.ups.icc.portafolio_backend.programmer.entity.Advisory;
import ec.edu.ups.icc.portafolio_backend.shared.util.MailTemplates;

@Service
public class NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    @Value("${sendgrid.api.key:}")
    private String sendGridApiKey;

    @Value("${spring.mail.username:}")
    private String mailFrom;

    public void notifyByEmail(String to, String subject, String body) {
        if (sendGridApiKey == null || sendGridApiKey.isBlank()) {
            logger.warn("Correo no enviado a {}: falta sendgrid.api.key", to);
            return;
        }
        if (mailFrom == null || mailFrom.isBlank()) {
            logger.warn("Correo no enviado a {}: falta spring.mail.username", to);
            return;
        }

        try {
            logger.info("=== SENDGRID EMAIL ===");
            logger.info("From: {}", mailFrom);
            logger.info("To: {}", to);
            logger.info("Subject: {}", subject);
            
            Email from = new Email(mailFrom);
            Email toEmail = new Email(to);
            Content content = new Content("text/plain", body);
            Mail mail = new Mail(from, subject, toEmail, content);

            SendGrid sg = new SendGrid(sendGridApiKey);
            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            
            Response response = sg.api(request);
            
            if (response.getStatusCode() >= 200 && response.getStatusCode() < 300) {
                logger.info("✓ Email enviado exitosamente a {} (status: {})", to, response.getStatusCode());
            } else {
                logger.error("✗ Error al enviar email a {}: Status {}, Body: {}", 
                    to, response.getStatusCode(), response.getBody());
            }
        } catch (Exception ex) {
            logger.error("✗ Excepción enviando email a {}: {}", to, ex.getMessage(), ex);
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
