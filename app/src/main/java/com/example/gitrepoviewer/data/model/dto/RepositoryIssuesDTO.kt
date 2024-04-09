package com.example.gitrepoviewer.data.model.dto

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class RepositoryIssuesDTO (
    val id: Long? = null,

    val state: String? = null,
    val title: String? = null,
        val user: OwnerDTO? = null,

    @SerializedName("created_at")
    val createdAt: String? = null
)
