package com.opentrivia.app.lib.datasource.remote.mapping.response.model

import com.google.gson.annotations.SerializedName


class TriviaCategory {

    @SerializedName("id")
    var id: Int = 0
    @SerializedName("name")
    lateinit var name: String

}