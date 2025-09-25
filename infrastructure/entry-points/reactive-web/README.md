# API de Solicitudes de Préstamo - Endpoint de Solicitudes Pendientes

Este documento describe la implementación del endpoint para consultar solicitudes de préstamo pendientes para revisión de asesores.

## Endpoint

```
GET /api/v1/solicitudes
```

## Descripción

Obtiene una lista paginada y filtrable de todas las solicitudes de préstamo con estado "PENDIENTE", disponible únicamente para usuarios con rol de ASESOR.

## Autenticación y Autorización

- **Autenticación:** Requerida (Bearer Token)
- **Autorización:** Rol `ADVISOR` únicamente
- **Respuesta sin auth:** `401 Unauthorized`
- **Respuesta rol incorrecto:** `403 Forbidden`

## Parámetros de Query (Opcionales)

| Parámetro | Tipo | Rango/Valores | Por Defecto | Descripción |
|-----------|------|---------------|-------------|-------------|
| `pagina` | Integer | 1-10000 | 1 | Número de página a consultar |
| `cantidad` | Integer | 1-100 | 10 | Cantidad de elementos por página |
| `ordenar` | String | Ver campos válidos | id | Campo por el cual ordenar |
| `direccion` | String | ASC, DESC | ASC | Dirección del ordenamiento |

### Campos de Ordenamiento Válidos

- `id` - Identificador único de la solicitud
- `monto` - Monto solicitado del préstamo
- `plazoEnMeses` - Plazo en meses del préstamo
- `nombre` - Nombre completo del cliente
- `tipoPrestamo` - Tipo de préstamo
- `tasaInteres` - Tasa de interés del préstamo
- `estadoSolicitud` - Estado actual de la solicitud
- `salarioBase` - Salario base del cliente
- `montoMensualSolicitud` - Monto mensual calculado

## Respuesta Exitosa (200 OK)

```json
{
  "pagina": 1,
  "cantidad": 10,
  "total_elementos": 25,
  "total_paginas": 3,
  "items": [
    {
      "id": 1,
      "monto": 1000000,
      "plazo_en_meses": 12,
      "correo_electronico": "juan.perez@email.com",
      "nombre": "Juan Pérez",
      "tipo_prestamo": "Personal",
      "tasa_interes": 15.5,
      "estado_solicitud": "PENDIENTE",
      "salario_base": 3000000,
      "monto_mensual_solicitud": 91679.83
    }
  ]
}
```

## Respuestas de Error

### 400 Bad Request - Parámetros Inválidos
```json
{
  "error": "Parámetros inválidos",
  "mensaje": "El parámetro 'pagina' debe ser un número entero mayor a 0"
}
```

### 500 Internal Server Error
```json
{
  "error": "Error interno del servidor",
  "mensaje": "No fue posible obtener las solicitudes de préstamo en este momento. Intente nuevamente."
}
```

## Ejemplos de Uso

### Consulta básica
```bash
curl -H "Authorization: Bearer <token>" \
  "http://localhost:8080/api/v1/solicitudes"
```

### Consulta con paginación
```bash
curl -H "Authorization: Bearer <token>" \
  "http://localhost:8080/api/v1/solicitudes?pagina=2&cantidad=20"
```

### Consulta con ordenamiento
```bash
curl -H "Authorization: Bearer <token>" \
  "http://localhost:8080/api/v1/solicitudes?ordenar=monto&direccion=DESC"
```

### Consulta completa
```bash
curl -H "Authorization: Bearer <token>" \
  "http://localhost:8080/api/v1/solicitudes?pagina=1&cantidad=15&ordenar=tasaInteres&direccion=ASC"
```

## Implementación Técnica

### Arquitectura
- **Patrón:** Hexagonal (Clean Architecture)
- **Framework:** Spring WebFlux (Reactivo)
- **Paginación:** Implementada con PageFilter
- **Validación:** Robusta con mensajes detallados
- **Logging:** Trazas completas para monitoreo

### Validaciones Implementadas
- ✅ Página: 1-10000
- ✅ Cantidad: 1-100
- ✅ Dirección: ASC/DESC (case insensitive)
- ✅ Campo de ordenamiento: Solo campos permitidos
- ✅ Tipos de datos: Validación numérica
- ✅ Manejo de errores: Mensajes controlados

### Cálculo de Monto Mensual
El `monto_mensual_solicitud` se calcula utilizando la fórmula de amortización:

```
PMT = P * [r(1 + r)^n] / [(1 + r)^n - 1]
```

Donde:
- P = Principal (monto del préstamo)
- r = Tasa de interés mensual (tasa anual / 12 / 100)
- n = Número de pagos (plazo en meses)

Para préstamos con tasa de interés 0%, se calcula como: `principal / meses`

### Logs y Monitoreo
- Log de inicio de consulta
- Log de parámetros validados
- Log de resultados obtenidos
- Log de respuesta enviada
- Log detallado de errores

### Tests
- ✅ Tests unitarios para HandlerV1
- ✅ Tests de integración end-to-end
- ✅ Tests de validación de parámetros
- ✅ Tests de cálculo de monto mensual
- ✅ Tests de manejo de errores

## Consideraciones de Rendimiento

- **Paginación:** Límite máximo de 100 elementos por página
- **Cache:** Considerar implementar cache para consultas frecuentes
- **Índices DB:** Asegurar índices en campos de ordenamiento
- **Timeout:** Configurar timeouts apropiados para consultas grandes