# ms-campuslab-audit

Microservicio de auditoría de CampusLab.

## Tecnologías

- Java
- Spring Boot
- Spring Data JPA
- Oracle
- Kafka

## Funcionalidades

- Registrar eventos de reservas.
- Mantener timeline de acciones.
- Consultar trazabilidad.
- Consultar eventos asociados a una reserva.
- Filtrar eventos de auditoría.

## Endpoints

```http
GET /api/v1/audit/events
GET /api/v1/audit/bookings/{bookingId}
```

## Filtros

```http
GET /api/v1/audit/events?userId=
GET /api/v1/audit/events?type=
GET /api/v1/audit/events?from=&to=
```

## Acceso

Solo lectura.

## Roles

```text
ADMIN
AUDITOR
```

## Kafka

Consume:

```text
bookings.events
audit.timeline
```

## Base de datos

Oracle.

## Variables de entorno

```env
DB_URL=
DB_USERNAME=
DB_PASSWORD=
KAFKA_BOOTSTRAP_SERVERS=
```

## Ejecución local

```bash
./mvnw spring-boot:run
```

## Build

```bash
./mvnw clean package
```
