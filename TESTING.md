# Guía de Testing - HuertoHogar

Esta guía explica la estrategia de testing implementada en la aplicación HuertoHogar.

## 🎯 Estrategia de Testing

Implementamos una **estrategia híbrida** que combina:

1. **Pruebas Unitarias** - Lógica de negocio y ViewModels
2. **Pruebas de UI** - Comportamiento visual con Compose Test
3. **Pruebas de Integración** - Flujos completos
4. **Pruebas Asíncronas** - Flows y Coroutines

## 🛠️ Herramientas Utilizadas

### JUnit 5
- Framework principal para pruebas unitarias
- Soporte para pruebas parametrizadas y paralelas

### Kotest
- Assertions más expresivas y legibles
- Property-based testing
- Matchers avanzados

### MockK
- Mocking de dependencias
- Verificación de llamadas
- Stubbing de funciones suspend

### Compose UI Test
- Testing de componentes Compose
- Interacciones con la UI
- Verificación de estado visual

### kotlinx-coroutines-test
- Testing de código asíncrono
- TestDispatcher para control de tiempo
- Testing de Flows con Turbine

## 📁 Estructura de Tests

```
app/src/
├── test/                    # Pruebas unitarias (JVM)
│   └── java/com/huertohogar/
│       ├── presentation/    # Tests de ViewModels
│       ├── data/           # Tests de Repositories
│       └── util/           # Utilidades de testing
│
└── androidTest/            # Pruebas de integración y UI
    └── java/com/huertohogar/
        └── presentation/   # Tests de UI con Compose
```

## 🧪 Ejecutar Tests

### Todas las pruebas unitarias
```bash
./gradlew test
```

### Pruebas de UI
```bash
./gradlew connectedAndroidTest
```

### Prueba específica
```bash
./gradlew test --tests "HomeViewModelTest"
```

### Con cobertura
```bash
./gradlew test jacocoTestReport
```

## 📝 Ejemplos de Tests

### Test de ViewModel con MockK

```kotlin
@Test
fun `should emit success state with products`() = runTest {
    // Given
    val products = listOf(Product(...))
    coEvery { repository.getProducts() } returns flowOf(products)
    
    // When
    viewModel.loadProducts()
    
    // Then
    viewModel.uiState.test {
        val state = awaitItem()
        state.shouldBeInstanceOf<HomeUiState.Success>()
    }
}
```

### Test de UI con Compose

```kotlin
@Test
fun productCard_displaysProductName() {
    // Given
    val product = Product(...)
    
    // When
    composeTestRule.setContent {
        ProductCard(product = product)
    }
    
    // Then
    composeTestRule.onNodeWithText("Manzana").assertExists()
}
```

### Test de Flow con Turbine

```kotlin
@Test
fun `should emit cart items`() = runTest {
    repository.getCartItems().test {
        val items = awaitItem()
        items.size shouldBe 2
    }
}
```

## ✅ Mejores Prácticas

1. **Nombres descriptivos**: Usar backticks para nombres legibles
2. **AAA Pattern**: Arrange, Act, Assert
3. **Un test, una responsabilidad**: Cada test verifica una cosa
4. **Mocking estratégico**: Mock solo lo necesario
5. **Test data builders**: Crear helpers para datos de prueba
6. **Isolation**: Cada test es independiente

## 🔍 Cobertura de Código

Objetivo: **>80% de cobertura**

Verificar cobertura:
```bash
./gradlew test jacocoTestReport
```

El reporte estará en: `app/build/reports/jacoco/test/html/index.html`

## 🚀 CI/CD Integration

Los tests se ejecutan automáticamente en:
- Pre-commit hooks
- Pull requests
- Builds de CI/CD

## 📚 Recursos

- [JUnit 5 Documentation](https://junit.org/junit5/docs/current/user-guide/)
- [Kotest Documentation](https://kotest.io/)
- [MockK Documentation](https://mockk.io/)
- [Compose Testing](https://developer.android.com/jetpack/compose/testing)
- [Coroutines Testing](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-test/)

