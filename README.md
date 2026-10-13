# Servicio de Catálogo y Sincronización

**Alumno:** Valentín Barzola
**Materia:** Programación 2 — Proyecto Integrador 2026
**Universidad:** Universidad de Mendoza

---

## Descripción

Este repositorio contiene el **servicio de catálogo y sincronización**, uno de los dos backends del proyecto integrador. Es responsable de mantener una copia local actualizada del catálogo de categorías, profesionales y horarios semanales publicado por el servicio central de cátedra.

Expone endpoints de solo lectura para que el servicio de turnos (Repo 3) y la app Android (Repo 1) puedan consultar profesionales y sus horarios sin acceder directamente al servicio central.

## Responsabilidades

- Almacenar la copia local de categorías, profesionales y horarios semanales
- Ejecutar sincronización completa (snapshot REST) e incremental (Kafka + Redis)
- Detectar discontinuidades de versión y reconstruir el catálogo local
- Exponer búsqueda y filtros de profesionales resueltos 100% sobre datos locales
- Exponer horarios vigentes de un profesional para que Repo 3 valide disponibilidad

## Tecnologías

- Java 21 + Spring Boot 3 (JHipster 8.11.0)
- PostgreSQL (base de datos propia, esquema `catalogo`)
- Kafka (consumidor de eventos `CatalogUpdated` de cátedra)
- Redis (lectura del catálogo publicado por cátedra)
- Maven

## Requisitos previos

- Java 21
- Docker y Docker Compose
- Conexión a la red ZeroTier de cátedra
- Archivo `.env` con las credenciales de integración (ver `.env.example`)

## Configuración del entorno

Copiar el archivo de ejemplo y completar con los valores recibidos al registrar la cuenta técnica:

```bash
cp .env.example .env
# editar .env con los valores reales
```

## Levantar el servicio

```bash
# 1. Levantar la base de datos
docker compose up -d

# 2. Levantar el backend
./mvnw spring-boot:run
```

El servicio queda disponible en `http://localhost:8081`.

## Ejecutar los tests

```bash
./mvnw test
```

## Estructura del proyecto (Arquitectura Hexagonal)

```
src/main/java/com/vbarzola/catalogo/
│
├── domain/                    # Núcleo del dominio (sin dependencias de framework)
│   ├── model/                 #   Entidades de dominio (ProfessionalCategory, Professional, WeeklySchedule, CatalogSyncStatus)
│   ├── port/
│   │   ├── in/                #   Puertos de entrada — interfaces de casos de uso
│   │   └── out/               #   Puertos de salida — interfaces de repositorios
│   ├── service/               #   Servicios de dominio (implementan los puertos de entrada)
│   ├── dto/                   #   Objetos de transferencia de datos
│   └── mapper/                #   Conversión Entity ↔ DTO (MapStruct)
│
├── adapter/
│   ├── in/
│   │   ├── rest/              #   Controladores REST (implementan puertos de entrada vía HTTP)
│   │   └── kafka/             #   Consumidor Kafka (eventos CatalogUpdated de cátedra — Sprint 1)
│   └── out/
│       └── persistence/       #   Repositorios Spring Data JPA (implementan puertos de salida)
│
├── config/                    # Configuración de Spring Boot (seguridad, propiedades, etc.)
├── security/                  # Utilidades de seguridad JWT
├── broker/                    # Infraestructura Kafka generada por JHipster (SSE)
└── web/rest/                  # Endpoints de gestión de usuarios generados por JHipster
```
