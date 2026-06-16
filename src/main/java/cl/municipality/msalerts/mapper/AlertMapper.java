package cl.municipality.msalerts.mapper;

import cl.municipality.msalerts.dto.AlertResponseDTO;
import cl.municipality.msalerts.model.Alert;
import org.springframework.stereotype.Component;

/**
 * Componente encargado de convertir entidades {@link Alert} en DTOs de salida.
 * Centraliza la logica de mapeo para evitar duplicacion entre capas.
 *
 * <p>Patrones aplicados:</p>
 * <ul>
 *   <li>Mapper Pattern: separa la transformacion de datos de la logica de negocio</li>
 *   <li>Single Responsibility: solo se encarga de la conversion Alert → AlertResponseDTO</li>
 * </ul>
 *
 * @author Beltran
 * @version 1.0
 * @since 1.0
 */
@Component
public class AlertMapper {

    /**
     * Convierte una entidad {@link Alert} en su representacion {@link AlertResponseDTO}.
     *
     * @param alert Entidad a convertir. No debe ser null.
     * @return DTO con los datos de la alerta listos para ser enviados al cliente.
     */
    public AlertResponseDTO toDTO(Alert alert) {
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