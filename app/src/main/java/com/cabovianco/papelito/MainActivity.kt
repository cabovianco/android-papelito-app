package com.cabovianco.papelito

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.cabovianco.papelito.presentation.navigation.AppNavigation
import com.cabovianco.papelito.presentation.ui.theme.PapelitoTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PapelitoTheme {
                AppNavigation()
            }
        }
    }
}
