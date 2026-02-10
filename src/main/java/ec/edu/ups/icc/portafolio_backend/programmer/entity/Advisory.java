package ec.edu.ups.icc.portafolio_backend.programmer.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Advisory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "profile_id")
    private ProgrammerProfile profile;

    private String requesterName;
    private String requesterEmail;

    private LocalDateTime scheduledAt;

    @Column(length = 2000)
    private String comment;

    @Enumerated(EnumType.STRING)
    private AdvisoryStatus status = AdvisoryStatus.PENDIENTE;

    @Column(length = 2000)
    private String response;

    private LocalDateTime createdAt = LocalDateTime.now();

    private boolean reminderSent = false;

    public Long getId() { return id; }
    public ProgrammerProfile getProfile() { return profile; }
    public void setProfile(ProgrammerProfile profile) { this.profile = profile; }
    public String getRequesterName() { return requesterName; }
    public void setRequesterName(String requesterName) { this.requesterName = requesterName; }
    public String getRequesterEmail() { return requesterEmail; }
    public void setRequesterEmail(String requesterEmail) { this.requesterEmail = requesterEmail; }
    public LocalDateTime getScheduledAt() { return scheduledAt; }
    public void setScheduledAt(LocalDateTime scheduledAt) { this.scheduledAt = scheduledAt; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    public AdvisoryStatus getStatus() { return status; }
    public void setStatus(AdvisoryStatus status) { this.status = status; }
    public String getResponse() { return response; }
    public void setResponse(String response) { this.response = response; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public boolean isReminderSent() { return reminderSent; }
    public void setReminderSent(boolean reminderSent) { this.reminderSent = reminderSent; }
}