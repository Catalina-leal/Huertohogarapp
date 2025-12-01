package com.huertohogar.data.repository

import com.huertohogar.data.model.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Repositorio de la pantalla Home.
 * aca manejamos productos, carrito y los testimonios.
 */
class HomeRepository(
    private val cartDao: CartDao,
    private val productDao: ProductDao
) {
    // Productos segun las especificaciones del proyecto
    private val initialProducts = listOf(
        // Frutas Frescas
        Product(
            id = "FR001",
            name = "Manzanas Fuji",
            description = "Manzanas Fuji crujientes y dulces, cultivadas en el Valle del Maule. Perfectas para meriendas saludables o como ingrediente en postres. Estas manzanas son conocidas por su textura firme y su sabor equilibrado entre dulce y ácido.",
            price = 1200.0,
            oldPrice = null,
            stock = 150.0,
            category = ProductCategory.FRUTAS_FRESCAS,
            imageUrlName = "manzanas_fuji",
            tag = "Frescos",
            origin = "Valle del Maule",
            unit = "kg",
            isOrganic = false,
            isActive = true
        ),
        Product(
            id = "FR002",
            name = "Naranjas Valencia",
            description = "Jugosas y ricas en vitamina C, estas naranjas Valencia son ideales para zumos frescos y refrescantes. Cultivadas en condiciones climáticas óptimas que aseguran su dulzura y jugosidad.",
            price = 1000.0,
            oldPrice = null,
            stock = 200.0,
            category = ProductCategory.FRUTAS_FRESCAS,
            imageUrlName = "naranjas",
            tag = "Frescos",
            origin = "Región de Valparaíso",
            unit = "kg",
            isOrganic = false,
            isActive = true
        ),
        Product(
            id = "FR003",
            name = "Plátanos Cavendish",
            description = "Plátanos maduros y dulces, perfectos para el desayuno o como snack energético. Estos plátanos son ricos en potasio y vitaminas, ideales para mantener una dieta equilibrada.",
            price = 800.0,
            oldPrice = null,
            stock = 250.0,
            category = ProductCategory.FRUTAS_FRESCAS,
            imageUrlName = "bananas",
            tag = "Frescos",
            origin = "Región de O'Higgins",
            unit = "kg",
            isOrganic = false,
            isActive = true
        ),
        // Verduras Orgánicas
        Product(
            id = "VR001",
            name = "Zanahorias Orgánicas",
            description = "Zanahorias crujientes cultivadas sin pesticidas en la Región de O'Higgins. Excelente fuente de vitamina A y fibra, ideales para ensaladas, jugos o como snack saludable.",
            price = 900.0,
            oldPrice = null,
            stock = 100.0,
            category = ProductCategory.VERDURAS_ORGANICAS,
            imageUrlName = "zanahorias_1024x683",
            tag = "Orgánicas",
            origin = "Región de O'Higgins",
            unit = "kg",
            isOrganic = true,
            certifications = "Certificación Orgánica",
            sustainablePractices = "Cultivo sin pesticidas, rotación de cultivos",
            isActive = true
        ),
        Product(
            id = "VR002",
            name = "Espinacas Frescas",
            description = "Espinacas frescas y nutritivas, perfectas para ensaladas y batidos verdes. Estas espinacas son cultivadas bajo prácticas orgánicas que garantizan su calidad y valor nutricional.",
            price = 700.0,
            oldPrice = null,
            stock = 80.0,
            category = ProductCategory.VERDURAS_ORGANICAS,
            imageUrlName = "espinaca_americana",
            tag = "Orgánicas",
            origin = "Región Metropolitana",
            unit = "bolsa",
            isOrganic = true,
            certifications = "Certificación Orgánica"
        ),
        Product(
            id = "VR003",
            name = "Pimientos Tricolores",
            description = "Pimientos rojos, amarillos y verdes, ideales para salteados y platos coloridos. Ricos en antioxidantes y vitaminas, estos pimientos añaden un toque vibrante y saludable a cualquier receta.",
            price = 1500.0,
            oldPrice = null,
            stock = 120.0,
            category = ProductCategory.VERDURAS_ORGANICAS,
            imageUrlName = "pimiento_tricolor",
            tag = "Frescos",
            origin = "Región de Valparaíso",
            unit = "kg",
            isOrganic = false,
            isActive = true
        ),
        // Productos Orgánicos
        Product(
            id = "PO001",
            name = "Miel Orgánica",
            description = "Miel pura y orgánica producida por apicultores locales. Rica en antioxidantes y con un sabor inigualable, perfecta para endulzar de manera natural tus comidas y bebidas.",
            price = 5000.0,
            oldPrice = null,
            stock = 50.0,
            category = ProductCategory.PRODUCTOS_ORGANICOS,
            imageUrlName = "miel_organica",
            tag = "Orgánico",
            origin = "Región de Los Lagos",
            unit = "frasco",
            isOrganic = true,
            certifications = "Certificación Orgánica",
            sustainablePractices = "Apicultura sostenible, sin químicos"
        ),
        Product(
            id = "PO003",
            name = "Quinua Orgánica",
            description = "Quinua orgánica de alta calidad, rica en proteínas y nutrientes esenciales. Perfecta para una alimentación saludable y sostenible.",
            price = 3500.0,
            oldPrice = null,
            stock = 75.0,
            category = ProductCategory.PRODUCTOS_ORGANICOS,
            imageUrlName = "product_2",
            tag = "Orgánico",
            origin = "Región de Tarapacá",
            unit = "kg",
            isOrganic = true,
            certifications = "Certificación Orgánica"
        ),
        // Productos Lácteos
        Product(
            id = "PL001",
            name = "Leche Entera",
            description = "Leche entera fresca proveniente de granjas locales que se dedican a la producción responsable y de calidad. Rica en calcio y nutrientes esenciales.",
            price = 1000.0,
            oldPrice = null,
            stock = 200.0,
            category = ProductCategory.PRODUCTOS_LACTEOS,
            imageUrlName = "leche",
            tag = "Fresco",
            origin = "Región de Los Lagos",
            unit = "lt",
            isOrganic = false,
            isActive = true
        ),
        // algunos productos adicionales con imagenes disponibles
        Product(
            id = "FR004",
            name = "Kiwis",
            description = "Kiwis frescos y dulces, ricos en vitamina C y antioxidantes. Perfectos para el desayuno o como snack saludable.",
            price = 2500.0,
            oldPrice = null,
            stock = 60.0,
            category = ProductCategory.FRUTAS_FRESCAS,
            imageUrlName = "kiwi",
            tag = "Frescos",
            origin = "Región del Maule",
            unit = "kg",
            isOrganic = false,
            isActive = true
        ),
        Product(
            id = "FR005",
            name = "Peras",
            description = "Peras jugosas y dulces, ideales para consumo fresco o en postres. Excelente fuente de fibra y vitaminas.",
            price = 1300.0,
            oldPrice = null,
            stock = 90.0,
            category = ProductCategory.FRUTAS_FRESCAS,
            imageUrlName = "peras",
            tag = "Frescos",
            origin = "Región Metropolitana",
            unit = "kg",
            isOrganic = false,
            isActive = true
        ),
        Product(
            id = "FR006",
            name = "Uvas",
            description = "Uvas frescas y dulces, perfectas para el consumo directo o para hacer vino casero. Ricas en antioxidantes.",
            price = 1800.0,
            oldPrice = null,
            stock = 70.0,
            category = ProductCategory.FRUTAS_FRESCAS,
            imageUrlName = "uvas",
            tag = "Frescos",
            origin = "Valle Central",
            unit = "kg",
            isOrganic = false,
            isActive = true
        ),
        Product(
            id = "PL002",
            name = "Queso",
            description = "Queso fresco de producción local, rico en calcio y proteínas. Perfecto para acompañar tus comidas.",
            price = 3500.0,
            oldPrice = null,
            stock = 40.0,
            category = ProductCategory.PRODUCTOS_LACTEOS,
            imageUrlName = "queso",
            tag = "Fresco",
            origin = "Región de Los Lagos",
            unit = "kg",
            isOrganic = false,
            isActive = true
        ),
        Product(
            id = "PL003",
            name = "Mantequilla",
            description = "Mantequilla natural de producción artesanal, sin conservantes. Ideal para cocinar y untar.",
            price = 2800.0,
            oldPrice = null,
            stock = 50.0,
            category = ProductCategory.PRODUCTOS_LACTEOS,
            imageUrlName = "mantequilla",
            tag = "Fresco",
            origin = "Región de Los Lagos",
            unit = "unidad",
            isOrganic = false,
            isActive = true
        ),
        Product(
            id = "PL004",
            name = "Yogurt Natural",
            description = "Yogurt natural cremoso, rico en probióticos. Perfecto para el desayuno o como snack saludable.",
            price = 1200.0,
            oldPrice = null,
            stock = 80.0,
            category = ProductCategory.PRODUCTOS_LACTEOS,
            imageUrlName = "jogurt",
            tag = "Fresco",
            origin = "Región Metropolitana",
            unit = "unidad",
            isOrganic = false,
            isActive = true
        ),
        Product(
            id = "PO002",
            name = "Chocolate Orgánico",
            description = "Chocolate orgánico de alta calidad, hecho con cacao puro. Rico en antioxidantes y sin azúcares refinados.",
            price = 4500.0,
            oldPrice = 5000.0,
            stock = 30.0,
            category = ProductCategory.PRODUCTOS_ORGANICOS,
            imageUrlName = "chocolate",
            tag = "Orgánico",
            origin = "Región de Valparaíso",
            unit = "unidad",
            isOrganic = true,
            certifications = "Certificación Orgánica"
        )
    )

    private val simulatedTestimonials = listOf(
        Testimonial(
            quote = "Mi pareja me metió en el rollo de los productos orgánicos y ahora no puedo vivir sin ellos. La calidad es excepcional.",
            name = "Sara",
            profession = "Arquitecta",
            imageUrlName = "testimonial_1"
        ),
        Testimonial(
            quote = "A veces compro productos orgánicos solo porque la calidad del procesamiento es mejor. HuertoHogar nunca me decepciona.",
            name = "Luis",
            profession = "Electricista",
            imageUrlName = "testimonial_2"
        ),
        Testimonial(
            quote = "Los productos llegan frescos y deliciosos. Me encanta saber que estoy apoyando a agricultores locales.",
            name = "María",
            profession = "Nutricionista",
            imageUrlName = "testimonial_3"
        ),
        Testimonial(
            quote = "La mejor experiencia de compra online. Productos frescos, entrega rápida y atención al cliente excelente.",
            name = "Carlos",
            profession = "Chef",
            imageUrlName = "testimonial_4"
        )
    )

    // Obtenemos los productos desde la base de datos, solo los activos
    fun getProducts(): Flow<List<Product>> {
        // Retornamos productos activos desde la base de datos
        return productDao.getActiveProducts()
    }
    
    // Función auxiliar para obtener productos iniciales directamente
    fun getInitialProducts(): List<Product> = initialProducts

    fun getTestimonials(): List<Testimonial> = simulatedTestimonials

    // nuestros metodos de persistencia real(ROOM)

    /**
     * Obtiene los ítems del carrito desde la base de datos local (Room).
     */
    fun getCartItems(): Flow<List<CartItem>> {
        return cartDao.getAllCartItems()
    }

    /**
     * Agrega o actualiza un ítem en el carrito.
     */
    suspend fun insertOrUpdateCartItem(item: CartItem) {
        cartDao.insert(item)
    }

    /**
     * Elimina un ítem específico del carrito.
     */
    suspend fun deleteCartItem(item: CartItem) {
        cartDao.deleteItemById(item.productId)
    }

    /**
     * Vacía completamente el carrito de compras.
     */
    suspend fun clearCart() {
        cartDao.clearAll()
    }
}