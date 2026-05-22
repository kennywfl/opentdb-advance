package com.opentrivia.app.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.opentrivia.app.lib.datasource.DataManager
import com.opentrivia.app.lib.datasource.local.sharedpreference.AppSharedPreference
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

data class LauncherUiState(
    val isLoading: Boolean = false,
    val isReady: Boolean = false,
    val errorMessage: String? = null
)

class LauncherViewModel(
    private val dataManager: DataManager,
    private val appSharedPreference: AppSharedPreference
) : ViewModel() {

    private val _uiState = MutableStateFlow(LauncherUiState())
    val uiState: StateFlow<LauncherUiState> = _uiState.asStateFlow()

    init {
        fetchCategories()
    }

    fun fetchCategories() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val categories = dataManager.getTriviaCategories()
                val categoryJson = Json.encodeToString(
                    com.opentrivia.app.lib.datasource.remote.mapping.response.ApiCategoryResponseMessage.serializer(),
                    categories
                )
                appSharedPreference.saveCategories(categoryJson)
                _uiState.value = _uiState.value.copy(isLoading = false, isReady = true)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Failed to load categories"
                )
            }
        }
    }
}
