package com.huertohogar.presentation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.huertohogar.data.model.Product
import com.huertohogar.presentation.home.HomeScreen
import com.huertohogar.presentation.products.ProductsScreen
import com.huertohogar.presentation.login.LoginScreen
import com.huertohogar.presentation.registro.RegistroScreen
import com.huertohogar.presentation.cart.CartScreen
import com.huertohogar.presentation.checkout.CheckoutScreen
import com.huertohogar.presentation.about.AboutScreen
import com.huertohogar.presentation.blog.BlogScreen
import com.huertohogar.presentation.profile.OrderHistoryScreen
import com.huertohogar.presentation.profile.LogoutScreen
import com.huertohogar.presentation.profile.ProfileScreen
import com.huertohogar.presentation.productdetail.ProductDetailScreen
import com.huertohogar.presentation.map.MapScreen
import com.huertohogar.presentation.payment.PaymentScreen
import com.huertohogar.presentation.components.Error404Screen

//   Rutas de Navegacion extendidas
sealed class Screen(val route: String, val icon: androidx.compose.ui.graphics.vector.ImageVector?, val label: String) {
    object Home : Screen("home", Icons.Default.Home, "Inicio")
    object Products : Screen("products", Icons.Default.ShoppingCart, "Productos")
    object About : Screen("about", Icons.Default.Info, "Nosotros")
    object Blog : Screen("blog", Icons.Default.Description, "Blog")
    object Profile : Screen("profile", Icons.Default.Person, "Perfil")
    // Rutas sin icono en la barra inferior, pero usadas para navegación:
    object Login : Screen("login", null, "Login")
    object Register : Screen("registro", null, "Registro")
    object Cart : Screen("cart", null, "Carrito")
    object Checkout : Screen("checkout", null, "Pago")
    object OrderHistory : Screen("order_history", null, "Historial")
    object Logout : Screen("logout", null, "Cerrar Sesión")
    object NotFound : Screen("404", null, "No Encontrada")
    object ProductDetail : Screen("product_detail/{productId}", null, "Detalle Producto") {
        fun createRoute(productId: String) = "product_detail/$productId"
    }
    object Map : Screen("map", null, "Mapa")
    object Payment : Screen("payment/{orderId}/{amount}", null, "Pago") {
        fun createRoute(orderId: String, amount: Double) = "payment/$orderId/$amount"
    }
    // Rutas de Admin
    object AdminProducts : Screen("admin/products", null, "Admin Productos")
    object AdminUsers : Screen("admin/users", null, "Admin Usuarios")
    object AdminSales : Screen("admin/sales", null, "Reporte Ventas")
    object AdminDashboard : Screen("admin/dashboard", null, "Panel Admin")
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { AppBottomBar(navController) }
    ) { innerPadding ->
        AppNavHost(navController, Modifier.padding(innerPadding))
    }
}

// AppNavHost  incluye todas las rutas con animaciones
@Composable
fun AppNavHost(navController: NavHostController, modifier: Modifier) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier,
        enterTransition = {
            fadeIn(animationSpec = tween(300)) + slideInHorizontally(
                initialOffsetX = { it },
                animationSpec = tween(300)
            )
        },
        exitTransition = {
            fadeOut(animationSpec = tween(300)) + slideOutHorizontally(
                targetOffsetX = { -it },
                animationSpec = tween(300)
            )
        },
        popEnterTransition = {
            fadeIn(animationSpec = tween(300)) + slideInHorizontally(
                initialOffsetX = { -it },
                animationSpec = tween(300)
            )
        },
        popExitTransition = {
            fadeOut(animationSpec = tween(300)) + slideOutHorizontally(
                targetOffsetX = { it },
                animationSpec = tween(300)
            )
        }
    ) {
        composable(Screen.Home.route) { HomeScreen(navController) }
        composable(Screen.Products.route) { ProductsScreen(navController) }
        composable(Screen.About.route) { AboutScreen(navController) }
        composable(Screen.Blog.route) { BlogScreen(navController) }
        composable(Screen.Profile.route) { ProfileScreen(navController) }

        // Rutas funcionales
        composable(Screen.Login.route) { LoginScreen(navController) }
        composable(Screen.Register.route) { RegistroScreen(navController) }
        composable(Screen.Cart.route) { CartScreen(navController) }
        composable(Screen.Checkout.route) { CheckoutScreen(navController) }
        composable(Screen.OrderHistory.route) { OrderHistoryScreen(navController) }
        composable(Screen.Logout.route) { LogoutScreen(navController) }
        composable(Screen.NotFound.route) { Error404Screen(navController) }
        
        // Detalle de producto con reseñas
        composable(
            route = Screen.ProductDetail.route,
            arguments = listOf(
                androidx.navigation.navArgument("productId") {
                    type = androidx.navigation.NavType.StringType
                }
            )
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId") ?: ""
            ProductDetailScreen(productId = productId, navController = navController)
        }
        
        // Mapa de las tiendas
        composable(Screen.Map.route) { MapScreen(navController) }
        
        // Pantalla de pago
        composable(
            route = Screen.Payment.route,
            arguments = listOf(
                androidx.navigation.navArgument("orderId") {
                    type = androidx.navigation.NavType.StringType
                },
                androidx.navigation.navArgument("amount") {
                    type = androidx.navigation.NavType.FloatType
                }
            )
        ) { backStackEntry ->
            val orderId = backStackEntry.arguments?.getString("orderId") ?: ""
            val amount = backStackEntry.arguments?.getFloat("amount")?.toDouble() ?: 0.0
            PaymentScreen(
                orderId = orderId,
                totalAmount = amount,
                navController = navController
            )
        }
        
        // Rutas de Admin
        composable(Screen.AdminProducts.route) { 
            com.huertohogar.presentation.admin.AdminProductsScreen(navController) 
        }
        composable(Screen.AdminUsers.route) { 
            com.huertohogar.presentation.admin.AdminUsersScreen(navController) 
        }
        composable(Screen.AdminSales.route) { 
            com.huertohogar.presentation.admin.AdminSalesScreen(navController) 
        }
        composable(Screen.AdminDashboard.route) { 
            com.huertohogar.presentation.admin.AdminDashboardScreen(navController) 
        }
        

    }
}

// Barra de Navegacion Inferior
@Composable
fun AppBottomBar(navController: NavHostController) {
    val items = listOf(Screen.Home, Screen.Products, Screen.Profile) // Solo rutas con iconos
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar {
        items.forEach { screen ->
            NavigationBarItem(

                icon = { Icon(screen.icon!!, contentDescription = screen.label) },
                label = { Text(screen.label) },
                selected = currentRoute == screen.route,
                onClick = { navController.navigate(screen.route) {
                    popUpTo(navController.graph.findStartDestination().id) { this.saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }}
            )
        }
    }
}

// App Bar (Top Bar)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(navController: NavHostController) {
    val context = LocalContext.current
    // Obtenemos el ViewModel del carrito para el contador dinámico
    val cartViewModel: com.huertohogar.presentation.cart.CartViewModel = androidx.lifecycle.viewmodel.compose.viewModel(
        factory = com.huertohogar.presentation.cart.CartViewModel.Factory(
            context.applicationContext as android.app.Application
        )
    )
    val cartItems by cartViewModel.cartItems.collectAsState()
    val cartItemCount = cartItems.sumOf { it.quantity }

    TopAppBar(
        title = {
            Row {
                Text(
                    text = "Huerto",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Hogar",
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        actions = {
            // Icono de Compartir App
            IconButton(onClick = { 
                com.huertohogar.utils.ShareHelper.shareApp(context)
            }) {
                Icon(Icons.Default.Share, contentDescription = "Compartir app", tint = Color.Black)
            }
            // Icono de Login/Perfil
            IconButton(onClick = { navController.navigate(Screen.Login.route) }) {
                Icon(Icons.Default.Person, contentDescription = "Perfil/Login", tint = Color.Black)
            }
            // Icono de Carrito con contador dinámico (Badge)
            IconButton(onClick = { navController.navigate(Screen.Cart.route) }) {
                if (cartItemCount > 0) {
                    BadgedBox(
                        badge = { 
                            Badge(containerColor = MaterialTheme.colorScheme.secondary) { 
                                Text(cartItemCount.toString()) 
                            } 
                        }
                    ) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = "Carrito", tint = Color.Black)
                    }
                } else {
                    Icon(Icons.Default.ShoppingCart, contentDescription = "Carrito", tint = Color.Black)
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
    )
}

// Tarjeta de Producto Reutilizable (ProductCard)
@Composable
fun ProductCard(product: Product) {
    Card(
        modifier = Modifier.padding(4.dp).fillMaxWidth().clickable { /* Navegar a detalle */ },
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Box(
                modifier = Modifier.fillMaxWidth().height(150.dp).background(Color.Gray.copy(alpha = 0.1f))
            ) {
              
                // Image(painter = painterResource(id = R.drawable.placeholder), contentDescription = null)

                // Etiqueta "Frescos"
                Text(
                    text = product.tag,
                    color = Color.White,
                    modifier = Modifier.align(Alignment.TopStart).padding(8.dp)
                        .background(MaterialTheme.colorScheme.secondary)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }

            Column(modifier = Modifier.padding(12.dp)) {
                Text(text = product.name, style = MaterialTheme.typography.titleMedium)
                Row(modifier = Modifier.padding(top = 4.dp)) {
                    Text(text = "$${product.price}", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.width(8.dp))
                    product.oldPrice?.let { oldPrice -> // <-- Condicional y formato
                        Text(
                            text = "$${oldPrice}", // <-- Formato a String
                            color = Color.Gray,
                            style = MaterialTheme.typography.bodySmall.copy(textDecoration = TextDecoration.LineThrough)
                        )
                    }
                }
                Button(
                    onClick = { /* Añadir al carrito */ },
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                ) {
                    Text("Añadir al carrito")
                }
            }
        }
    }
}