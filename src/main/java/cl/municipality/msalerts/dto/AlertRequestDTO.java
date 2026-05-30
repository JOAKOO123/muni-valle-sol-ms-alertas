package cl.municipality.msalerts.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AlertRequestDTO(
    @NotBlank String title,
    @NotBlank String description,
    @NotNull String severity,
    Long reportId,
    Long userId
) {}
