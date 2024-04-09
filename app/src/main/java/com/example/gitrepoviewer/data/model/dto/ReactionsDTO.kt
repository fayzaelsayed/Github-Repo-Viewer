package com.example.gitrepoviewer.data.model.dto

import com.google.gson.annotations.SerializedName

data class ReactionsDTO (
    val url: String,

    @SerializedName("total_count")
    val totalCount: Long,

    @SerializedName("+1")
    val the1: Long,

    @SerializedName("-1")
    val reactions1: Long,

    val laugh: Long,
    val hooray: Long,
    val confused: Long,
    val heart: Long,
    val rocket: Long,
    val eyes: Long
)
