package com.sample.test.lib.datasource.local.sharedpreference

import android.content.Context
import android.preference.PreferenceManager
import com.google.gson.Gson
import com.sample.test.lib.Constants
import com.sample.test.lib.datasource.remote.mapping.response.ApiCategoryResponseMessage
import com.sample.test.lib.injection.qualifier.ApplicationContext
import javax.inject.Inject


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
        val categories = Gson().fromJson<ApiCategoryResponseMessage>(value, ApiCategoryResponseMessage::class.java)
        categories.triviaCategories?.let {
            it.forEach {
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