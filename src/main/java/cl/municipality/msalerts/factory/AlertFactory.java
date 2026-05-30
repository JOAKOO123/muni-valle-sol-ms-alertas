package cl.municipality.msalerts.factory;

import cl.municipality.msalerts.dto.AlertRequestDTO;
import cl.municipality.msalerts.model.Alert;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AlertFactory {

    public Alert create(AlertRequestDTO request) {
        return Alert.builder()
                .title(request.title())
                .description(request.description())
                .severity(Alert.Severity.valueOf(request.severity()))
                .status(Alert.Status.ACTIVE)
                .date(LocalDateTime.now())
                .reportId(request.reportId())
                .userId(request.userId())
                .build();
    }
}
