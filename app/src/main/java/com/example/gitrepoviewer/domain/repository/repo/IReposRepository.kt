package com.example.gitrepoviewer.domain.repository.repo

import com.example.gitrepoviewer.common.DataState
import com.example.gitrepoviewer.data.model.entity.RepositoryEntity
import com.example.gitrepoviewer.domain.model.Repository

interface IReposRepository {

    suspend fun fetchAllRepos(): DataState<List<Repository>>

    suspend fun getAllRepos(page: Int, pageSize: Int): List<Repository>

    suspend fun saveReposInDatabase(reposList: List<RepositoryEntity>)

}