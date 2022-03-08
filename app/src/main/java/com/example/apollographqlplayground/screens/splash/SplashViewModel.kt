package com.example.apollographqlplayground.screens.splash


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * A View model with hiltViewModel annotation that is used to access this view model everywhere needed
 */
@HiltViewModel
class SplashViewModel @Inject constructor() : ViewModel() {

    fun initSplash(
        onSplashFinished: ()-> Unit,
    ){
        /** Delay the screen for just 3 seconds */
        viewModelScope.launch {
            delay(3000)
            onSplashFinished()
        }
    }

}