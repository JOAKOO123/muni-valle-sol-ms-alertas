package cl.municipality.msalerts.exception;

/**
 * Excepcion lanzada cuando no se encuentra una alerta con el id indicado.
 * Capturada globalmente por GlobalExceptionHandler con respuesta 404.
 *
 * <p>Patrones aplicados:</p>
 * <ul>
 *   <li>Single Responsibility: solo representa el caso de alerta no encontrada</li>
 * </ul>
 *
 * @author Beltran
 * @version 1.0
 * @since 1.0
 */
public class AlertNotFoundException extends RuntimeException {

    /**
     * Construye la excepcion con el id de la alerta no encontrada.
     *
     * @param id Identificador de la alerta que no fue encontrada.
     */
    public AlertNotFoundException(String id) {
        super("Alert not found with id: " + id);
    }
}