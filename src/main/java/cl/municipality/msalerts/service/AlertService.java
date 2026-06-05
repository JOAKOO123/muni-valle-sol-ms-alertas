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

/**
 * Servicio de negocio para la gestion del ciclo de vida de las alertas municipales.
 * Actua como capa intermedia entre el controlador REST y el repositorio.
 *
 * <p>Patrones aplicados:</p>
 * <ul>
 *   <li>Facade Pattern: simplifica las operaciones sobre alertas para el controlador</li>
 *   <li>Factory Pattern: delega la construccion de entidades a AlertFactory</li>
 *   <li>Single Responsibility: solo gestiona la logica de negocio de alertas</li>
 * </ul>
 *
 * @author Beltran
 * @version 1.0
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
public class AlertService {

    private final AlertRepository alertRepository;
    private final AlertFactory alertFactory;

    /**
     * Crea y persiste una nueva alerta en estado ACTIVE.
     *
     * @param request DTO con los datos de la alerta. No debe ser null.
     * @return DTO con la alerta creada, incluyendo el id generado por MongoDB.
     */
    public AlertResponseDTO create(AlertRequestDTO request) {
        Alert alert = alertFactory.create(request);
        return toDTO(alertRepository.save(alert));
    }

    /**
     * Retorna todas las alertas con estado ACTIVE.
     *
     * @return Lista de DTOs de alertas activas. Vacia si no hay ninguna.
     */
    public List<AlertResponseDTO> listActive() {
        return alertRepository.findByStatus(Alert.Status.ACTIVE)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    /**
     * Retorna el historial completo de alertas sin importar su estado.
     *
     * @return Lista de DTOs con todas las alertas registradas. Vacia si no hay ninguna.
     */
    public List<AlertResponseDTO> listAll() {
        return alertRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    /**
     * Busca y retorna una alerta por su identificador.
     *
     * @param id Identificador unico de la alerta.
     * @return DTO con los datos de la alerta encontrada.
     * @throws AlertNotFoundException si no existe ninguna alerta con el id indicado.
     */
    public AlertResponseDTO findById(String id) {
        return toDTO(alertRepository.findById(id)
                .orElseThrow(() -> new AlertNotFoundException(id)));
    }

    /**
     * Cambia el estado de una alerta existente.
     * Los valores validos son ACTIVE y RESOLVED.
     *
     * @param id     Identificador de la alerta a actualizar.
     * @param status Nuevo estado: "ACTIVE" o "RESOLVED".
     * @return DTO con la alerta actualizada.
     * @throws AlertNotFoundException    si no existe ninguna alerta con el id indicado.
     * @throws IllegalArgumentException si el valor de status no corresponde a ningun estado valido.
     */
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
        return toDTO(alertRepository.save(alert));
    }

    /**
     * Elimina permanentemente una alerta de la base de datos.
     *
     * @param id Identificador de la alerta a eliminar.
     * @throws AlertNotFoundException si no existe ninguna alerta con el id indicado.
     */
    public void delete(String id) {
        if (!alertRepository.existsById(id)) {
            throw new AlertNotFoundException(id);
        }
        alertRepository.deleteById(id);
    }

    /**
     * Convierte una entidad Alert en su representacion AlertResponseDTO.
     *
     * @param alert Entidad a convertir. No debe ser null.
     * @return DTO con los datos de la alerta.
     */
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