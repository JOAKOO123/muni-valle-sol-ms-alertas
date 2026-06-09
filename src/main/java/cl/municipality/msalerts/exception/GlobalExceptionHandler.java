package cl.municipality.msalerts.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Manejador global de excepciones del microservicio de alertas.
 * Intercepta excepciones y retorna respuestas HTTP con formato estandarizado.
 *
 * <p>Patrones aplicados:</p>
 * <ul>
 *   <li>Chain of Responsibility: intercepta excepciones en cascada</li>
 *   <li>Single Responsibility: centraliza el manejo de errores</li>
 * </ul>
 *
 * @author Beltran
 * @version 1.0
 * @since 1.0
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maneja el caso en que no se encuentra una alerta por su id.
     *
     * @param ex excepcion con el id que no fue encontrado
     * @return ResponseEntity con status 404 y detalle del error
     */
    @ExceptionHandler(AlertNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(AlertNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                "error", ex.getMessage(),
                "timestamp", LocalDateTime.now().toString(),
                "status", 404
        ));
    }

    /**
     * Maneja errores de validacion de @Valid en el body del request.
     *
     * @param ex excepcion con los campos que fallaron la validacion
     * @return ResponseEntity con status 400 y detalle del primer error
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .findFirst()
                .orElse("Datos de entrada invalidos");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                "error", message,
                "timestamp", LocalDateTime.now().toString(),
                "status", 400
        ));
    }

    /**
     * Maneja valores invalidos en parametros de negocio (ej: status desconocido).
     *
     * @param ex excepcion con el detalle del valor invalido
     * @return ResponseEntity con status 400 y detalle del error
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                "error", ex.getMessage(),
                "timestamp", LocalDateTime.now().toString(),
                "status", 400
        ));
    }

    /**
     * Maneja excepciones genericas de runtime no controladas.
     *
     * @param ex excepcion capturada
     * @return ResponseEntity con status 500 y detalle del error
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntime(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "error", ex.getMessage(),
                "timestamp", LocalDateTime.now().toString(),
                "status", 500
        ));
    }
}