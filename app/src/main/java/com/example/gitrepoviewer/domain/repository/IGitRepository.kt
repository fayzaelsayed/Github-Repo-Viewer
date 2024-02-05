package com.example.gitrepoviewer.domain.repository

import com.example.gitrepoviewer.data.local.entities.RepoEntity
import com.example.gitrepoviewer.domain.model.RepoResponseModel
import kotlinx.coroutines.flow.Flow

interface IGitRepository {

    suspend fun fetchAllRepos()

    suspend fun addReposToDatabase(reposList: List<RepoResponseModel>)

    fun getAllRepos() : Flow<List<RepoEntity>>

}