# Changelog

Todos los cambios relevantes de este repositorio se documentan aquí.
El formato sigue [Keep a Changelog](https://keepachangelog.com/es-ES/1.1.0/) y el versionamiento sigue [SemVer](https://semver.org/lang/es/).

## [Sin publicar]

- Dead Letter Topic (`bookings.events.DLT`) con reintentos acotados y errores en el log; un mensaje defectuoso ya no bloquea la partición.
- La clave de `audit.timeline` pasa a ser el `eventId`.
- Consumidor con concurrencia 3 y productor idempotente.

## [1.0.0] - 2026-11-22

Primera versión estable.

- Timeline de auditoría de reservas (solo lectura).
- Consume `bookings.events` (grupo `campuslab-audit`) y publica en `audit.timeline`.
- API versionada en `/api/v1/audit`.
