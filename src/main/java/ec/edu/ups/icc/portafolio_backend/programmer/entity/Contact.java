package ec.edu.ups.icc.portafolio_backend.programmer.entity;

import jakarta.persistence.*;

@Entity
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;
    private String url;

    @ManyToOne(optional = false)
    @JoinColumn(name = "profile_id")
    private ProgrammerProfile profile;

    public Long getId() { return id; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    public ProgrammerProfile getProfile() { return profile; }
    public void setProfile(ProgrammerProfile profile) { this.profile = profile; }
}