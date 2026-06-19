package cl.municipality.msalerts.mapper;

import cl.municipality.msalerts.dto.AlertResponseDTO;
import cl.municipality.msalerts.model.Alert;
import org.springframework.stereotype.Component;

/**
 * Mapper que convierte entidades {@link Alert} en objetos {@link AlertResponseDTO}.
 * Centraliza la transformacion para evitar duplicacion en la capa de servicio.
 *
 * <p>Patrones aplicados:</p>
 * <ul>
 *   <li>Mapper Pattern: convierte entre modelos de dominio y DTOs</li>
 *   <li>Single Responsibility: solo realiza la conversion de tipos</li>
 * </ul>
 *
 * @author Beltran
 * @version 1.1
 * @since 1.0
 */
@Component
public class AlertMapper {

    /**
     * Convierte una entidad {@link Alert} en un {@link AlertResponseDTO}.
     * Incluye coordenadas geograficas en el DTO de salida.
     *
     * @param alert Entidad de dominio a convertir.
     * @return DTO de respuesta con todos los campos mapeados.
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
                alert.getUserId(),
                alert.getLatitude(),
                alert.getLongitude()
        );
    }
}