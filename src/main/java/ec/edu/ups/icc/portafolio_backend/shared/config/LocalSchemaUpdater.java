package ec.edu.ups.icc.portafolio_backend.shared.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@Profile("local")
public class LocalSchemaUpdater implements ApplicationRunner {

    private final JdbcTemplate jdbcTemplate;

    public LocalSchemaUpdater(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (!columnExists("advisory", "reminder_sent")) {
            jdbcTemplate.execute("ALTER TABLE advisory ADD COLUMN reminder_sent BOOLEAN NOT NULL DEFAULT FALSE");
        }
    }

    private boolean columnExists(String tableName, String columnName) {
        Integer count = jdbcTemplate.queryForObject(
            """
            SELECT COUNT(*)
            FROM information_schema.columns
            WHERE table_name = ? AND column_name = ?
            """,
            Integer.class,
            tableName,
            columnName
        );
        return count != null && count > 0;
    }
}