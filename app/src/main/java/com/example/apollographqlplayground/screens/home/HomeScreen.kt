package com.example.apollographqlplayground.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.example.apollographqlplayground.UiState
import com.google.accompanist.placeholder.*

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel(),
    controller: NavHostController,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        /** The current state of the screen , e.g: Loading or Success or Error state */
        val uiState by remember { homeViewModel.uiState }
        /** The list of characters that we had fetched from remote */
        val characters = homeViewModel.characters

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = "Rick & Morty Characters",
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp,
        )
        LazyColumn{
            when(uiState){
                is UiState.Loading -> {
                    /** Still loading */
                    items(10){
                        CharacterLayoutPH()
                    }
                }
                is UiState.Success -> {
                    /** Loading finished successfully ! */
                    items(characters){
                        it?.let {character->
                            CharacterLayout(
                                image = character.image ?: "",
                                episodes = character.episode.size,
                                name = character.name ?: "Name Unknown",
                                origin = character.origin?.name ?: "Unknown",
                                onCharacterClicked = {
                                    /** Handle event of clicking on a character */
                                    controller.navigate("details/${character.id}")
                                }
                            )
                        }
                    }
                }
                is UiState.Error -> {
                    /** An error occur */
                    item{
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(24.dp)
                        ){
                            Text(
                                text = "Connection failed, please make sure you are connected to the internet & try again!",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = Color.Gray,
                                textAlign = TextAlign.Center,
                            )
                            Button(
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = Color.Red,
                                    contentColor = Color.White,
                                ),
                                onClick = {
                                    /** Try fetching characters again ! */
                                    homeViewModel.getCharacters()
                                },
                                shape = RoundedCornerShape(10.dp),
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                                content = {
                                    Icon(
                                        imageVector = Icons.Rounded.Refresh,
                                        contentDescription = "retry",
                                    )
                                    Spacer(modifier = Modifier.width(16.dp))
                                    Text(
                                        text = "Try again",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 20.sp,
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CharacterLayout(
    image: String,
    episodes: Int,
    name: String,
    origin: String,
    onCharacterClicked: ()-> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .clickable {
                onCharacterClicked()
            }
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ){
        Image(
            painter = rememberImagePainter(data = image),
            contentDescription = "icon",
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(10.dp)),
            contentScale = ContentScale.Crop,
        )
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = name,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = "Appeared in $episodes episodes",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
            )
            Text(
                text = "Origin: $origin",
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
            )
        }
    }
}

@Composable
fun CharacterLayoutPH() {
    val shimmer = PlaceholderHighlight.shimmer(
        highlightColor = Color.Gray,
    )

    Row(
        modifier = Modifier
            .background(Color.White)
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ){
        Box(
            modifier = Modifier
                .size(64.dp)
                .placeholder(
                    visible = true,
                    shape = RoundedCornerShape(10.dp),
                    color = Color.LightGray,
                    highlight = shimmer
                ),
        )
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(16.dp)
                    .placeholder(
                        visible = true,
                        shape = RoundedCornerShape(10.dp),
                        color = Color.LightGray,
                        highlight = shimmer
                    ),
            )
            Spacer(
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(13.dp)
                    .placeholder(
                        visible = true,
                        shape = RoundedCornerShape(10.dp),
                        color = Color.LightGray,
                        highlight = shimmer
                    ),
            )
            Spacer(
                modifier = Modifier
                    .fillMaxWidth(0.4f)
                    .height(10.dp)
                    .placeholder(
                        visible = true,
                        shape = RoundedCornerShape(10.dp),
                        color = Color.LightGray,
                        highlight = shimmer
                    ),
            )
        }
    }
}