package com.example.apollographqlplayground.screens.home


import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apollographqlplayground.Response
import com.example.apollographqlplayground.SeriesCharactersQuery
import com.example.apollographqlplayground.UiState
import com.example.apollographqlplayground.data.AppRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * A View model with hiltViewModel annotation that is used to access this view model everywhere needed
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: AppRepositoryImpl,
) : ViewModel() {

    val characters: MutableList<SeriesCharactersQuery.Result?> = mutableStateListOf()
    val uiState = mutableStateOf<UiState>(UiState.Loading)

    init {
        getCharacters()
    }

    fun getCharacters() {
        /** Show Loading placeholder */
        uiState.value = UiState.Loading
        /** Launch a coroutine for the background task */
        viewModelScope.launch {

            delay(3000)

            repository.getAllCharacters().let{
                when (it) {
                    is Response.Success -> {
                        /** Got a response from the server successfully */
                        uiState.value = UiState.Success
                        it.data?.characters?.results?.let { characters ->
                            /** We got the list of characters */
                            this@HomeViewModel.characters.addAll(characters)
                        }
                    }
                    is Response.Error -> {
                        /** An error happened when fetching data from the server */
                        uiState.value = UiState.Error
                    }
                }
            }
        }
    }
}