package com.example.gitrepoviewer.data.repository

import com.example.gitrepoviewer.R
import com.example.gitrepoviewer.data.local.dao.RepoDAO
import com.example.gitrepoviewer.data.local.entities.RepoEntity
import com.example.gitrepoviewer.data.remote.GitHubApi
import com.example.gitrepoviewer.domain.model.RepoResponseModel
import com.example.gitrepoviewer.domain.repository.IGitRepository
import com.example.gitrepoviewer.util.UtilFunctions
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.transform
import javax.inject.Inject

class GitRepositoryImpl @Inject constructor(
    private val gitHubApi: GitHubApi,
    private val repoDAO: RepoDAO,
    private val utilFunctions: UtilFunctions
) : IGitRepository {

    val isFetchingDone = MutableStateFlow(false)
    val errorMessageResourceId = MutableStateFlow<Int?>(null)

    override suspend fun fetchAllRepos() {
        try {
            if (utilFunctions.isInternetWorking()) {
                val response = gitHubApi.getRepositories()
                if (response.isSuccessful) {
                    val reposList = response.body()
                    if (!reposList.isNullOrEmpty()) {
                        addReposToDatabase(reposList)
                    } else {
                        errorMessageResourceId.value = R.string.no_repositories
                    }
                } else {
                    errorMessageResourceId.value = R.string.request_not_successful
                }
            } else {
                errorMessageResourceId.value = R.string.no_internet_connection
            }
        } catch (e: Exception) {
            e.printStackTrace()
            errorMessageResourceId.value = R.string.request_not_successful
        } finally {
            isFetchingDone.value = true
        }
    }

    override suspend fun addReposToDatabase(reposList: List<RepoResponseModel>) {
        for (repo in reposList) {
            val repoEntity = repo.modelToEntity()
            repoDAO.addRepo(repoEntity)
        }
    }

    override fun getAllRepos(): Flow<List<RepoEntity>> {
        return repoDAO.getAllRepos()
            .transform { emit(it) } // Transform to Flow
            .combine(isFetchingDone) { repos, fetchingDone ->
                if (fetchingDone) {
                    repos
                } else {
                    emptyList()
                }
            }
    }
}
