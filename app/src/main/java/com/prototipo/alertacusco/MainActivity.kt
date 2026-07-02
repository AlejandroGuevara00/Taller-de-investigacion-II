package com.prototipo.alertacusco

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.prototipo.alertacusco.navigation.AppNavigation
import com.prototipo.alertacusco.ui.theme.AlertacuscoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            AlertacuscoTheme {
                AppNavigation()
            }
        }
    }
}