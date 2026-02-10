package ec.edu.ups.icc.portafolio_backend.programmer.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(length = 2000)
    private String description;

    @Enumerated(EnumType.STRING)
    private Participation participation;

    @ElementCollection
    private List<String> technologies = new ArrayList<>();

    private String repoUrl;
    private String demoUrl;

    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private ProjectSection section;

    private boolean active = true;

    @ManyToOne(optional = false)
    @JoinColumn(name = "profile_id")
    private ProgrammerProfile profile;

    public Long getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Participation getParticipation() { return participation; }
    public void setParticipation(Participation participation) { this.participation = participation; }
    public List<String> getTechnologies() { return technologies; }
    public void setTechnologies(List<String> technologies) { this.technologies = technologies; }
    public String getRepoUrl() { return repoUrl; }
    public void setRepoUrl(String repoUrl) { this.repoUrl = repoUrl; }
    public String getDemoUrl() { return demoUrl; }
    public void setDemoUrl(String demoUrl) { this.demoUrl = demoUrl; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public ProjectSection getSection() { return section; }
    public void setSection(ProjectSection section) { this.section = section; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public ProgrammerProfile getProfile() { return profile; }
    public void setProfile(ProgrammerProfile profile) { this.profile = profile; }
}