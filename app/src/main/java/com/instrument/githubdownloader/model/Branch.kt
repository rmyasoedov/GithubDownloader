package com.instrument.githubdownloader.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Branch (
    @SerializedName("name")
    @Expose
    val name: String? = null,

    @SerializedName("protected")
    @Expose
    val protected: Boolean? = null,
): Serializable