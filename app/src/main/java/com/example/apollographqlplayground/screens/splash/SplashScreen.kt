package com.example.apollographqlplayground.screens.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.apollographqlplayground.R

@Composable
fun SplashScreen(
    splashViewModel: SplashViewModel = hiltViewModel(),
    controller: NavHostController,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        /** Ensure that the call to initSplash() doesn't occur multiple time on Recomposition
         * the @param key1 = true means it will be executed once when it get into the composition
         */
        LaunchedEffect(key1 = true){
            splashViewModel.initSplash {
                /** Removing this screen from our Backstack */
                controller.popBackStack()
                /** Navigate to home screen */
                controller.navigate("home")
            }
        }

        /** The centered logo */
        Image(
            painter = painterResource(id = R.drawable.rick_morty),
            contentDescription = "icon",
            modifier = Modifier
                .size(150.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop,
        )
    }
}