package cl.municipality.msalerts.controller;

import cl.municipality.msalerts.dto.AlertRequestDTO;
import cl.municipality.msalerts.dto.AlertResponseDTO;
import cl.municipality.msalerts.service.AlertService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controlador REST para la gestion de alertas municipales.
 * Expone los endpoints bajo la ruta base /api/alerts.
 *
 * <p>Patrones aplicados:</p>
 * <ul>
 *   <li>Facade Pattern: delega toda la logica al AlertService</li>
 *   <li>Single Responsibility: solo gestiona los endpoints de alertas</li>
 * </ul>
 *
 * @author Beltran
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping("/api/alerts")
@RequiredArgsConstructor
public class AlertController {

    private final AlertService alertService;

    /**
     * Crea una nueva alerta.
     * POST /api/alerts
     *
     * @param request Cuerpo de la solicitud con los datos de la alerta.
     * @return 201 Created con el DTO de la alerta creada.
     */
    @PostMapping
    public ResponseEntity<AlertResponseDTO> create(@Valid @RequestBody AlertRequestDTO request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(alertService.create(request));
    }

    /**
     * Lista todas las alertas activas.
     * GET /api/alerts
     *
     * @return 200 OK con la lista de alertas en estado ACTIVE.
     */
    @GetMapping
    public ResponseEntity<List<AlertResponseDTO>> listActive() {
        return ResponseEntity.ok(alertService.listActive());
    }

    /**
     * Lista el historial completo de alertas sin importar su estado.
     * GET /api/alerts/history
     *
     * @return 200 OK con todas las alertas registradas.
     */
    @GetMapping("/history")
    public ResponseEntity<List<AlertResponseDTO>> listAll() {
        return ResponseEntity.ok(alertService.listAll());
    }

    /**
     * Obtiene una alerta por su identificador.
     * GET /api/alerts/{id}
     *
     * @param id Identificador unico de la alerta.
     * @return 200 OK con el DTO de la alerta encontrada.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AlertResponseDTO> findById(@PathVariable String id) {
        return ResponseEntity.ok(alertService.findById(id));
    }

    /**
     * Cambia el estado de una alerta existente.
     * PUT /api/alerts/{id}/status
     * Cuerpo esperado: {"status": "RESOLVED"}
     *
     * @param id   Identificador de la alerta a actualizar.
     * @param body Mapa con la clave status y su nuevo valor.
     * @return 200 OK con el DTO de la alerta actualizada.
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<AlertResponseDTO> changeStatus(
            @PathVariable String id,
            @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(alertService.changeStatus(id, body.get("status")));
    }

    /**
     * Elimina una alerta de forma permanente.
     * DELETE /api/alerts/{id}
     *
     * @param id Identificador de la alerta a eliminar.
     * @return 204 No Content si la eliminacion fue exitosa.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        alertService.delete(id);
        return ResponseEntity.noContent().build();
    }
}