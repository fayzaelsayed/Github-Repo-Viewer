package com.example.gitrepoviewer.data.repository.repo_issues

import com.example.gitrepoviewer.common.BaseDataSource
import com.example.gitrepoviewer.common.DataState
import com.example.gitrepoviewer.data.model.dto.RepositoryIssuesDTO
import com.example.gitrepoviewer.data.remote.GitHubApiService
import com.example.gitrepoviewer.domain.repository.repo_issues.IRepoIssuesRemoteDS
import javax.inject.Inject

class RepoIssuesRemoteDSImpl @Inject constructor(
    private val gitHubApiService: GitHubApiService
) : BaseDataSource(), IRepoIssuesRemoteDS {

    override suspend fun fetchRepoIssues(
        owner: String,
        repoName: String
    ): DataState<List<RepositoryIssuesDTO>> {
        return getResult { gitHubApiService.getRepoIssues(owner, repoName) }
    }
}