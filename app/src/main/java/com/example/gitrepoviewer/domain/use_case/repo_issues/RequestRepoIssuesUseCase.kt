package com.example.gitrepoviewer.domain.use_case.repo_issues

import com.example.gitrepoviewer.common.BaseUseCase
import com.example.gitrepoviewer.common.Resource
import com.example.gitrepoviewer.data.mapper.RepoViewerMapper
import com.example.gitrepoviewer.domain.model.RepositoryIssues
import com.example.gitrepoviewer.domain.repository.repo_issues.IRepoIssuesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RequestRepoIssuesUseCase @Inject constructor(private val repository: IRepoIssuesRepository) : BaseUseCase(){
    suspend operator fun invoke(owner: String, repoName: String, repoId: Long) : Flow<Resource<List<RepositoryIssues>>> {
        val dataState = repository.fetchRepoIssues(owner, repoName)
        dataState.data?.let {
            val mappedIssuesList = dataState.data.map { RepoViewerMapper.mapRepoIssuesDomainToDTO(it) }
            return executeApiRequest(networkCall = {dataState}){ repository.saveRepoIssuesInDatabase(mappedIssuesList, repoId) }
        } ?: return executeApiRequest(networkCall = {dataState})
    }
}