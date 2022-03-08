package com.example.apollographqlplayground


sealed class UiState {
    object Loading : UiState()
    object Success : UiState()
    object Error : UiState()
}
