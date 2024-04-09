package com.example.gitrepoviewer.data.model.dto

import com.google.gson.annotations.SerializedName

data class RepositoryDTO(
    val id: Long? = null,

    val name: String? = null,

    @SerializedName("full_name")
    val fullName: String? = null,

    val owner: OwnerDTO? = null,

    val description: String? = null
)
