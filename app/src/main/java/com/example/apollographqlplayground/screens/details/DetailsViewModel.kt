package com.example.apollographqlplayground.screens.details


import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apollographqlplayground.CharacterDetailsQuery
import com.example.apollographqlplayground.Response
import com.example.apollographqlplayground.UiState
import com.example.apollographqlplayground.data.AppRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * A View model with hiltViewModel annotation that is used to access this view model everywhere needed
 */
@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val repositoryImpl: AppRepositoryImpl
) : ViewModel() {
    val uiState = mutableStateOf<UiState>(UiState.Loading)
    private val _character = MutableLiveData<CharacterDetailsQuery.Character>()
    val character: LiveData<CharacterDetailsQuery.Character> = _character

    /** It takes $characterId as a @param which is the id of character
     * It updates the value of $_character to the corresponding character
     */
    fun getCharacterDetails(characterId: String){
        uiState.value = UiState.Loading
        viewModelScope.launch {
            repositoryImpl.getCharacterDetails(characterId = characterId).let {
                when (it) {
                    is Response.Success -> {
                        /** Got a response from the server successfully */
                        uiState.value = UiState.Success
                        _character.value = it.data?.character
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