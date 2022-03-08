package com.example.apollographqlplayground.screens.details

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
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
import com.example.apollographqlplayground.CharacterDetailsQuery
import com.example.apollographqlplayground.SeriesCharactersQuery
import com.example.apollographqlplayground.UiState
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.placeholder.shimmer

@Composable
fun DetailsScreen(
    characterId: String,
    detailsViewModel: DetailsViewModel = hiltViewModel(),
    controller: NavHostController,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        /** The current state of the screen , e.g: Loading or Success or Error state */
        val uiState by remember{ detailsViewModel.uiState }
        /** The details of the character that we had fetched from remote */
        val character by detailsViewModel.character.observeAsState()

        /** Ensure that the call to initSplash() doesn't occur multiple time on Recomposition
         * the @param key1 = true means it will be executed once when it get into the composition
         */
        LaunchedEffect(key1 = true){
            detailsViewModel.getCharacterDetails(characterId = characterId)
        }

        /** Our secondary TopBar, used to navigate back */
        SecondaryTopBar {
            controller.popBackStack()
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(state = rememberScrollState())
        ){
            when(uiState){
                is UiState.Loading -> {
                    /** Still loading */
                    ProfileHeaderPH()
                    EpisodesSectionPH()
                }
                is UiState.Success -> {
                    /** Loading finished successfully ! */
                    character?.let {
                        /** Profile Header which contains character's basic info */
                        ProfileHeader(
                            image = it.image ?: "",
                            name = it.name ?: "Unknown Character",
                            origin = it.origin?.name ?: "Unknown origin"
                        )
                        /** The list of the episodes that this character has appeared in */
                        EpisodesSection(episodes = it.episode)
                    }
                }
                is UiState.Error -> {
                    /** An error occur */
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
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
                                /** Try fetching character's details again ! */
                                detailsViewModel.getCharacterDetails(characterId = characterId)
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

@Composable
fun EpisodesSection(
    episodes: List<CharacterDetailsQuery.Episode?>
) {
    Text(
        modifier = Modifier.padding(vertical = 16.dp),
        text = "In episodes",
        fontWeight = FontWeight.Bold,
        fontSize = 26.sp,
    )
    episodes.filterNotNull().forEachIndexed{ index, episode ->
        EpisodeItem(
            index = index,
            name = "${episode.episode} : ${episode.name}",
            airedOn = episode.air_date ?: "xx-xx-xxxx"
        )
    }
}

@Composable
fun EpisodesSectionPH() {
    val shimmer = PlaceholderHighlight.shimmer(
        highlightColor = Color.Gray,
    )
    Spacer(
        modifier = Modifier
            .padding(vertical = 16.dp)
            .fillMaxWidth(0.4f)
            .height(26.dp)
            .placeholder(
                visible = true,
                shape = RoundedCornerShape(10.dp),
                color = Color.LightGray,
                highlight = shimmer
            ),
    )
    for(x in 0..10){
        EpisodeItemPH()
    }
}

@Composable
fun EpisodeItem(index: Int, name: String, airedOn: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.Top,
    ){
        Text(
            text = "$index.",
            fontWeight = FontWeight.ExtraBold,
            fontSize = 18.sp,
        )
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = name,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
            )
            Text(
                text = "Aired on $airedOn",
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
            )
        }
    }
}

@Composable
fun EpisodeItemPH() {
    val shimmer = PlaceholderHighlight.shimmer(
        highlightColor = Color.Gray,
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.Top,
    ){
        Spacer(
            modifier = Modifier
                .width(36.dp)
                .height(18.dp)
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
                    .fillMaxWidth(0.6f)
                    .height(18.dp)
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
                    .height(15.dp)
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

@Composable
fun ProfileHeader(
   image: String,
   name: String,
   origin: String,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        Image(
            painter = rememberImagePainter(data = image),
            contentDescription = "icon",
            modifier = Modifier
                .size(200.dp)
                .clip(RoundedCornerShape(16.dp))
                .padding(bottom = 8.dp),
            contentScale = ContentScale.Crop,
        )

        Text(
            text = name,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
        )

        Text(
            text = "Origin: $origin",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
        )
    }
}

@Composable
fun ProfileHeaderPH() {
    val shimmer = PlaceholderHighlight.shimmer(
        highlightColor = Color.Gray,
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        Box(
            modifier = Modifier
                .size(200.dp)
                .placeholder(
                    visible = true,
                    shape = RoundedCornerShape(16.dp),
                    color = Color.LightGray,
                    highlight = shimmer
                )
                .padding(bottom = 8.dp),
        )

        Spacer(
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .height(24.dp)
                .placeholder(
                    visible = true,
                    shape = RoundedCornerShape(10.dp),
                    color = Color.LightGray,
                    highlight = shimmer
                ),
        )

        Spacer(
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .height(16.dp)
                .placeholder(
                    visible = true,
                    shape = RoundedCornerShape(10.dp),
                    color = Color.LightGray,
                    highlight = shimmer
                ),
        )
    }
}

@Composable
fun SecondaryTopBar(
    onBackPress: ()-> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(24.dp),
        verticalAlignment = Alignment.CenterVertically,
    ){
        Icon(
            imageVector = Icons.Rounded.KeyboardArrowLeft,
            contentDescription = "Back button",
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(Color.Black)
                .padding(8.dp)
                .clickable {
                    onBackPress()
                },
            tint = Color.White
        )
        Text(
            text = "Character's Details",
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
        )
    }
}