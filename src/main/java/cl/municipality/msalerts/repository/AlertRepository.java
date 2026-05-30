package cl.municipality.msalerts.repository;

import cl.municipality.msalerts.model.Alert;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AlertRepository extends MongoRepository<Alert, String> {
    List<Alert> findByStatus(Alert.Status status);
    List<Alert> findByReportId(Long reportId);
}
