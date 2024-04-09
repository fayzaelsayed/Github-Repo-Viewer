package com.example.gitrepoviewer.data.repository.repo

import com.example.gitrepoviewer.common.BaseDataSource
import com.example.gitrepoviewer.common.DataState
import com.example.gitrepoviewer.data.model.dto.RepositoryDTO
import com.example.gitrepoviewer.data.remote.GitHubApiService
import com.example.gitrepoviewer.domain.repository.repo.IReposRemoteDS
import javax.inject.Inject

class ReposRemoteDSImpl @Inject constructor(
    private val gitHubApiService: GitHubApiService
) : BaseDataSource(), IReposRemoteDS {

    override suspend fun fetchAllRepos(): DataState<List<RepositoryDTO>> {
        return getResult { gitHubApiService.getRepositories() }
    }
}