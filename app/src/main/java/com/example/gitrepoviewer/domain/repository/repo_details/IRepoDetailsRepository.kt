package com.example.gitrepoviewer.domain.repository.repo_details

import com.example.gitrepoviewer.common.DataState
import com.example.gitrepoviewer.data.model.entity.RepositoryDetailsEntity
import com.example.gitrepoviewer.domain.model.RepositoryDetails

interface IRepoDetailsRepository {
    suspend fun fetchRepoDetails(owner: String, repoName: String): DataState<RepositoryDetails>
    suspend fun saveRepoDetailsInDatabase(repositoryDetailsEntity: RepositoryDetailsEntity)
    suspend fun getRepoDetails(id: Long): RepositoryDetails?
}