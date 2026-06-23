# MS-Alertas

Microservicio de gestión de alertas de emergencia para la Municipalidad Valle del Sol. Permite crear, consultar y gestionar alertas con severidad y estado, usando MongoDB Atlas como base de datos documental.

## Tabla Técnica

| Ítem | Detalle |
|---|---|
| Lenguaje | Java 21 |
| Framework | Spring Boot 3.4.1 |
| Base de datos | MongoDB Atlas (NoSQL documental) |
| ORM | Spring Data MongoDB |
| Seguridad | Sin autenticación propia (delegada al BFF) |
| Documentación API | SpringDoc OpenAPI 2.8.3 (Swagger UI) |
| Testing | JUnit 5 + Mockito + JaCoCo |
| Cobertura | 82% |
| Puerto | 8083 |
| Patrones de diseño | Repository Pattern, Factory Method (AlertFactory), Singleton (Spring Beans) |

## Librerías principales

- `spring-boot-starter-data-mongodb`
- `spring-boot-starter-validation`
- `spring-boot-starter-actuator`
- `spring-cloud-starter-circuitbreaker-resilience4j`
- `lombok`
- `springdoc-openapi-starter-webmvc-ui:2.8.3`
- `jacoco-maven-plugin:0.8.14`

## Requisitos

- Java 21
- Maven (incluido con `./mvnw`)
- Conexión a MongoDB Atlas

## Instalación y ejecución

```bash
# Clonar el repositorio
git clone https://github.com/JOAKOO123/muni-valle-sol-ms-alertas.git
cd muni-valle-sol-ms-alertas

# Ejecutar
./mvnw clean spring-boot:run
```

El servicio estará disponible en `http://localhost:8083`

## Variables de entorno

| Variable | Descripción |
|---|---|
| `SPRING_DATA_MONGODB_URI` | URI de conexión a MongoDB Atlas |

## Endpoints principales

| Método | Ruta | Descripción |
|---|---|---|
| POST | `/api/v1/api/alerts` | Crear nueva alerta |
| GET | `/api/v1/api/alerts` | Listar todas las alertas |
| GET | `/api/v1/api/alerts/{id}` | Obtener alerta por ID |
| PATCH | `/api/v1/api/alerts/{id}/status` | Cambiar estado de alerta |
| DELETE | `/api/v1/api/alerts/{id}` | Eliminar alerta |

## Niveles de severidad

`HIGH` · `MEDIUM` · `LOW`

## Estados

`ACTIVE` · `RESOLVED`

## Swagger UI

```
http://localhost:8083/swagger-ui.html
```

## Ejecutar pruebas y cobertura

```bash
# Ejecutar tests
./mvnw test

# Generar reporte de cobertura JaCoCo
./mvnw clean verify

# Ver reporte en:
# target/site/jacoco/index.html
```

## Docker

```bash
# Build
docker build -t ms-alertas .

# Run
docker run -p 8083:8083 \
  -e SPRING_DATA_MONGODB_URI="mongodb+srv://..." \
  ms-alertas
```