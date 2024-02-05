package com.example.gitrepoviewer.domain.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class RepoIssuesModel (
    val id: Long? = null,

    val state: String? = null,
    val title: String? = null,
    val user: Owner? = null,

    @SerializedName("created_at")
    val createdAt: String? = null,
)
