package com.example.gitrepoviewer.domain.repository.repo_issues

import com.example.gitrepoviewer.data.model.dto.RepositoryIssuesDTO

interface IRepoIssuesLocalDS {
    suspend fun getRepoIssues(id: Long): List<RepositoryIssuesDTO>
    suspend fun saveRepoIssuesInDatabase(repoIssues: List<RepositoryIssuesDTO>, repoId: Long)
}