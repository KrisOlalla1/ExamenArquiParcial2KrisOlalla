# API Documentation - Branch Management (BanQuito)

## Información General

**Proyecto:** Gestión de Sucursales y Feriados - BanQuito  
**Framework:** Spring Boot 3.5.8  
**Base de Datos:** MongoDB  
**Puerto:** 8080

---

## Base URL
```
http://localhost:8080/api/branches_api/v1/branch
```

---

## Modelo de Datos

### Branch (Sucursal)
```json
{
  "id": "string",
  "emailAddress": "string",
  "name": "string",
  "phoneNumber": "string",
  "state": "ACTIVE",
  "creationDate": "2024-12-17T10:00:00",
  "lastModifiedDate": "2024-12-17T10:00:00",
  "branchHolidays": [
    {
      "date": "2024-12-25",
      "name": "Navidad"
    }
  ]
}
```

### BranchHoliday (Feriado)
```json
{
  "date": "2024-12-25",
  "name": "string"
}
```

---

## Endpoints

### Endpoint 1: Obtener todas las sucursales

| Método | URL |
|--------|-----|
| GET | `/api/branches_api/v1/branch` |

**Response:** `200 OK`
```json
[
  {
    "id": "675265a3b5e4f9001234abcd",
    "emailAddress": "centro@banquito.com",
    "name": "Sucursal Centro",
    "phoneNumber": "0991234567",
    "state": "ACTIVE",
    "creationDate": "2024-12-17T10:00:00",
    "lastModifiedDate": "2024-12-17T10:00:00",
    "branchHolidays": []
  }
]
```

**Logs:**
- INFO: `API: GET /api/branches_api/v1/branch - Fetching all branches`
- INFO: `API: Returning {count} branches`

---

### Endpoint 2: Crear una sucursal (sin feriados)

| Método | URL |
|--------|-----|
| POST | `/api/branches_api/v1/branch` |

**Headers:** `Content-Type: application/json`

**Request Body:**
```json
{
  "name": "Sucursal Centro",
  "emailAddress": "centro@banquito.com",
  "phoneNumber": "0991234567"
}
```

**Validaciones:**
| Campo | Regla |
|-------|-------|
| name | Requerido |
| emailAddress | Requerido, formato email válido |
| phoneNumber | Requerido, exactamente 10 dígitos |

**Response:** `201 Created`

**Logs:**
- INFO: `API: POST /api/branches_api/v1/branch - Creating new branch: {name}`
- INFO: `API: Branch created with ID: {id}`

---

### Endpoint 3: Obtener sucursal por ID

| Método | URL |
|--------|-----|
| GET | `/api/branches_api/v1/branch/{id}` |

**Path Parameters:**
| Parámetro | Descripción |
|-----------|-------------|
| id | ID de la sucursal |

**Response:** `200 OK`

**Error Response:** `404 Not Found`
```json
{
  "timestamp": "2024-12-17T10:00:00",
  "status": 404,
  "error": "Branch Not Found",
  "message": "Branch not found with ID: xyz",
  "path": "/api/branches_api/v1/branch/xyz"
}
```

**Logs:**
- INFO: `API: GET /api/branches_api/v1/branch/{id} - Fetching branch`
- INFO: `API: Branch found: {name}`
- ERROR: `Branch not found: {message}` (si no existe)

---

### Endpoint 4: Modificar teléfono de sucursal

| Método | URL |
|--------|-----|
| PATCH | `/api/branches_api/v1/branch/{id}/phone` |

**Headers:** `Content-Type: application/json`

**Path Parameters:**
| Parámetro | Descripción |
|-----------|-------------|
| id | ID de la sucursal |

**Request Body:**
```json
{
  "phoneNumber": "0999999999"
}
```

**Validaciones:**
| Campo | Regla |
|-------|-------|
| phoneNumber | Requerido, exactamente 10 dígitos |

**Response:** `200 OK` (Sucursal con `lastModifiedDate` actualizado)

**Logs:**
- INFO: `API: PATCH /api/branches_api/v1/branch/{id}/phone - Updating phone number`
- INFO: `API: Phone number updated for branch: {name}`

---

### Endpoint 5: Crear feriados para una sucursal

| Método | URL |
|--------|-----|
| POST | `/api/branches_api/v1/branch/{id}/holiday` |

**Headers:** `Content-Type: application/json`

**Path Parameters:**
| Parámetro | Descripción |
|-----------|-------------|
| id | ID de la sucursal |

**Request Body:**
```json
[
  {
    "date": "2024-12-25",
    "name": "Navidad"
  },
  {
    "date": "2024-01-01",
    "name": "Año Nuevo"
  }
]
```

**Validaciones:**
| Campo | Regla |
|-------|-------|
| date | Requerido (formato: YYYY-MM-DD) |
| name | Requerido |

**Response:** `201 Created`

**Logs:**
- INFO: `API: POST /api/branches_api/v1/branch/{id}/holiday - Adding {count} holidays`
- INFO: `API: Holidays added to branch: {name}`

---

### Endpoint 6: Eliminar feriado de una sucursal

| Método | URL |
|--------|-----|
| DELETE | `/api/branches_api/v1/branch/{id}/holiday/{date}` |

**Path Parameters:**
| Parámetro | Descripción |
|-----------|-------------|
| id | ID de la sucursal |
| date | Fecha del feriado (formato: YYYY-MM-DD) |

**Response:** `200 OK`

**Error Response:** `404 Not Found` (si el feriado no existe)

**Logs:**
- INFO: `API: DELETE /api/branches_api/v1/branch/{id}/holiday/{date} - Deleting holiday`
- INFO: `API: Holiday deleted from branch: {name}`
- ERROR: `Holiday not found: {message}` (si no existe)

---

### Endpoint 7: Obtener todos los feriados de una sucursal

| Método | URL |
|--------|-----|
| GET | `/api/branches_api/v1/branch/{id}/holiday` |

**Path Parameters:**
| Parámetro | Descripción |
|-----------|-------------|
| id | ID de la sucursal |

**Response:** `200 OK`
```json
[
  {
    "date": "2024-12-25",
    "name": "Navidad"
  }
]
```

**Logs:**
- INFO: `API: GET /api/branches_api/v1/branch/{id}/holiday - Fetching holidays`
- INFO: `API: Returning {count} holidays`

---

### Endpoint 8: Verificar si una fecha es feriado

| Método | URL |
|--------|-----|
| GET | `/api/branches_api/v1/branch/{id}/holiday/check?date=YYYY-MM-DD` |

**Path Parameters:**
| Parámetro | Descripción |
|-----------|-------------|
| id | ID de la sucursal |

**Query Parameters:**
| Parámetro | Descripción |
|-----------|-------------|
| date | Fecha a verificar (formato: YYYY-MM-DD) |

**Response:** `200 OK`

Si es feriado:
```json
{
  "branchId": "675265a3b5e4f9001234abcd",
  "date": "2024-12-25",
  "isHoliday": true,
  "holidayName": "Navidad"
}
```

Si no es feriado:
```json
{
  "branchId": "675265a3b5e4f9001234abcd",
  "date": "2024-12-26",
  "isHoliday": false,
  "holidayName": null
}
```

**Logs:**
- INFO: `API: GET /api/branches_api/v1/branch/{id}/holiday/check?date={date} - Checking if holiday`
- INFO: `API: Date {date} is {a holiday/not a holiday} for branch {id}`

---

## Manejo de Errores

Todas las respuestas de error siguen este formato:

```json
{
  "timestamp": "2024-12-17T10:00:00",
  "status": 400,
  "error": "Error Type",
  "message": "Mensaje detallado del error",
  "path": "/api/branches_api/v1/branch/..."
}
```

**Códigos de Estado HTTP:**
| Código | Descripción |
|--------|-------------|
| 200 OK | Operación GET/PATCH/DELETE exitosa |
| 201 Created | Operación POST exitosa |
| 400 Bad Request | Error de validación |
| 404 Not Found | Sucursal o feriado no encontrado |
| 500 Internal Server Error | Error inesperado |

---

## Estructura del Proyecto

```
src/main/java/com/examen/branches_api/
├── BranchesApiApplication.java     # Clase principal
├── model/                          # Entidades
│   ├── Branch.java
│   └── BranchHoliday.java
├── repository/                     # Acceso a datos
│   └── BranchRepository.java
├── dto/                            # Data Transfer Objects
│   ├── BranchRequest.java
│   ├── BranchResponse.java
│   ├── BranchHolidayRequest.java
│   ├── HolidayCheckResponse.java
│   └── PhoneUpdateRequest.java
├── exception/                      # Manejo de errores
│   ├── BranchNotFoundException.java
│   ├── HolidayNotFoundException.java
│   ├── ErrorResponse.java
│   └── GlobalExceptionHandler.java
├── service/                        # Lógica de negocio
│   └── BranchService.java
└── controller/                     # Endpoints REST
    └── BranchController.java
```

---

## Especificaciones Técnicas

| Característica | Implementación |
|----------------|----------------|
| Inyección de dependencias | Por constructor con `@AllArgsConstructor` |
| Campos de servicio | Declarados como `final` |
| Transacciones | `@Transactional` en servicios |
| Validaciones | Bean Validator (`@Valid`, `@NotBlank`, `@Email`, `@Pattern`) |
| Logging | `@Slf4j` en servicios y controladores |
| Paquetes | En singular (model, dto, exception, service, controller, repository) |
| Base de datos | MongoDB |
