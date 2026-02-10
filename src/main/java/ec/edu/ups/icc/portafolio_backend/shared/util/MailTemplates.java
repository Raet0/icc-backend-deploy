package ec.edu.ups.icc.portafolio_backend.shared.util;

import ec.edu.ups.icc.portafolio_backend.programmer.entity.Advisory;

public final class MailTemplates {
    private MailTemplates() {}

    public static String advisoryCreatedForRequester(Advisory advisory) {
        return "Tu asesoría fue registrada para: " + advisory.getScheduledAt()
            + "\nProgramador: " + advisory.getProfile().getUser().getName();
    }

    public static String advisoryCreatedForProgrammer(Advisory advisory) {
        return "Nueva solicitud de asesoría.\nSolicitante: " + advisory.getRequesterName()
            + "\nEmail: " + advisory.getRequesterEmail()
            + "\nFecha: " + advisory.getScheduledAt();
    }

    public static String advisoryUpdatedForRequester(Advisory advisory) {
        return "Tu asesoría fue " + advisory.getStatus().name()
            + " para: " + advisory.getScheduledAt()
            + (advisory.getResponse() != null ? "\nRespuesta: " + advisory.getResponse() : "");
    }

    public static String advisoryUpdatedForProgrammer(Advisory advisory) {
        return "Estado actualizado a " + advisory.getStatus().name()
            + " para la asesoría del " + advisory.getScheduledAt()
            + "\nSolicitante: " + advisory.getRequesterName();
    }

    public static String advisoryReminderForRequester(Advisory advisory) {
        return "Recordatorio: tu asesoría es el " + advisory.getScheduledAt()
            + "\nProgramador: " + advisory.getProfile().getUser().getName();
    }

    public static String advisoryReminderForProgrammer(Advisory advisory) {
        return "Recordatorio: asesoría con " + advisory.getRequesterName()
            + " el " + advisory.getScheduledAt();
    }
}