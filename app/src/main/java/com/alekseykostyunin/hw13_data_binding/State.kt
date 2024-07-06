package com.alekseykostyunin.hw13_data_binding

sealed class State(
    val isLoading: Boolean = false,
    open val textError: String? = null,
) {
    data object Initial : State()
    data object Loading : State(isLoading = true)
    data object Success : State()
    data class Error(
        override val textError: String?
    ) : State(
        textError = textError
    )
}