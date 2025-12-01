package com.huertohogar.presentation.map

import android.Manifest
import android.content.pm.PackageManager
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.huertohogar.presentation.AppBar
import androidx.navigation.NavHostController
import com.google.android.gms.maps.CameraUpdateFactory
import kotlinx.coroutines.launch

// las coordenadas de las tiendas en Chile
val storeLocations = mapOf(
    "Santiago" to LatLng(-33.4489, -70.6693),
    "Puerto Montt" to LatLng(-41.4718, -72.9366),
    "Villarica" to LatLng(-39.2856, -72.2276),
    "Nacimiento" to LatLng(-37.5000, -72.6667),
    "Viña del Mar" to LatLng(-33.0246, -71.5518),
    "Valparaíso" to LatLng(-33.0472, -71.6127),
    "Concepción" to LatLng(-36.8201, -73.0444)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(navController: NavHostController) {
    val context = LocalContext.current
    val santiago = LatLng(-33.4489, -70.6693)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(santiago, 6f)
    }
    
    val scope = rememberCoroutineScope()

    var selectedStore by remember { mutableStateOf<String?>(null) }
    var hasLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    Scaffold(
        topBar = { AppBar(navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            // la lista de las tiendas
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Text(
                        "Nuestras Tiendas",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(8.dp))
                    
                    storeLocations.keys.forEach { city ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = if (selectedStore == city)
                                    MaterialTheme.colorScheme.primary
                                else
                                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(Modifier.width(8.dp))
                            TextButton(
                                onClick = {
                                    selectedStore = city
                                    storeLocations[city]?.let { location ->
                                        scope.launch {
                                            cameraPositionState.animate(
                                                CameraUpdateFactory.newCameraPosition(
                                                    CameraPosition.fromLatLngZoom(location, 15f)
                                                )
                                            )
                                        }
                                    }
                                }
                            ) {
                                Text(
                                    text = city,
                                    fontWeight = if (selectedStore == city) FontWeight.Bold else FontWeight.Normal,
                                    color = if (selectedStore == city)
                                        MaterialTheme.colorScheme.primary
                                    else
                                        MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }

            // Mapa
            Box(modifier = Modifier.fillMaxSize()) {
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState,
                    properties = MapProperties(
                        isMyLocationEnabled = hasLocationPermission,
                        mapType = MapType.NORMAL
                    ),
                    uiSettings = MapUiSettings(
                        zoomControlsEnabled = true,
                        myLocationButtonEnabled = hasLocationPermission
                    )
                ) {
                    // Marcadores de las tiendas
                    storeLocations.forEach { (city, location) ->
                        Marker(
                            state = MarkerState(position = location),
                            title = "HuertoHogar $city",
                            snippet = "Tienda en $city"
                        )
                    }
                }
            }
        }
    }
}
