package com.huertohogar.presentation.home

import android.app.Application
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontStyle
import kotlinx.coroutines.delay
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.huertohogar.R
import com.huertohogar.data.model.CartItem
import com.huertohogar.data.model.Product
import com.huertohogar.data.model.Testimonial
import com.huertohogar.presentation.AppBar
import com.huertohogar.presentation.Screen
import com.huertohogar.presentation.components.ProductCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: HomeViewModel = viewModel(
        factory = HomeViewModel.Factory(LocalContext.current.applicationContext as Application)
    ),
    blogViewModel: com.huertohogar.presentation.blog.BlogViewModel = viewModel(
        factory = com.huertohogar.presentation.blog.BlogViewModel.Factory(
            LocalContext.current.applicationContext as Application
        )
    )
) {
    // aca estamos observando el estado
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(topBar = { AppBar(navController) }) { paddingValues ->
        when (uiState) {
            is HomeUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            }
            is HomeUiState.Error -> {
                Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                    Text(
                        text = (uiState as HomeUiState.Error).message,
                        color = Color.Red
                    )
                }
            }
            is HomeUiState.Success -> {
                val data = uiState as HomeUiState.Success

                LazyColumn(
                    modifier = Modifier.padding(paddingValues),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item { CarouselSection() }
                    item { WeatherWidget() }
                    item { AboutSection() }
                    item { EnhancedFeatureSection() }
                    item { NaturalProcessesSection() }
                    item { ProductsHeader() }
                    
                    // Lista de productos usando LazyVerticalGrid dentro de un item
                    item {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = 800.dp)
                                .padding(horizontal = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding = PaddingValues(vertical = 8.dp),
                            userScrollEnabled = false
                        ) {
                            items(data.products.take(6), key = { it.id }) { product ->
                                ProductCard(
                                    product = product,
                                    onAddToCart = { product ->
                                        viewModel.addToCart(
                                            CartItem(
                                                productId = product.id,
                                                name = product.name,
                                                price = product.price,
                                                quantity = 1,
                                                imageUrl = product.imageUrlName
                                            )
                                        )
                                    },
                                    onViewDetails = { product ->
                                        navController.navigate(Screen.ProductDetail.createRoute(product.id))
                                    }
                                )
                            }
                        }
                    }
                    
                    // Botón "Más Productos"
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Button(
                                onClick = { navController.navigate(Screen.Products.route) },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary
                                )
                            ) {
                                Text("Más Productos", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                    
                    item { VisitUsSection(navController) }
                    item { EnhancedTestimonialSection(data.testimonials) }
                    item { BlogPreviewSection(navController, blogViewModel) }
                    item { FooterSection(navController) }
                }
            }
        }
    }
}

// implementando placeholder sin cambios

@Composable
fun CarouselSection() {
    val carouselImages = listOf(
        R.drawable.carousel_1,
        R.drawable.carousel_2
    )
    var currentIndex by remember { mutableStateOf(0) }
    
    // Nos encargamos de cambiar la imagen cada 5 segundos
    LaunchedEffect(Unit) {
        while (true) {
            delay(5000)
            currentIndex = (currentIndex + 1) % carouselImages.size
        }
    }

    Crossfade(
        targetState = carouselImages[currentIndex],
        modifier = Modifier.fillMaxWidth().height(250.dp),
        label = "CarouselCrossfade",
        animationSpec = tween(durationMillis = 800)
    ) { drawableId ->
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = drawableId),
                contentDescription = "Carrusel de portada",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            // manejo del overlay oscuro para la mejor legibilidad del texto
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f))
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Cultiva salud, compra Orgánico",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Productos frescos del campo a tu mesa",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White.copy(alpha = 0.9f)
                )
            }
        }
    }
    
    // Indicadores de página
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        carouselImages.forEachIndexed { index, _ ->
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .padding(4.dp)
                    .background(
                        color = if (index == currentIndex) 
                            MaterialTheme.colorScheme.primary 
                        else 
                            Color.Gray.copy(alpha = 0.5f),
                        shape = androidx.compose.foundation.shape.CircleShape
                    )
            )
        }
    }
}

@Composable
fun AboutSection() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Imagen
            Image(
                painter = painterResource(id = R.drawable.about),
                contentDescription = "Acerca de HuertoHogar",
                modifier = Modifier
                    .size(120.dp)
                    .clip(MaterialTheme.shapes.medium),
                contentScale = ContentScale.Crop
            )
            
            Spacer(Modifier.width(16.dp))
            
            // Texto
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "De la tierra a tu mesa",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "HuertoHogar lleva más de 6 años conectando a los chilenos con productos frescos del campo, directamente a sus hogares.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun EnhancedFeatureSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Nuestras Características",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        // Separador decorativo
        Row(
            modifier = Modifier.padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .width(40.dp)
                    .height(3.dp)
                    .background(MaterialTheme.colorScheme.secondary)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Box(
                modifier = Modifier
                    .width(60.dp)
                    .height(3.dp)
                    .background(MaterialTheme.colorScheme.primary)
            )
        }
        Text(
            text = "En HuertoHogar, nos enorgullecemos de llevar la frescura y calidad del campo directamente a la puerta de nuestros clientes en Chile, actualmente somos líder en la distribución de productos frescos y naturales en Chile, reconocida por nuestra calidad excepcional, servicio al cliente y compromiso con la sostenibilidad.",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(horizontal = 16.dp),
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(24.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            FeatureItem(
                icon = R.drawable.icon_1,
                title = "Frescos",
                description = "Productos del día"
            )
            FeatureItem(
                icon = R.drawable.icon_2,
                title = "Orgánicos",
                description = "Certificados"
            )
            FeatureItem(
                icon = R.drawable.icon_3,
                title = "Rápido",
                description = "Entrega rápida"
            )
        }
    }
}

@Composable
fun NaturalProcessesSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(24.dp))
        // Icono de hojas (usando un icono de Material)
        Icon(
            imageVector = Icons.Default.Eco,
            contentDescription = "Procesos Naturales",
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = "Procesos Naturales",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Cultivamos con técnicas tradicionales y respetuosas con el medio ambiente, asegurando la calidad y frescura de nuestros productos.",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(horizontal = 16.dp),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun FeatureItem(icon: Int, title: String, description: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(8.dp)
    ) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = title,
            modifier = Modifier.size(64.dp),
            contentScale = ContentScale.Fit
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = description,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
    }
}

@Composable
fun VisitUsSection(navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .padding(24.dp)
    ) {
        Column {
            Text(
                text = "Visítenos",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(Modifier.height(12.dp))
            Text(
                text = "Te invitamos a conocernos en persona y descubrir todo lo que tenemos para ofrecer. Nuestro equipo estará encantado de recibirte, responder tus preguntas y brindarte la mejor atención. ¡Ven y vive la experiencia por ti mismo!",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White
            )
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = { /* Navegar a ubicación/mapa */ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.LocationOn, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Ubicación", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun BlogPreviewSection(
    navController: NavHostController,
    blogViewModel: com.huertohogar.presentation.blog.BlogViewModel = viewModel(
        factory = com.huertohogar.presentation.blog.BlogViewModel.Factory(
            LocalContext.current.applicationContext as Application
        )
    )
) {
    val context = LocalContext.current
    val blogState by blogViewModel.uiState.collectAsState()
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Separador decorativo
        Row(
            modifier = Modifier.padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .width(40.dp)
                    .height(3.dp)
                    .background(MaterialTheme.colorScheme.primary)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Box(
                modifier = Modifier
                    .width(60.dp)
                    .height(3.dp)
                    .background(MaterialTheme.colorScheme.secondary)
            )
        }
        
        Text(
            text = "Últimas publicaciones del Blog",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "La sabiduría es poder, por eso te mantenemos informado de todo lo relacionado con el mercado.",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 8.dp),
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(16.dp))
        
        if (blogState.isLoading) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        } else if (blogState.posts.isNotEmpty()) {
            val firstPost = blogState.posts.first()
            val imageResId = remember(firstPost.imageUrlName) {
                try {
                    context.resources.getIdentifier(
                        firstPost.imageUrlName,
                        "drawable",
                        context.packageName
                    )
                } catch (e: Exception) {
                    0
                }
            }
            
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { navController.navigate(Screen.Blog.route) },
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column {
                    if (imageResId != 0) {
                        Image(
                            painter = painterResource(id = imageResId),
                            contentDescription = firstPost.title,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentScale = ContentScale.Crop
                        )
                    }
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = firstPost.title,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.Person,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = firstPost.author,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.CalendarToday,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = java.text.SimpleDateFormat("dd MMM, yyyy", java.util.Locale("es", "ES"))
                                        .format(java.util.Date(firstPost.publishedDate)),
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FooterSection(navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF1A1A1A))
            .padding(24.dp)
    ) {
        Column {
            // Logo
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Huerto",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Hogar",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
            Spacer(Modifier.height(12.dp))
            Text(
                text = "Una tienda online dedicada a llevar la frescura y calidad de los productos del campo directamente a la puerta de nuestros clientes en Chile.",
                style = MaterialTheme.typography.bodySmall,
                color = Color.White
            )
            Spacer(Modifier.height(16.dp))
            
            // Redes sociales
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                IconButton(
                    onClick = { /* Facebook */ },
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color.White.copy(alpha = 0.1f), CircleShape)
                ) {
                    Text(
                        text = "f",
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                IconButton(
                    onClick = { /* Instagram */ },
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color.White.copy(alpha = 0.1f), CircleShape)
                ) {
                    Icon(
                        Icons.Default.CameraAlt,
                        contentDescription = "Instagram",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
                IconButton(
                    onClick = { /* WhatsApp */ },
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color.White.copy(alpha = 0.1f), CircleShape)
                ) {
                    Icon(
                        Icons.Default.Phone,
                        contentDescription = "WhatsApp",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            
            Spacer(Modifier.height(24.dp))
            
            // Ubicación
            Text(
                text = "Ubicación",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Av. España N°8, Santiago chile", style = MaterialTheme.typography.bodySmall, color = Color.White)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Phone, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("+56985007544", style = MaterialTheme.typography.bodySmall, color = Color.White)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Email, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Huertohogar.info@gmail.com", style = MaterialTheme.typography.bodySmall, color = Color.White)
            }
            
            Spacer(Modifier.height(24.dp))
            
            // Enlaces rápidos
            Text(
                text = "Enlaces Rápidos",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(Modifier.height(8.dp))
            listOf("Nosotros", "Contactos", "Blog", "Productos", "Iniciar Sesión").forEach { link ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            when (link) {
                                "Blog" -> navController.navigate(Screen.Blog.route)
                                "Productos" -> navController.navigate(Screen.Products.route)
                                "Iniciar Sesión" -> navController.navigate(Screen.Login.route)
                            }
                        }
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(link, style = MaterialTheme.typography.bodySmall, color = Color.White)
                }
            }
            
            Spacer(Modifier.height(24.dp))
            HorizontalDivider(color = Color.White.copy(alpha = 0.3f))
            Spacer(Modifier.height(16.dp))
            
            // Copyright
            Text(
                text = "© HuertoHogar, Todos los Derechos Reservados",
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.7f)
            )
            Text(
                text = "Desarrollado Por Hawk & Guerbens",
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
fun ProductsHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Nuestros Productos",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        // Separador decorativo (verde y rojo)
        Row(
            modifier = Modifier.padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .width(40.dp)
                    .height(3.dp)
                    .background(MaterialTheme.colorScheme.primary)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Box(
                modifier = Modifier
                    .width(60.dp)
                    .height(3.dp)
                    .background(MaterialTheme.colorScheme.secondary)
            )
        }
        Text(
            text = "Orgánico, la elección natural.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
    }
}

@Composable
fun EnhancedTestimonialSection(testimonials: List<Testimonial>) {
    val context = LocalContext.current
    var currentTestimonialIndex by remember { mutableStateOf(0) }
    val currentTestimonial = testimonials.getOrNull(currentTestimonialIndex)
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Separador decorativo
        Row(
            modifier = Modifier.padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .width(40.dp)
                    .height(3.dp)
                    .background(MaterialTheme.colorScheme.primary)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Box(
                modifier = Modifier
                    .width(60.dp)
                    .height(3.dp)
                    .background(MaterialTheme.colorScheme.secondary)
            )
        }
        
        Text(
            text = "Reseñas de nuestros clientes",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "¡Con transparencia y productos orgánicos, cuidamos tu bienestar!",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 8.dp),
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(16.dp))
        
        if (currentTestimonial != null) {
            val imageResId = remember(currentTestimonial.imageUrlName) {
                try {
                    context.resources.getIdentifier(
                        currentTestimonial.imageUrlName,
                        "drawable",
                        context.packageName
                    )
                } catch (e: Exception) {
                    0
                }
            }
            
            // Card verde con testimonio
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp)
                ) {
                    // Quote icon
                    Text(
                        text = "\"",
                        style = MaterialTheme.typography.displayLarge,
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    
                    Text(
                        text = currentTestimonial.quote,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (imageResId != 0) {
                            Image(
                                painter = painterResource(id = imageResId),
                                contentDescription = currentTestimonial.name,
                                modifier = Modifier
                                    .size(50.dp)
                                    .clip(androidx.compose.foundation.shape.CircleShape),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                        }
                        Column {
                            Text(
                                text = currentTestimonial.name,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = currentTestimonial.profession,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                        }
                    }
                }
            }
            
            // Navegación de testimonios
            Spacer(Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        currentTestimonialIndex = if (currentTestimonialIndex > 0) 
                            currentTestimonialIndex - 1 
                        else 
                            testimonials.size - 1
                    }
                ) {
                    Icon(
                        Icons.Default.ChevronLeft,
                        contentDescription = "Anterior",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                IconButton(
                    onClick = {
                        currentTestimonialIndex = (currentTestimonialIndex + 1) % testimonials.size
                    }
                ) {
                    Icon(
                        Icons.Default.ChevronRight,
                        contentDescription = "Siguiente",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}