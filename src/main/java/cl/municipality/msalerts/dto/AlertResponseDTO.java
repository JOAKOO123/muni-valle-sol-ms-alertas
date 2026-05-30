package cl.municipality.msalerts.dto;

import java.time.LocalDateTime;

public record AlertResponseDTO(
    String id,
    String title,
    String description,
    String severity,
    String status,
    LocalDateTime date,
    Long reportId,
    Long userId
) {}
