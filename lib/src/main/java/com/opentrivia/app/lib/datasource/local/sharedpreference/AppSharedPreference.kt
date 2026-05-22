package com.opentrivia.app.lib.datasource.local.sharedpreference

import android.content.Context
import android.preference.PreferenceManager
import com.opentrivia.app.lib.Constants
import com.opentrivia.app.lib.datasource.remote.mapping.response.ApiCategoryResponseMessage
import com.opentrivia.app.lib.injection.qualifier.ApplicationContext
import javax.inject.Inject
import kotlinx.serialization.json.Json


class AppSharedPreference @Inject constructor(@ApplicationContext val context: Context) :
    BaseSharedPreference(context, Constants.SharedPref.PREF_NAME_MAIN) {

    fun saveToken(token: String) {
        putStringValue(Constants.SharedPref.Key.PREF_TOKEN, token)
    }

    fun retrieveToken() = getStringValue(Constants.SharedPref.Key.PREF_TOKEN, "")

    fun removeToken() {
        remove(Constants.SharedPref.Key.PREF_TOKEN)
    }

    fun saveCategories(categories: String) {
        putStringValue(Constants.SharedPref.Key.PREF_CATEGORIES, categories)
    }

    fun retrieveCategories(): MutableList<Pair<String, String>> {
        val categoryList = mutableListOf<Pair<String, String>>()
        val value = getStringValue(Constants.SharedPref.Key.PREF_CATEGORIES, "")
        if (value.isNotBlank()) {
            val categories = Json.decodeFromString<ApiCategoryResponseMessage>(value)
            categories.triviaCategories.forEach {
                categoryList.add(it.name to it.id.toString())
            }
        }
        return categoryList
    }

    fun isDarkModeSelected() =
        PreferenceManager.getDefaultSharedPreferences(context).getBoolean(
            Constants.SharedPref.Key.PREF_DARK_MODE,
            false
        )

}