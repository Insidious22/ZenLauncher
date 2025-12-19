package com.insidious22.zenlauncher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.insidious22.zenlauncher.presentation.HomeScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Edge-to-edge real (System UI feel)
        enableEdgeToEdge()

        setContent {
            HomeScreen()
        }
    }
}
