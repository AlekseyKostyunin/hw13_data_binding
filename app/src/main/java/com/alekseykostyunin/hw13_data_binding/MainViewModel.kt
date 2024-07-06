package com.alekseykostyunin.hw13_data_binding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

private const val TAG = "MainViewModel"

class MainViewModel : ViewModel() {

    private val _state = MutableStateFlow<State>(State.Initial)
    val state = _state.asStateFlow()

    private val isSearching = MutableStateFlow(false)
    private var searchJob: Job? = null

    val textSearch = MutableStateFlow("")
    val textResult = MutableStateFlow("Здесь будут отображаться результаты поиска")

    init {
        textSearch.debounce(300).onEach { query ->
            if (query.length > 3) search(textSearch.value)
        }.launchIn(viewModelScope)
    }

    private fun search(text: String) {
        textResult.value = ""
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            isSearching.value = true
            _state.value = State.Loading
            delay(3000)
            _state.value = State.Success
            textResult.value = "По вашему запросу \"$text\" ничего не найдено"
            isSearching.value = false
        }
    }

}