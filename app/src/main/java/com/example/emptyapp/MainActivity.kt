package com.example.emptyapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.emptyapp.core.designsystem.theme.EmptyAppTheme
import com.example.emptyapp.core.network.token.SessionManager
import com.example.emptyapp.core.network.token.TokenStatus
import com.example.emptyapp.navigation.Destinations
import com.example.emptyapp.navigation.EmptyAppNavHost
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val startDestination =
            if (sessionManager.sessionState.value == TokenStatus.Authenticated) {
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
