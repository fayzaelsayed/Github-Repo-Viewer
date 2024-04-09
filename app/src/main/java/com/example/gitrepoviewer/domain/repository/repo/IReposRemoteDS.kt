package com.example.gitrepoviewer.domain.repository.repo

import com.example.gitrepoviewer.common.DataState
import com.example.gitrepoviewer.data.model.dto.RepositoryDTO

interface IReposRemoteDS {
    suspend fun fetchAllRepos() : DataState<List<RepositoryDTO>>
}