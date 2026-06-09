package cl.municipality.msalerts.controller;

import cl.municipality.msalerts.dto.AlertRequestDTO;
import cl.municipality.msalerts.dto.AlertResponseDTO;
import cl.municipality.msalerts.exception.AlertNotFoundException;
import cl.municipality.msalerts.exception.GlobalExceptionHandler;
import cl.municipality.msalerts.service.AlertService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AlertController - pruebas de integración web")
class AlertControllerTest {

    @Mock
    private AlertService alertService;

    @InjectMocks
    private AlertController alertController;

    private MockMvc mockMvc;

    private final AlertResponseDTO mockResponse = new AlertResponseDTO(
            "abc123", "Incendio norte", "Fuego activo", "HIGH", "ACTIVE",
            LocalDateTime.now(), 1L, 2L);

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(alertController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("POST /api/alerts debería retornar 201 con la alerta creada")
    void create_retorna201() throws Exception {
        AlertRequestDTO request = new AlertRequestDTO("Incendio norte", "Fuego activo", "HIGH", 1L, 2L);
        when(alertService.create(any())).thenReturn(mockResponse);

        mockMvc.perform(post("/api/alerts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Incendio norte\",\"description\":\"Fuego activo\",\"severity\":\"HIGH\",\"reportId\":1,\"userId\":2}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("abc123"))
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andExpect(jsonPath("$.severity").value("HIGH"));
    }

    @Test
    @DisplayName("POST /api/alerts debería retornar 400 si el título está vacío")
    void create_retorna400SiTituloVacio() throws Exception {
        AlertRequestDTO request = new AlertRequestDTO("", "Desc", "HIGH", null, null);

        mockMvc.perform(post("/api/alerts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"\",\"description\":\"Desc\",\"severity\":\"HIGH\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /api/alerts debería retornar 200 con alertas activas")
    void listActive_retorna200() throws Exception {
        when(alertService.listActive()).thenReturn(List.of(mockResponse));

        mockMvc.perform(get("/api/alerts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("abc123"))
                .andExpect(jsonPath("$[0].status").value("ACTIVE"));
    }

    @Test
    @DisplayName("GET /api/alerts debería retornar 200 con lista vacía si no hay activas")
    void listActive_retornaVacio() throws Exception {
        when(alertService.listActive()).thenReturn(List.of());

        mockMvc.perform(get("/api/alerts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @DisplayName("GET /api/alerts/history debería retornar 200 con historial completo")
    void listAll_retorna200() throws Exception {
        when(alertService.listAll()).thenReturn(List.of(mockResponse));

        mockMvc.perform(get("/api/alerts/history"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("abc123"));
    }

    @Test
    @DisplayName("GET /api/alerts/{id} debería retornar 200 con la alerta encontrada")
    void findById_retorna200() throws Exception {
        when(alertService.findById("abc123")).thenReturn(mockResponse);

        mockMvc.perform(get("/api/alerts/abc123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("abc123"))
                .andExpect(jsonPath("$.title").value("Incendio norte"));
    }

    @Test
    @DisplayName("GET /api/alerts/{id} debería retornar 404 si no existe")
    void findById_retorna404SiNoExiste() throws Exception {
        when(alertService.findById("noexiste"))
                .thenThrow(new AlertNotFoundException("noexiste"));

        mockMvc.perform(get("/api/alerts/noexiste"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    @DisplayName("PUT /api/alerts/{id}/status debería retornar 200 con estado actualizado")
    void changeStatus_retorna200() throws Exception {
        AlertResponseDTO resuelto = new AlertResponseDTO(
                "abc123", "Incendio", "Desc", "HIGH", "RESOLVED",
                LocalDateTime.now(), 1L, 2L);
        when(alertService.changeStatus("abc123", "RESOLVED")).thenReturn(resuelto);

        mockMvc.perform(put("/api/alerts/abc123/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"RESOLVED\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("RESOLVED"));
    }

    @Test
    @DisplayName("PUT /api/alerts/{id}/status debería retornar 400 si el status es inválido")
    void changeStatus_retorna400SiStatusInvalido() throws Exception {
        when(alertService.changeStatus("abc123", "CERRADO"))
                .thenThrow(new IllegalArgumentException("Estado invalido: 'CERRADO'"));

        mockMvc.perform(put("/api/alerts/abc123/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"CERRADO\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    @DisplayName("DELETE /api/alerts/{id} debería retornar 204 si la alerta existe")
    void delete_retorna204() throws Exception {
        doNothing().when(alertService).delete("abc123");

        mockMvc.perform(delete("/api/alerts/abc123"))
                .andExpect(status().isNoContent());

        verify(alertService).delete("abc123");
    }

    @Test
    @DisplayName("DELETE /api/alerts/{id} debería retornar 404 si no existe")
    void delete_retorna404SiNoExiste() throws Exception {
        doThrow(new AlertNotFoundException("noexiste")).when(alertService).delete("noexiste");

        mockMvc.perform(delete("/api/alerts/noexiste"))
                .andExpect(status().isNotFound());
    }
}