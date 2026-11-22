# ms-campuslab-audit

Microservicio de auditoría de CampusLab. Persiste eventos de reserva en Oracle
y expone consultas de solo lectura para el BFF.

## Endpoints internos

| Método | Ruta | Parámetros | Respuesta |
|---|---|---|---|
| `GET` | `/api/v1/audit/events` | `userId`, `type`, `from`, `to` (opcionales) | Lista de eventos |
| `GET` | `/api/v1/audit/bookings/{bookingId}` | `bookingId` en la ruta | Timeline de eventos de la reserva |

`from` y `to` son fechas-horas locales ISO-8601. Los contratos de respuestas
están en [`openapi.yaml`](./openapi.yaml).

## Autenticación y autorización

El servicio Audit no implementa autenticación ni autorización por roles. El
control JWT y de roles de las rutas públicas se aplica en `ms-campuslab-bff`
(detrás del API Gateway). Las rutas de este servicio son internas y no deben
confundirse con una protección propia del microservicio.

## Integración Kafka

- Audit consume `bookings.events` con el consumer group `campuslab-audit`.
- Al procesar un evento de reserva, publica un `AuditEvent` en `audit.timeline`.
- La clave Kafka de los eventos publicados es el `eventId` (con la política
  `compact,delete` de `audit.timeline`, una clave única por evento evita que la
  compactación elimine el historial de una reserva).
- `audit.timeline` es consumido por `ms-campuslab-report` con el grupo `campuslab-report`.
- El consumidor usa 3 hilos (uno por partición de `bookings.events`). Si un mensaje
  falla tras 2 reintentos, o su payload es inválido, se publica en `bookings.events.DLT`
  y el consumidor continúa con el siguiente.
- El productor usa `acks=all` e idempotencia (`enable.idempotence=true`).

Los nombres y DTO se configuran en `src/main/resources/application.properties`
y `src/main/java/ms_campuslab_audit/messaging/kafka`.

## Base de datos y configuración

La persistencia usa Oracle. Variables:

```env
DB_URL=
DB_USERNAME=
DB_PASSWORD=
KAFKA_BOOTSTRAP_SERVERS=
```

## Ejecución y pruebas locales

```bash
./mvnw spring-boot:run
./mvnw clean package
```

La suite predeterminada no requiere Oracle. La prueba de integración del
contexto es opt-in y requiere una base Oracle disponible:

```bash
./mvnw -DRUN_AUDIT_ORACLE_INTEGRATION_TESTS=true test
```
