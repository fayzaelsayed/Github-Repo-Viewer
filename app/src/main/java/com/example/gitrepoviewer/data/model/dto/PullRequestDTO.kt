package com.example.gitrepoviewer.data.model.dto

import com.google.gson.annotations.SerializedName

data class PullRequestDTO (
    val url: String,

    @SerializedName("html_url")
    val htmlURL: String,

    @SerializedName("diff_url")
    val diffURL: String,

    @SerializedName("patch_url")
    val patchURL: String,

    @SerializedName("merged_at")
    val mergedAt: Any? = null
)
