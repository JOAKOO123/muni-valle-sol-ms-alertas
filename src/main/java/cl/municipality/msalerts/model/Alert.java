package cl.municipality.msalerts.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "alerts")
public class Alert {

    @Id
    private String id;

    private String title;
    private String description;
    private Severity severity;
    private Status status;
    private LocalDateTime date;
    private Long reportId;
    private Long userId;

    public enum Severity {
        HIGH, MEDIUM, LOW
    }

    public enum Status {
        ACTIVE, RESOLVED
    }
}
