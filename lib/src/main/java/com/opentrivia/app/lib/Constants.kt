package com.opentrivia.app.lib


object Constants {
    const val USE_SSL = false
    const val IS_MOCK = false
    const val BASE_URL = "https://opentdb.com/"
    const val PAGING_SIZE = 50
    const val QUIZ_SIZE = 10
    const val COUNT_DOWN_TIMER = 60L
    const val COUNT_DOWN_TEXT = "00:%02d"

    object Api {
        const val CONNECT_TIMEOUT_SEC = 60L
        const val READ_TIMEOUT_SEC = 60L

        // Query parameter name
        const val QUERY_AMOUNT = "amount"
        const val QUERY_CATEGORY = "category"
        const val QUERY_DIFFICULTY = "difficulty"
        const val QUERY_TYPE = "type"
        const val QUERY_ENCODE = "encode"
        const val QUERY_COMMAND = "command"
        const val QUERY_TOKEN = "token"

        // Query parameter value
        const val PARAM_REQUEST = "request"
        const val PARAM_RESET = "reset"
        const val PARAM_EASY = "easy"
        const val PARAM_MEDIUM = "medium"
        const val PARAM_HARD = "hard"
        const val PARAM_MULTIPLE = "multiple"
        const val PARAM_BOOLEAN = "boolean"
        const val PARAM_LEGACY = "urlLegacy"
        const val PARAM_URL_3986 = "url3986"
        const val PARAM_BASE64 = "base64"
    }

    object SharedPref {
        const val PREF_NAME_MAIN = "main_pref"

        object Key {
            const val PREF_TOKEN = "$PREF_NAME_MAIN.token"
            const val PREF_CATEGORIES = "$PREF_NAME_MAIN.categories"
            const val PREF_DARK_MODE = "dark_mode"
        }
    }
}