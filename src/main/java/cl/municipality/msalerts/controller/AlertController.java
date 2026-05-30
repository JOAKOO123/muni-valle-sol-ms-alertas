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

@RestController
@RequestMapping("/api/alerts")
@RequiredArgsConstructor
public class AlertController {

    private final AlertService alertService;

    @PostMapping
    public ResponseEntity<AlertResponseDTO> create(@Valid @RequestBody AlertRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(alertService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<AlertResponseDTO>> listActive() {
        return ResponseEntity.ok(alertService.listActive());
    }

    @GetMapping("/history")
    public ResponseEntity<List<AlertResponseDTO>> listAll() {
        return ResponseEntity.ok(alertService.listAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlertResponseDTO> findById(@PathVariable String id) {
        return ResponseEntity.ok(alertService.findById(id));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<AlertResponseDTO> changeStatus(
            @PathVariable String id,
            @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(alertService.changeStatus(id, body.get("status")));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        alertService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
