package com.opentrivia.app.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.opentrivia.app.lib.Constants
import com.opentrivia.app.lib.datasource.DataManager
import com.opentrivia.app.lib.datasource.remote.mapping.response.model.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class MainUiState(
    val isLoading: Boolean = false,
    val questions: List<Result> = emptyList(),
    val selectedCategory: Int = -1,
    val errorMessage: String? = null
)

class MainViewModel(
    private val dataManager: DataManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    fun loadQuestions(category: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                selectedCategory = category,
                errorMessage = null
            )
            try {
                val cat = if (category <= 0) null else category
                val response = dataManager.getTriviaWithToken(
                    amount = Constants.PAGING_SIZE,
                    category = cat
                )
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    questions = response.results
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Failed to load questions"
                )
            }
        }
    }
}
