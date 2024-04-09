package com.example.gitrepoviewer.presentation.issues

import com.example.gitrepoviewer.domain.model.RepositoryIssues

data class RepoIssuesViewState(
    val isLoading: Boolean = true,
    val issues: List<RepositoryIssues>? = null,
    val error: String? = null
)