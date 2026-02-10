package ec.edu.ups.icc.portafolio_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PortafolioBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(PortafolioBackendApplication.class, args);
    }
}