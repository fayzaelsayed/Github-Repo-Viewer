package com.example.gitrepoviewer.domain.repository

import com.example.gitrepoviewer.data.local.entities.RepoDetailsEntity
import com.example.gitrepoviewer.domain.model.RepoDetailsModel
import com.example.gitrepoviewer.domain.model.RepoIssuesModel
import kotlinx.coroutines.flow.Flow

interface IDetailsRepository {
    suspend fun fetchRepoDetails(owner: String, repoName: String, repoId: Long)
    suspend fun addRepoDetailsToDatabase(repoDetailsModel : RepoDetailsModel)
    fun getRepoDetails(id: Long): Flow<RepoDetailsEntity?>
    suspend fun fetchRepoIssues(owner: String, repoName: String, repoId: Long)
    suspend fun insertRepoIssuesInDatabase(repoIssues: List<RepoIssuesModel>, repoId: Long)
    fun getRepoIssues(id: Long): Flow<RepoDetailsEntity>
}