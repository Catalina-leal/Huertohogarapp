# Integración Backend con Android

## Pasos para conectar la app Android con el backend Spring Boot

### 1. Iniciar el Backend

```bash
cd backend
mvn spring-boot:run
```

El servidor estará disponible en: `http://localhost:8080/api/v1`

### 2. Configurar la URL en Android

**Opción A: Android Emulator**
- El emulator usa `10.0.2.2` para referirse al `localhost` de tu máquina
- En `ApiClient.kt` ya está configurado: `http://10.0.2.2:8080/api/v1/`

**Opción B: Dispositivo Físico**
1. Encuentra tu IP local:
   ```bash
   # Windows
   ipconfig
   
   # Mac/Linux
   ifconfig
   ```
2. Actualiza `ApiClient.kt`:
   ```kotlin
   private const val BASE_URL = "http://TU_IP:8080/api/v1/"
   ```
3. Asegúrate de que el dispositivo y la computadora estén en la misma red WiFi

**Opción C: Producción**
```kotlin
private const val BASE_URL = "https://api.huertohogar.cl/api/v1/"
```

### 3. Activar la API en Android

En `ApiClient.kt`, asegúrate de que:
```kotlin
fun isApiAvailable(): Boolean {
    return true  // ← Debe ser true
}
```

### 4. Verificar Conexión

1. Inicia el backend Spring Boot
2. Ejecuta la app Android
3. Verifica los logs del backend para ver las peticiones

### 5. Endpoints Disponibles

Todos los endpoints están documentados en Swagger:
- **Swagger UI:** http://localhost:8080/api/v1/swagger-ui.html

### 6. Autenticación

Actualmente, la API usa un header simple:
```
X-User-Email: usuario@email.com
```

En producción, implementar JWT completo con tokens en el header `Authorization`.

### 7. Testing

**Probar con Postman/curl:**

```bash
# Obtener productos
curl http://localhost:8080/api/v1/products

# Registrar usuario
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@test.com",
    "password": "password123",
    "fullName": "Test User"
  }'

# Login
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@test.com",
    "password": "password123"
  }'
```

### 8. Troubleshooting

**Error de conexión:**
- Verifica que el backend esté corriendo
- Verifica la URL en `ApiClient.kt`
- Verifica que el puerto 8080 esté libre
- Para dispositivo físico, verifica que estén en la misma red

**CORS errors:**
- El backend ya tiene CORS configurado para permitir todas las solicitudes
- Si hay problemas, verifica `SecurityConfig.java`

**Timeout:**
- Aumenta el timeout en `ApiClient.kt`:
  ```kotlin
  .connectTimeout(60, TimeUnit.SECONDS)
  ```

### 9. Datos Iniciales

El backend carga automáticamente productos iniciales al iniciar (ver `DataInitializer.java`).

### 10. Sincronización

La app Android tiene fallback automático:
- Si la API está disponible → usa datos del servidor
- Si la API falla → usa datos locales (Room)

Esto permite trabajar offline y sincronizar cuando hay conexión.

