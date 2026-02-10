package ec.edu.ups.icc.portafolio_backend.programmer.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SocialLink {
    private String name;
    private String url;
}