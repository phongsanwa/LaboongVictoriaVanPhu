package com.example.laboongvictoriavanphu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.laboongvictoriavanphu.ui.dashboard.DashboardScreen
import com.example.laboongvictoriavanphu.ui.theme.LaboongVictoriaVanPhuTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LaboongVictoriaVanPhuTheme {
                DashboardScreen()
            }
        }
    }
}
