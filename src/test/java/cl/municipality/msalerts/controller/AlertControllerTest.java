package cl.municipality.msalerts.controller;

import cl.municipality.msalerts.dto.AlertRequestDTO;
import cl.municipality.msalerts.dto.AlertResponseDTO;
import cl.municipality.msalerts.exception.AlertNotFoundException;
import cl.municipality.msalerts.exception.GlobalExceptionHandler;
import cl.municipality.msalerts.service.AlertServicePort;
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

/**
 * Pruebas de integracion web para {@link AlertController}.
 * Verifica el comportamiento HTTP de cada endpoint utilizando MockMvc
 * con el servicio mockeado a traves de la interfaz {@link AlertServicePort}.
 *
 * <p>Patrones aplicados:</p>
 * <ul>
 *   <li>Arrange-Act-Assert: estructura clara en cada caso de prueba</li>
 *   <li>Dependency Inversion: se mockea la interfaz, no la implementacion</li>
 * </ul>
 *
 * @author Beltran
 * @version 1.1
 * @since 1.0
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("AlertController - pruebas de integración web")
class AlertControllerTest {

    /** Mock de la interfaz de servicio inyectado en el controlador. */
    @Mock
    private AlertServicePort alertService;

    /** Controlador bajo prueba con dependencias mockeadas. */
    @InjectMocks
    private AlertController alertController;

    /** Cliente HTTP simulado para ejecutar las peticiones. */
    private MockMvc mockMvc;

    /** DTO de respuesta reutilizado en multiples pruebas. */
    private final AlertResponseDTO mockResponse = new AlertResponseDTO(
            "abc123", "Incendio norte", "Fuego activo", "HIGH", "ACTIVE",
            LocalDateTime.now(), 1L, 2L, null, null);

    /**
     * Configura MockMvc con el controlador y el manejador global de excepciones
     * antes de cada prueba.
     */
    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(alertController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    /**
     * Verifica que POST /api/alerts retorna 201 con la alerta creada.
     *
     * @throws Exception si falla la ejecucion de MockMvc.
     */
    @Test
    @DisplayName("POST /api/alerts debería retornar 201 con la alerta creada")
    void create_retorna201() throws Exception {
        when(alertService.create(any())).thenReturn(mockResponse);

        mockMvc.perform(post("/api/alerts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Incendio norte\",\"description\":\"Fuego activo\",\"severity\":\"HIGH\",\"reportId\":1,\"userId\":2}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("abc123"))
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andExpect(jsonPath("$.severity").value("HIGH"));
    }

    /**
     * Verifica que POST /api/alerts retorna 400 cuando el titulo esta vacio.
     *
     * @throws Exception si falla la ejecucion de MockMvc.
     */
    @Test
    @DisplayName("POST /api/alerts debería retornar 400 si el título está vacío")
    void create_retorna400SiTituloVacio() throws Exception {
        mockMvc.perform(post("/api/alerts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"\",\"description\":\"Desc\",\"severity\":\"HIGH\"}"))
                .andExpect(status().isBadRequest());
    }

    /**
     * Verifica que GET /api/alerts retorna 200 con la lista de alertas activas.
     *
     * @throws Exception si falla la ejecucion de MockMvc.
     */
    @Test
    @DisplayName("GET /api/alerts debería retornar 200 con alertas activas")
    void listActive_retorna200() throws Exception {
        when(alertService.listActive()).thenReturn(List.of(mockResponse));

        mockMvc.perform(get("/api/alerts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("abc123"))
                .andExpect(jsonPath("$[0].status").value("ACTIVE"));
    }

    /**
     * Verifica que GET /api/alerts retorna 200 con lista vacia si no hay activas.
     *
     * @throws Exception si falla la ejecucion de MockMvc.
     */
    @Test
    @DisplayName("GET /api/alerts debería retornar 200 con lista vacía si no hay activas")
    void listActive_retornaVacio() throws Exception {
        when(alertService.listActive()).thenReturn(List.of());

        mockMvc.perform(get("/api/alerts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    /**
     * Verifica que GET /api/alerts/history retorna 200 con el historial completo.
     *
     * @throws Exception si falla la ejecucion de MockMvc.
     */
    @Test
    @DisplayName("GET /api/alerts/history debería retornar 200 con historial completo")
    void listAll_retorna200() throws Exception {
        when(alertService.listAll()).thenReturn(List.of(mockResponse));

        mockMvc.perform(get("/api/alerts/history"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("abc123"));
    }

    /**
     * Verifica que GET /api/alerts/{id} retorna 200 con la alerta encontrada.
     *
     * @throws Exception si falla la ejecucion de MockMvc.
     */
    @Test
    @DisplayName("GET /api/alerts/{id} debería retornar 200 con la alerta encontrada")
    void findById_retorna200() throws Exception {
        when(alertService.findById("abc123")).thenReturn(mockResponse);

        mockMvc.perform(get("/api/alerts/abc123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("abc123"))
                .andExpect(jsonPath("$.title").value("Incendio norte"));
    }

    /**
     * Verifica que GET /api/alerts/{id} retorna 404 cuando la alerta no existe.
     *
     * @throws Exception si falla la ejecucion de MockMvc.
     */
    @Test
    @DisplayName("GET /api/alerts/{id} debería retornar 404 si no existe")
    void findById_retorna404SiNoExiste() throws Exception {
        when(alertService.findById("noexiste"))
                .thenThrow(new AlertNotFoundException("noexiste"));

        mockMvc.perform(get("/api/alerts/noexiste"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    /**
     * Verifica que PUT /api/alerts/{id}/status retorna 200 con el estado actualizado.
     *
     * @throws Exception si falla la ejecucion de MockMvc.
     */
    @Test
    @DisplayName("PUT /api/alerts/{id}/status debería retornar 200 con estado actualizado")
    void changeStatus_retorna200() throws Exception {
        AlertResponseDTO resuelto = new AlertResponseDTO(
                "abc123", "Incendio", "Desc", "HIGH", "RESOLVED",
                LocalDateTime.now(), 1L, 2L, null, null);
        when(alertService.changeStatus("abc123", "RESOLVED")).thenReturn(resuelto);

        mockMvc.perform(put("/api/alerts/abc123/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"RESOLVED\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("RESOLVED"));
    }

    /**
     * Verifica que PUT /api/alerts/{id}/status retorna 400 si el status es invalido.
     *
     * @throws Exception si falla la ejecucion de MockMvc.
     */
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

    /**
     * Verifica que DELETE /api/alerts/{id} retorna 204 si la alerta existe.
     *
     * @throws Exception si falla la ejecucion de MockMvc.
     */
    @Test
    @DisplayName("DELETE /api/alerts/{id} debería retornar 204 si la alerta existe")
    void delete_retorna204() throws Exception {
        doNothing().when(alertService).delete("abc123");

        mockMvc.perform(delete("/api/alerts/abc123"))
                .andExpect(status().isNoContent());

        verify(alertService).delete("abc123");
    }

    /**
     * Verifica que DELETE /api/alerts/{id} retorna 404 si la alerta no existe.
     *
     * @throws Exception si falla la ejecucion de MockMvc.
     */
    @Test
    @DisplayName("DELETE /api/alerts/{id} debería retornar 404 si no existe")
    void delete_retorna404SiNoExiste() throws Exception {
        doThrow(new AlertNotFoundException("noexiste")).when(alertService).delete("noexiste");

        mockMvc.perform(delete("/api/alerts/noexiste"))
                .andExpect(status().isNotFound());
    }
}