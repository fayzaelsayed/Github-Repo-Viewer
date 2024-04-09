package com.example.gitrepoviewer.presentation.details

import com.example.gitrepoviewer.domain.model.RepositoryDetails

data class RepoDetailsViewState (
    val isLoading: Boolean = true,
    val repoDetails: RepositoryDetails? = null,
    val error: String? = null
)