package com.example.gitrepoviewer.data.repository.repo_details

import com.example.gitrepoviewer.common.BaseDataSource
import com.example.gitrepoviewer.common.DataState
import com.example.gitrepoviewer.data.model.dto.RepositoryDetailsDTO
import com.example.gitrepoviewer.data.remote.GitHubApiService
import com.example.gitrepoviewer.domain.repository.repo_details.IRepoDetailsRemoteDS
import javax.inject.Inject

class RepoDetailsRemoteDSImpl @Inject constructor(
    private val gitHubApiService: GitHubApiService
) : BaseDataSource(), IRepoDetailsRemoteDS {

    override suspend fun fetchRepoDetails(
        owner: String,
        repoName: String
    ): DataState<RepositoryDetailsDTO> {
        return getResult { gitHubApiService.getRepoDetails(owner, repoName) }
    }
}