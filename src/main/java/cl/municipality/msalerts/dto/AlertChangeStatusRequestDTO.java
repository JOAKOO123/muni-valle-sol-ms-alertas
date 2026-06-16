package cl.municipality.msalerts.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO de entrada para la actualizacion del estado de una alerta.
 * Reemplaza el uso de Map&lt;String, String&gt; en el controlador para garantizar
 * tipado fuerte y validacion declarativa.
 *
 * <p>Patrones aplicados:</p>
 * <ul>
 *   <li>DTO Pattern: desacopla la capa HTTP del modelo de dominio</li>
 *   <li>Single Responsibility: solo transporta el nuevo estado solicitado</li>
 * </ul>
 *
 * @param status Nuevo estado de la alerta. Valores validos: ACTIVE, RESOLVED.
 *
 * @author Beltran
 * @version 1.0
 * @since 1.0
 */
public record AlertChangeStatusRequestDTO(
        @NotBlank String status
) {}