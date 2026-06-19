package cl.municipality.msalerts.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO de entrada para la creacion de una nueva alerta.
 * Contiene los datos enviados por el cliente en la solicitud.
 *
 * <p>Patrones aplicados:</p>
 * <ul>
 *   <li>DTO Pattern: desacopla la capa HTTP del modelo de dominio</li>
 *   <li>Single Responsibility: solo transporta datos de entrada</li>
 * </ul>
 *
 * @param title       Titulo de la alerta. Obligatorio, no puede ser vacio.
 * @param description Descripcion detallada. Obligatorio, no puede ser vacio.
 * @param severity    Severidad como texto: HIGH, MEDIUM o LOW. Obligatorio.
 * @param reportId    Id del reporte asociado. Opcional, puede ser null.
 * @param userId      Id del usuario relacionado. Opcional, puede ser null.
 * @param latitude    Latitud del incidente. Opcional, puede ser null.
 * @param longitude   Longitud del incidente. Opcional, puede ser null.
 *
 * @author Beltran
 * @version 1.1
 * @since 1.0
 */
public record AlertRequestDTO(
        @NotBlank String title,
        @NotBlank String description,
        @NotNull  String severity,
        Long reportId,
        Long userId,
        Double latitude,
        Double longitude
) {}