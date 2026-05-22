package com.opentrivia.app.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.opentrivia.app.lib.datasource.DataManager
import com.opentrivia.app.lib.datasource.model.QuestionCount
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CatalogUiState(
    val isLoading: Boolean = false,
    val counts: List<QuestionCount> = emptyList(),
    val errorMessage: String? = null
)

class CatalogViewModel(
    private val dataManager: DataManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(CatalogUiState())
    val uiState: StateFlow<CatalogUiState> = _uiState.asStateFlow()

    init {
        loadCounts()
    }

    fun loadCounts() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val counts = dataManager.getCategoriesQuestionCount()
                _uiState.value = _uiState.value.copy(isLoading = false, counts = counts)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Failed to load counts"
                )
            }
        }
    }
}
