package com.example.gitrepoviewer.domain.model

import com.example.gitrepoviewer.data.model.dto.OwnerDTO
import com.example.gitrepoviewer.data.model.dto.RepositoryIssuesDTO

data class RepositoryDetails(
    val id: Long? = null,
    val name: String? = null,
    val fullName: String? = null,
    val owner: OwnerDTO? = null,
    val description: String? = null,
    val forksCount: Long? = null,
    val stargazersCount: Long? = null,
    val openIssuesCount: Long? = null,
    val createdAt: String? = null,
    val subscribersCount: Long? = null,
    val repo_issues : List<RepositoryIssuesDTO>
)
