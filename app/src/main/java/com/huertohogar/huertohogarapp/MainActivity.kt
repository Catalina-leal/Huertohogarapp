package com.huertohogar.huertohogarapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.huertohogar.huertohogarapp.navigation.AppNavGraph
import com.huertohogar.huertohogarapp.ui.theme.HuertoHogarAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HuertoHogarAppTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    HuertoHogarApp()
                }
            }
        }
    }
}

@Composable
fun HuertoHogarApp() {
    val navController = rememberNavController()
    AppNavGraph(navController = navController)
}
