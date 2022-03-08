package com.example.apollographqlplayground

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.apollographqlplayground.screens.details.DetailsScreen
import com.example.apollographqlplayground.screens.home.HomeScreen
import com.example.apollographqlplayground.screens.splash.SplashScreen
import com.example.apollographqlplayground.ui.theme.ApolloGraphQLPlaygroundTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ApolloGraphQLPlaygroundTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background) {
                    MainHolder()
                }
            }
        }
    }
}

@Composable
fun MainHolder() {
    val scaffoldState = rememberScaffoldState()
    val controller = rememberNavController()
    val startDestination = "splash"

    Scaffold(
        scaffoldState = scaffoldState
    ) {
        NavHost(navController = controller, startDestination = startDestination){
            composable(route = "splash"){
                SplashScreen(controller = controller, )
            }
            composable(route = "home"){
                HomeScreen(controller = controller)
            }
            composable(
                route = "details/{characterId}",
                arguments = listOf(
                    navArgument("characterId"){
                        type = NavType.StringType
                    }
                )
            ){
                val characterId = it.arguments?.getString("characterId")

                /** If there is an argument passed, go to details, or home otherwise */
                if (characterId != null) {
                    DetailsScreen(characterId = characterId, controller = controller)
                } else {
                    HomeScreen(controller = controller)
                }
            }

        }
    }
}
