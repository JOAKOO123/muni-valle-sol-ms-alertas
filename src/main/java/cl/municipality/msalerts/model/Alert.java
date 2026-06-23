package cl.municipality.msalerts.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * Entidad que representa una alerta municipal en el sistema.
 * Almacenada en la coleccion alerts de MongoDB.
 *
 * <p>Patrones aplicados:</p>
 * <ul>
 *   <li>Builder Pattern: construccion fluida mediante Lombok @Builder</li>
 *   <li>Single Responsibility: solo modela los datos de una alerta</li>
 * </ul>
 *
 * @author Beltran
 * @version 1.1
 * @since 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "alerts")
public class Alert {

    /**
     * Identificador unico generado por MongoDB.
     */
    @Id
    private String id;

    /**
     * Titulo descriptivo de la alerta.
     */
    private String title;

    /**
     * Descripcion detallada del evento que origino la alerta.
     */
    private String description;

    /**
     * Nivel de severidad de la alerta.
     */
    private Severity severity;

    /**
     * Estado actual de la alerta en su ciclo de vida.
     */
    private Status status;

    /**
     * Fecha y hora de creacion de la alerta.
     */
    private LocalDateTime date;

    /**
     * Identificador del reporte asociado. Puede ser null.
     */
    private Long reportId;

    /**
     * Identificador del usuario relacionado. Puede ser null.
     */
    private Long userId;

    /**
     * Coordenada geografica de latitud de la ubicacion del incidente.
     * Puede ser null si la alerta no fue generada desde un reporte con ubicacion.
     */
    private Double latitude;

    /**
     * Coordenada geografica de longitud de la ubicacion del incidente.
     * Puede ser null si la alerta no fue generada desde un reporte con ubicacion.
     */
    private Double longitude;

    /**
     * Nivel de severidad de una alerta.
     *
     * @author Beltran
     * @version 1.0
     * @since 1.0
     */
    public enum Severity {
        /** Situacion critica que requiere atencion inmediata. */
        HIGH,
        /** Situacion importante pero no urgente. */
        MEDIUM,
        /** Situacion menor o informativa. */
        LOW
    }

    /**
     * Estado del ciclo de vida de una alerta.
     *
     * @author Beltran
     * @version 1.0
     * @since 1.0
     */
    public enum Status {
        /** La alerta esta vigente y pendiente de resolucion. */
        ACTIVE,
        /** La alerta ha sido atendida y cerrada. */
        RESOLVED
    }
}