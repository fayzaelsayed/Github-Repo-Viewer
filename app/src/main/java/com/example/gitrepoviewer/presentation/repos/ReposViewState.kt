package com.example.gitrepoviewer.presentation.repos

import com.example.gitrepoviewer.domain.model.Repository

data class ReposViewState(
    val isLoading: Boolean = true,
    val reposList: List<Repository>? = null,
    val error: String? = null,
    val isSearching: Boolean = false,
    val filteredReposList: List<Repository>? = null,
    val isPaginating: Boolean = false
)
