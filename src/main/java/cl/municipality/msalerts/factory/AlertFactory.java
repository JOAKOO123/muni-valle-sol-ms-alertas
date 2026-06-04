package cl.municipality.msalerts.factory;

import cl.municipality.msalerts.dto.AlertRequestDTO;
import cl.municipality.msalerts.model.Alert;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Fabrica responsable de construir instancias de Alert a partir de un DTO.
 * Centraliza la logica de creacion del objeto de dominio.
 *
 * <p>Patrones aplicados:</p>
 * <ul>
 *   <li>Factory Pattern: encapsula la construccion del objeto Alert</li>
 *   <li>Single Responsibility: solo se encarga de instanciar alertas</li>
 * </ul>
 *
 * @author Beltran
 * @version 1.0
 * @since 1.0
 */
@Component
public class AlertFactory {

    /**
     * Crea una nueva instancia de Alert a partir del DTO recibido.
     * El estado se fija en ACTIVE y la fecha se asigna al momento de la creacion.
     *
     * @param request DTO con los datos de la alerta. No debe ser null.
     * @return Instancia de Alert lista para ser persistida.
     * @throws IllegalArgumentException si severity no corresponde a ningun valor valido.
     */
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