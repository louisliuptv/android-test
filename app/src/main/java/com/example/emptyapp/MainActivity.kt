package com.example.emptyapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.emptyapp.core.designsystem.theme.EmptyAppTheme
import com.example.emptyapp.core.network.token.TokenProvider
import com.example.emptyapp.navigation.Destinations
import com.example.emptyapp.navigation.EmptyAppNavHost
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var tokenProvider: TokenProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val startDestination =
            if (!tokenProvider.currentAccessToken().isNullOrBlank()) {
                Destinations.GPS_LIST
            } else {
                Destinations.LOGIN
            }

        setContent {
            EmptyAppTheme {
                EmptyAppNavHost(startDestination = startDestination)
            }
        }
    }
}
