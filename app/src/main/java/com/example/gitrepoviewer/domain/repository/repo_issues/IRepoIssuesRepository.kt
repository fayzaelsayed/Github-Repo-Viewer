package com.example.gitrepoviewer.domain.repository.repo_issues

import com.example.gitrepoviewer.common.DataState
import com.example.gitrepoviewer.data.model.dto.RepositoryIssuesDTO
import com.example.gitrepoviewer.domain.model.RepositoryIssues

interface IRepoIssuesRepository {
    suspend fun fetchRepoIssues(owner: String, repoName: String): DataState<List<RepositoryIssues>>
    suspend fun saveRepoIssuesInDatabase(repoIssues: List<RepositoryIssuesDTO>, repoId: Long)
    suspend fun getRepoIssues(id: Long): List<RepositoryIssues>
}