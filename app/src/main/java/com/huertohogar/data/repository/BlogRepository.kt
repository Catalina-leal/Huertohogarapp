package com.huertohogar.data.repository

import com.huertohogar.data.model.BlogPost
import com.huertohogar.data.model.BlogPostDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * en este repositorio gestionamos  el blog posts
 */
class BlogRepository(
    private val blogPostDao: BlogPostDao
) {
    // los posts iniciales con imagenes
    private val initialPosts = listOf(
        BlogPost(
            id = "BLOG001",
            title = "10 Beneficios de Consumir Productos Orgánicos",
            content = "Los productos orgánicos no solo son mejores para tu salud, sino también para el medio ambiente. En este artículo exploramos los principales beneficios de incluir alimentos orgánicos en tu dieta diaria. Desde la reducción de pesticidas hasta el apoyo a la agricultura sostenible, descubrirás por qué cada vez más personas eligen productos orgánicos. Los alimentos orgánicos contienen más nutrientes y antioxidantes, y su producción ayuda a preservar la biodiversidad y la salud del suelo.",
            author = "Equipo HuertoHogar",
            imageUrlName = "blog_1",
            publishedDate = System.currentTimeMillis() - (7 * 24 * 60 * 60 * 1000L), // Hace 7 días
            category = "Alimentación Saludable",
            readTime = 5
        ),
        BlogPost(
            id = "BLOG002",
            title = "Cómo Cultivar tu Propio Huerto en Casa",
            content = "Aprende los secretos para crear tu propio huerto urbano. Desde la selección de semillas hasta el cuidado diario, te guiamos paso a paso para que puedas disfrutar de verduras frescas cultivadas por ti mismo. No necesitas un gran espacio, solo ganas de aprender y dedicación. Descubre qué plantas son ideales para principiantes, cómo preparar la tierra, cuándo regar y cómo proteger tus cultivos de plagas de forma natural.",
            author = "María González",
            imageUrlName = "blog_2",
            publishedDate = System.currentTimeMillis() - (14 * 24 * 60 * 60 * 1000L), // Hace 14 días
            category = "Sostenibilidad",
            readTime = 8
        ),
        BlogPost(
            id = "BLOG003",
            title = "La Importancia de Apoyar a los Agricultores Locales",
            content = "Cuando compras productos locales, no solo obtienes alimentos más frescos, sino que también apoyas a las comunidades agrícolas de tu región. Descubre cómo tu elección de compra puede tener un impacto positivo en la economía local y en la preservación de tradiciones agrícolas. Los agricultores locales utilizan métodos más sostenibles, reducen la huella de carbono del transporte y mantienen viva la cultura agrícola de Chile.",
            author = "Carlos Mendoza",
            imageUrlName = "blog_3",
            publishedDate = System.currentTimeMillis() - (21 * 24 * 60 * 60 * 1000L), // Hace 21 días
            category = "Comunidad",
            readTime = 6
        )
    )
    
    fun getAllBlogPosts(): Flow<List<BlogPost>> {
        // Por ahora retornamos los posts iniciales
        // En produccion, esto vendría de blogPostDao.getAllBlogPosts()
        return flow {
            kotlinx.coroutines.delay(300) // Simulamos una carga
            emit(initialPosts)
        }
    }

    suspend fun getBlogPostById(postId: String): BlogPost? {
        return initialPosts.find { it.id == postId }
            ?: blogPostDao.getBlogPostById(postId)
    }

    fun getBlogPostsByCategory(category: String): Flow<List<BlogPost>> {
        return flow {
            kotlinx.coroutines.delay(300)
            emit(initialPosts.filter { it.category == category })
        }
    }
    
    // Una función auxiliar para obtener los posts iniciales
    fun getInitialPosts(): List<BlogPost> = initialPosts
}
