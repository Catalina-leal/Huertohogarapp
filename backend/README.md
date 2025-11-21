# HuertoHogar API - Microservicio REST con Spring Boot

Microservicio REST desarrollado con Spring Boot para la aplicación HuertoHogar, siguiendo buenas prácticas de arquitectura en capas.

## 🏗️ Arquitectura

El proyecto sigue una arquitectura en capas (similar a MVVM del frontend):

```
┌─────────────────────────────────────┐
│      Controllers (REST API)          │
├─────────────────────────────────────┤
│         Services (Lógica)           │
├─────────────────────────────────────┤
│      Repositories (Datos)            │
├─────────────────────────────────────┤
│      Entities (Modelo)              │
└─────────────────────────────────────┘
```

## 📁 Estructura del Proyecto

```
backend/
├── src/main/java/com/huertohogar/
│   ├── config/              # Configuraciones (Security, Swagger, CORS)
│   ├── controller/          # REST Controllers
│   ├── exception/           # Manejo global de excepciones
│   ├── model/
│   │   ├── dto/             # Data Transfer Objects
│   │   └── entity/          # Entidades JPA
│   ├── repository/          # Repositorios JPA
│   ├── service/             # Lógica de negocio
│   └── util/                # Utilidades (JWT, etc.)
├── src/main/resources/
│   ├── application.yml      # Configuración desarrollo
│   └── application-prod.yml # Configuración producción
└── pom.xml                  # Dependencias Maven
```

## 🚀 Inicio Rápido

### Requisitos

- Java 17 o superior
- Maven 3.6+
- (Opcional) PostgreSQL para producción

### Instalación

1. **Clonar y navegar al directorio:**
```bash
cd backend
```

2. **Compilar el proyecto:**
```bash
mvn clean install
```

3. **Ejecutar la aplicación:**
```bash
mvn spring-boot:run
```

La API estará disponible en: `http://localhost:8080/api/v1`

### Base de Datos

**Desarrollo (H2 - In-Memory):**
- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:huertohogar`
- Usuario: `sa`
- Contraseña: (vacía)

**Producción (PostgreSQL):**
- Configurar en `application-prod.yml`
- Variables de entorno: `DB_USERNAME`, `DB_PASSWORD`

## 📚 Documentación API

### Swagger UI

Una vez iniciada la aplicación, accede a:
- **Swagger UI:** http://localhost:8080/api/v1/swagger-ui.html
- **API Docs (JSON):** http://localhost:8080/api/v1/api-docs

### Endpoints Principales

#### Autenticación
- `POST /api/v1/auth/register` - Registrar usuario
- `POST /api/v1/auth/login` - Iniciar sesión

#### Productos
- `GET /api/v1/products` - Listar todos
- `GET /api/v1/products/{id}` - Obtener por ID
- `GET /api/v1/products/category/{category}` - Por categoría
- `GET /api/v1/products/search?query=...` - Buscar
- `POST /api/v1/products` - Crear
- `PUT /api/v1/products/{id}` - Actualizar
- `DELETE /api/v1/products/{id}` - Eliminar

#### Pedidos
- `POST /api/v1/orders` - Crear pedido
- `GET /api/v1/orders/user/{email}` - Pedidos del usuario
- `GET /api/v1/orders/{orderId}` - Obtener pedido
- `PUT /api/v1/orders/{orderId}/status` - Actualizar estado

#### Reseñas
- `GET /api/v1/reviews/product/{productId}` - Reseñas de producto
- `POST /api/v1/reviews` - Crear reseña

#### Blog
- `GET /api/v1/blog/posts` - Listar posts
- `GET /api/v1/blog/posts/{id}` - Obtener post

#### Pagos
- `POST /api/v1/payments/process` - Procesar pago

## 🔐 Autenticación

La API usa JWT (JSON Web Tokens) para autenticación:

1. **Registro/Login:** Obtén el token
2. **Peticiones autenticadas:** Incluye el header:
   ```
   X-User-Email: usuario@email.com
   ```

**Nota:** En producción, implementar autenticación JWT completa con filtros de seguridad.

## 🧪 Testing

```bash
# Ejecutar tests
mvn test

# Ejecutar con cobertura
mvn test jacoco:report
```

## 📦 Dependencias Principales

- **Spring Boot 3.2.0** - Framework principal
- **Spring Data JPA** - Persistencia
- **H2 Database** - Base de datos en memoria (desarrollo)
- **PostgreSQL** - Base de datos producción
- **Swagger/OpenAPI** - Documentación API
- **JWT** - Autenticación
- **Lombok** - Reducción de boilerplate
- **Validation** - Validación de datos

## 🔄 Integración con Android

Para conectar la app Android:

1. **Actualizar `ApiClient.kt` en Android:**
```kotlin
private const val BASE_URL = "http://TU_IP:8080/api/v1/"
```

2. **Activar API en Android:**
```kotlin
fun isApiAvailable(): Boolean {
    return true  // Cambiar a true
}
```

3. **Ejecutar backend:**
```bash
mvn spring-boot:run
```

## 🚢 Despliegue

### Producción

1. **Configurar variables de entorno:**
```bash
export DB_USERNAME=huertohogar
export DB_PASSWORD=password_segura
export JWT_SECRET=secret_muy_largo_y_seguro
export PORT=8080
```

2. **Ejecutar con perfil de producción:**
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

3. **O crear JAR:**
```bash
mvn clean package
java -jar target/huertohogar-api-1.0.0.jar --spring.profiles.active=prod
```

## 📝 Notas

- **CORS:** Configurado para permitir todas las solicitudes (ajustar en producción)
- **Seguridad:** En producción, implementar autenticación JWT completa
- **Validaciones:** Todos los endpoints tienen validaciones de entrada
- **Manejo de errores:** Excepciones globales con respuestas consistentes

## 🐛 Troubleshooting

**Error de puerto ocupado:**
```bash
# Cambiar puerto en application.yml
server:
  port: 8081
```

**Error de base de datos:**
- Verificar que H2 esté en las dependencias
- Para PostgreSQL, verificar conexión y credenciales

## 📄 Licencia

Este proyecto es parte de HuertoHogar.

