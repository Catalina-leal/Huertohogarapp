package com.huertohogar.data.api

import retrofit2.Response
import retrofit2.http.*

/**
 * Nuestro Servicio API para comunicación con el backend
 */
interface ApiService {
    
    // Productos
    @GET("products")
    suspend fun getProducts(): Response<List<ProductApiResponse>>
    
    @GET("products/{id}")
    suspend fun getProductById(@Path("id") id: String): Response<ProductApiResponse>
    
    // Usuarios
    @POST("auth/register")
    suspend fun registerUser(@Body user: RegisterRequest): Response<AuthResponse>
    
    @POST("auth/login")
    suspend fun loginUser(@Body credentials: LoginRequest): Response<AuthResponse>
    
    @GET("users/{email}")
    suspend fun getUser(@Path("email") email: String): Response<UserApiResponse>
    
    @PUT("users/{email}")
    suspend fun updateUser(@Path("email") email: String, @Body user: UserApiResponse): Response<UserApiResponse>
    
    // Pedidos
    @POST("orders")
    suspend fun createOrder(@Body order: OrderRequest): Response<OrderApiResponse>
    
    @GET("orders/user/{email}")
    suspend fun getOrdersByUser(@Path("email") email: String): Response<List<OrderApiResponse>>
    
    @GET("orders/{orderId}")
    suspend fun getOrderById(@Path("orderId") orderId: String): Response<OrderApiResponse>
    
    @PUT("orders/{orderId}/status")
    suspend fun updateOrderStatus(
        @Path("orderId") orderId: String,
        @Body status: OrderStatusUpdate
    ): Response<OrderApiResponse>
    
    // Reseñas
    @GET("reviews/product/{productId}")
    suspend fun getReviewsByProduct(@Path("productId") productId: String): Response<List<ReviewApiResponse>>
    
    @POST("reviews")
    suspend fun addReview(@Body review: ReviewApiResponse): Response<ReviewApiResponse>
    
    // Blog
    @GET("blog/posts")
    suspend fun getBlogPosts(): Response<List<BlogPostApiResponse>>
    
    @GET("blog/posts/{id}")
    suspend fun getBlogPostById(@Path("id") id: String): Response<BlogPostApiResponse>
    
    // Pagos
    @POST("payments/process")
    suspend fun processPayment(@Body payment: PaymentRequest): Response<PaymentResponse>
}

// Nuestros DTOs
data class ProductApiResponse(
    val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val stock: Double,
    val category: String,
    val imageUrl: String,
    val origin: String?,
    val isOrganic: Boolean
)

data class RegisterRequest(
    val email: String,
    val password: String,
    val fullName: String
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class AuthResponse(
    val token: String,
    val user: UserApiResponse
)

data class UserApiResponse(
    val email: String,
    val fullName: String,
    val phone: String?,
    val address: String?,
    val city: String?,
    val region: String?,
    val loyaltyPoints: Int
)

data class OrderRequest(
    val userEmail: String,
    val items: List<OrderItemRequest>,
    val shippingAddress: String,
    val deliveryDate: Long?
)

data class OrderItemRequest(
    val productId: String,
    val quantity: Int,
    val unitPrice: Double
)

data class OrderApiResponse(
    val orderId: String,
    val userEmail: String,
    val orderDate: Long,
    val status: String,
    val totalAmount: Double,
    val shippingAddress: String,
    val trackingNumber: String?
)

data class OrderStatusUpdate(
    val status: String
)

data class ReviewApiResponse(
    val id: Long?,
    val productId: String,
    val userEmail: String,
    val userName: String,
    val rating: Int,
    val comment: String,
    val createdAt: Long
)

data class BlogPostApiResponse(
    val id: String,
    val title: String,
    val content: String,
    val author: String,
    val imageUrl: String,
    val publishedDate: Long,
    val category: String,
    val readTime: Int
)

data class PaymentRequest(
    val orderId: String,
    val amount: Double,
    val paymentMethod: String, // "credit_card", "debit_card", "transfer", "cash"
    val cardNumber: String?,
    val cardHolder: String?,
    val expiryDate: String?,
    val cvv: String?
)

data class PaymentResponse(
    val paymentId: String,
    val status: String, // "success", "pending", "failed"
    val transactionId: String?,
    val message: String
)
