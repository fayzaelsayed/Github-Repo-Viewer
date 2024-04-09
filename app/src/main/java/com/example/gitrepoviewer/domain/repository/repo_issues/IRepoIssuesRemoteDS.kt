package com.example.gitrepoviewer.domain.repository.repo_issues

import com.example.gitrepoviewer.common.DataState
import com.example.gitrepoviewer.data.model.dto.RepositoryIssuesDTO

interface IRepoIssuesRemoteDS {
    suspend fun fetchRepoIssues(owner: String, repoName: String): DataState<List<RepositoryIssuesDTO>>
}