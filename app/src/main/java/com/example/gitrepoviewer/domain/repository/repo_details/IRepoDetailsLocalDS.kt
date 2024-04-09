package com.example.gitrepoviewer.domain.repository.repo_details

import com.example.gitrepoviewer.data.model.entity.RepositoryDetailsEntity


interface IRepoDetailsLocalDS {
    suspend fun saveRepoDetailsInDatabase(repositoryDetailsEntity: RepositoryDetailsEntity)
    suspend fun getRepoDetails(id: Long): RepositoryDetailsEntity?
}