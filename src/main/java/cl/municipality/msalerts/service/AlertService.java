package cl.municipality.msalerts.service;

import cl.municipality.msalerts.dto.AlertRequestDTO;
import cl.municipality.msalerts.dto.AlertResponseDTO;
import cl.municipality.msalerts.exception.AlertNotFoundException;
import cl.municipality.msalerts.factory.AlertFactory;
import cl.municipality.msalerts.mapper.AlertMapper;
import cl.municipality.msalerts.model.Alert;
import cl.municipality.msalerts.repository.AlertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementacion concreta del puerto {@link AlertServicePort}.
 * Gestiona el ciclo de vida de las alertas municipales actuando como capa
 * intermedia entre el controlador REST y el repositorio de datos.
 *
 * <p>Patrones aplicados:</p>
 * <ul>
 *   <li>Facade Pattern: simplifica las operaciones sobre alertas para el controlador</li>
 *   <li>Factory Pattern: delega la construccion de entidades a {@link AlertFactory}</li>
 *   <li>Mapper Pattern: delega la conversion a DTO a {@link AlertMapper}</li>
 *   <li>Single Responsibility: solo gestiona la logica de negocio de alertas</li>
 * </ul>
 *
 * @author Beltran
 * @version 1.0
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
public class AlertService implements AlertServicePort {

    private final AlertRepository alertRepository;
    private final AlertFactory alertFactory;
    private final AlertMapper alertMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public AlertResponseDTO create(AlertRequestDTO request) {
        Alert alert = alertFactory.create(request);
        return alertMapper.toDTO(alertRepository.save(alert));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AlertResponseDTO> listActive() {
        return alertRepository.findByStatus(Alert.Status.ACTIVE)
                .stream()
                .map(alertMapper::toDTO)
                .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AlertResponseDTO> listAll() {
        return alertRepository.findAll()
                .stream()
                .map(alertMapper::toDTO)
                .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AlertResponseDTO findById(String id) {
        return alertMapper.toDTO(
                alertRepository.findById(id)
                        .orElseThrow(() -> new AlertNotFoundException(id))
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AlertResponseDTO changeStatus(String id, String status) {
        Alert alert = alertRepository.findById(id)
                .orElseThrow(() -> new AlertNotFoundException(id));

        Alert.Status newStatus;
        try {
            newStatus = Alert.Status.valueOf(status);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                "Estado invalido: '" + status + "'. Los valores validos son: ACTIVE, RESOLVED"
            );
        }

        alert.setStatus(newStatus);
        return alertMapper.toDTO(alertRepository.save(alert));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(String id) {
        if (!alertRepository.existsById(id)) {
            throw new AlertNotFoundException(id);
        }
        alertRepository.deleteById(id);
    }
}