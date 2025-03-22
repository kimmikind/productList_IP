package com.example.productlist_ip

import android.graphics.Color.toArgb
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.WindowCompat
import com.example.productlist_ip.ui.screen.MainScreen
import com.example.productlist_ip.ui.theme.Blue40
import com.example.productlist_ip.ui.theme.ProductList_IPTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                Blue40.toArgb(), Blue40.toArgb()
            ),
            navigationBarStyle = SystemBarStyle.light(
                Blue40.toArgb(), Blue40.toArgb()
            )
        )
        setContent {
            ProductList_IPTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                   // color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }


}

