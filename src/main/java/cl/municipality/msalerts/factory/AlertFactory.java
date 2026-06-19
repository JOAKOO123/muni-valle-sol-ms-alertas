package cl.municipality.msalerts.factory;

import cl.municipality.msalerts.dto.AlertRequestDTO;
import cl.municipality.msalerts.model.Alert;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Fabrica de instancias de {@link Alert}.
 * Centraliza la creacion de objetos Alert para garantizar consistencia
 * en los valores por defecto y evitar duplicacion de logica de construccion.
 *
 * <p>Patrones aplicados:</p>
 * <ul>
 *   <li>Factory Method Pattern: abstrae la creacion de Alert</li>
 *   <li>Single Responsibility: solo se encarga de instanciar Alert</li>
 * </ul>
 *
 * @author Beltran
 * @version 1.1
 * @since 1.0
 */
@Component
public class AlertFactory {

    /**
     * Crea una nueva instancia de {@link Alert} a partir del DTO de entrada.
     * Asigna estado ACTIVE por defecto y registra la fecha actual de creacion.
     * Incluye coordenadas geograficas si fueron proporcionadas.
     *
     * @param dto DTO con los datos de la alerta a crear.
     * @return Instancia de Alert lista para persistir en MongoDB.
     */
    public Alert create(AlertRequestDTO dto) {
        return Alert.builder()
                .title(dto.title())
                .description(dto.description())
                .severity(Alert.Severity.valueOf(dto.severity()))
                .status(Alert.Status.ACTIVE)
                .date(LocalDateTime.now())
                .reportId(dto.reportId())
                .userId(dto.userId())
                .latitude(dto.latitude())
                .longitude(dto.longitude())
                .build();
    }
}