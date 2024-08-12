package com.opentrivia.app.lib.datasource.local.sharedpreference

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit


open class BaseSharedPreference(context: Context, prefFileName: String) {

    private val preferences: SharedPreferences = context.getSharedPreferences(prefFileName, Context.MODE_PRIVATE)

    fun clear() {
        preferences.edit {
            clear()
        }
    }

    fun remove(key: String) {
        preferences.edit {
            remove(key)
        }
    }


    fun putBooleanValue(key: String, value: Boolean) {
        preferences.edit {
            putBoolean(key, value)
        }
    }

    fun putFloatValue(key: String, value: Float) {
        preferences.edit {
            putFloat(key, value)
        }
    }

    fun putIntValue(key: String, value: Int) {
        preferences.edit {
            putInt(key, value)
        }
    }

    fun putLongValue(key: String, value: Long) {
        preferences.edit {
            putLong(key, value)
        }
    }

    fun putStringValue(key: String, value: String) {
        preferences.edit {
            putString(key, value)
        }
    }

    fun getBooleanValue(key: String, defValue: Boolean): Boolean {
        return preferences.getBoolean(key, defValue)
    }

    fun getFloatValue(key: String, defValue: Float): Float {
        return preferences.getFloat(key, defValue)
    }

    fun getIntValue(key: String, defValue: Int): Int {
        return preferences.getInt(key, defValue)
    }

    fun getLongValue(key: String, defValue: Long): Long {
        return preferences.getLong(key, defValue)
    }

    fun getStringValue(key: String, defValue: String): String {
        return preferences.getString(key, defValue) ?: defValue
    }


}