package com.ivanb.jobprepapp

sealed class UiState {
    object Loading : UiState()
    data class Success(val data: List<Book>) : UiState()
    data class Error(val message: String) : UiState()
}