package cl.municipality.msalerts.service;

import cl.municipality.msalerts.dto.AlertRequestDTO;
import cl.municipality.msalerts.dto.AlertResponseDTO;
import cl.municipality.msalerts.exception.AlertNotFoundException;
import cl.municipality.msalerts.factory.AlertFactory;
import cl.municipality.msalerts.model.Alert;
import cl.municipality.msalerts.repository.AlertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlertService {

    private final AlertRepository alertRepository;
    private final AlertFactory alertFactory;

    public AlertResponseDTO create(AlertRequestDTO request) {
        Alert alert = alertFactory.create(request);
        return toDTO(alertRepository.save(alert));
    }

    public List<AlertResponseDTO> listActive() {
        return alertRepository.findByStatus(Alert.Status.ACTIVE)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public List<AlertResponseDTO> listAll() {
        return alertRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public AlertResponseDTO findById(String id) {
        return toDTO(alertRepository.findById(id)
                .orElseThrow(() -> new AlertNotFoundException(id)));
    }

    public AlertResponseDTO changeStatus(String id, String status) {
        Alert alert = alertRepository.findById(id)
                .orElseThrow(() -> new AlertNotFoundException(id));
        alert.setStatus(Alert.Status.valueOf(status));
        return toDTO(alertRepository.save(alert));
    }

    public void delete(String id) {
        if (!alertRepository.existsById(id)) {
            throw new AlertNotFoundException(id);
        }
        alertRepository.deleteById(id);
    }

    private AlertResponseDTO toDTO(Alert alert) {
        return new AlertResponseDTO(
                alert.getId(),
                alert.getTitle(),
                alert.getDescription(),
                alert.getSeverity().name(),
                alert.getStatus().name(),
                alert.getDate(),
                alert.getReportId(),
                alert.getUserId()
        );
    }
}
