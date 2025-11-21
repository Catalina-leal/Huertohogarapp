package com.huertohogar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.huertohogar.presentation.MainScreen
import com.huertohogar.ui.theme.HuertohogarTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HuertohogarTheme {
                MainScreen()
            }
        }
    }
}
