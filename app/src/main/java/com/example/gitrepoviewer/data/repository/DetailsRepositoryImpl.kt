package com.example.gitrepoviewer.data.repository

import com.example.gitrepoviewer.R
import com.example.gitrepoviewer.data.local.dao.DetailsDAO
import com.example.gitrepoviewer.data.local.entities.RepoDetailsEntity
import com.example.gitrepoviewer.data.remote.GitHubApi
import com.example.gitrepoviewer.domain.model.RepoDetailsModel
import com.example.gitrepoviewer.domain.model.RepoIssuesModel
import com.example.gitrepoviewer.domain.repository.IDetailsRepository
import com.example.gitrepoviewer.util.UtilFunctions
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.transform
import javax.inject.Inject

class DetailsRepositoryImpl @Inject constructor(
    private val gitHubApi: GitHubApi,
    private val detailsDAO: DetailsDAO,
    private val utilFunctions: UtilFunctions
) : IDetailsRepository {

    val errorMessageResourceId = MutableStateFlow<Int?>(null)
    val isDetailsFetchingDone = MutableStateFlow(false)

    override suspend fun fetchRepoDetails(owner: String, repoName: String, repoId: Long) {
        try {
            if (utilFunctions.isInternetWorking()) {
                val response = gitHubApi.getRepoDetails(owner, repoName)
                if (response.isSuccessful) {
                    val details = response.body()
                    if (details != null) {
                        addRepoDetailsToDatabase(details)
                    } else {
                        errorMessageResourceId.value = R.string.no_details
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
        }finally {
            isDetailsFetchingDone.value = true
            fetchRepoIssues(owner, repoName, repoId)
        }
    }


    override suspend fun addRepoDetailsToDatabase(repoDetailsModel : RepoDetailsModel){
        detailsDAO.addRepoDetails(repoDetailsModel.fromModelToEntity())
    }

    override fun getRepoDetails(id: Long): Flow<RepoDetailsEntity?> {
        return detailsDAO.getRepoDetails(id)
            .transform { emit(it) } // Transform to Flow
            .combine(isDetailsFetchingDone) { details, fetchingDone ->
                if (fetchingDone) {
                    details
                } else {
                    null
                }
            }
    }

    override suspend fun fetchRepoIssues(owner: String, repoName: String, repoId: Long) {
        try {
            if (utilFunctions.isInternetWorking()) {
                val response = gitHubApi.getRepoIssues(owner, repoName)
                if (response.isSuccessful) {
                    val issuesList = response.body()
                    if (!issuesList.isNullOrEmpty()) {
                        insertRepoIssuesInDatabase(issuesList, repoId)
                    } else {
                        errorMessageResourceId.value = R.string.no_issues
                    }
                } else {
                    errorMessageResourceId.value = R.string.could_not_get_repo_issues
                }
            } else {
                errorMessageResourceId.value = R.string.no_internet_connection
            }
        } catch (e: Exception) {
            e.printStackTrace()
            errorMessageResourceId.value = R.string.could_not_get_repo_issues
        }
    }

    override suspend fun insertRepoIssuesInDatabase(
        repoIssues: List<RepoIssuesModel>,
        repoId: Long
    ) {
        detailsDAO.updateRepoIssues(repoId, repoIssues)
    }

    override fun getRepoIssues(id: Long): Flow<RepoDetailsEntity> {
        return detailsDAO.getRepoDetails(id)
    }
}