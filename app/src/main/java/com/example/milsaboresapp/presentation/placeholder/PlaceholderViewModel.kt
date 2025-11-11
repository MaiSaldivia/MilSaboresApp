package com.example.milsaboresapp.presentation.placeholder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PlaceholderViewModel : ViewModel() {
    @Suppress("UNCHECKED_CAST")
    class Factory : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T = PlaceholderViewModel() as T
    }
}
