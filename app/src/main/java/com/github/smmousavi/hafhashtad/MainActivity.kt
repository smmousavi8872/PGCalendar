package com.github.smmousavi.hafhashtad

import SearchScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.github.smmousavi.hafhashtad.ui.theme.HafhashtadTheme
import com.github.smmousavi.store.StoreScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            HafhashtadTheme {
                val navController = rememberNavController()
                NavHost(navController, startDestination = "store") {
                    composable("store") { StoreScreen(navController) }
                    composable("search") { SearchScreen(navController) }
                }
            }
        }
    }
}
