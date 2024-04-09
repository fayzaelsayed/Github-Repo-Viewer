package com.example.gitrepoviewer.domain.repository.repo_details

import com.example.gitrepoviewer.common.DataState
import com.example.gitrepoviewer.data.model.dto.RepositoryDetailsDTO

interface IRepoDetailsRemoteDS {
    suspend fun fetchRepoDetails(owner: String, repoName: String): DataState<RepositoryDetailsDTO>
}