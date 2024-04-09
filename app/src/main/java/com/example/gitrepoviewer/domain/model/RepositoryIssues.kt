package com.example.gitrepoviewer.domain.model

import com.example.gitrepoviewer.data.model.dto.OwnerDTO

data class RepositoryIssues(
    val id: Long? = null,
    val state: String? = null,
    val title: String? = null,
    val user: OwnerDTO? = null,
    val createdAt: String? = null,
)
