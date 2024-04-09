package com.example.gitrepoviewer.data.repository.repo_issues

import com.example.gitrepoviewer.common.DataState
import com.example.gitrepoviewer.data.mapper.RepoViewerMapper.mapDataStateIssueDtoToDomain
import com.example.gitrepoviewer.data.mapper.RepoViewerMapper.mapRepoIssuesDtoToDomain
import com.example.gitrepoviewer.data.model.dto.RepositoryIssuesDTO
import com.example.gitrepoviewer.domain.model.RepositoryIssues
import com.example.gitrepoviewer.domain.repository.repo_issues.IRepoIssuesLocalDS
import com.example.gitrepoviewer.domain.repository.repo_issues.IRepoIssuesRemoteDS
import com.example.gitrepoviewer.domain.repository.repo_issues.IRepoIssuesRepository
import javax.inject.Inject

class RepoIssuesRepositoryImpl @Inject constructor(
    private val remoteDS: IRepoIssuesRemoteDS,
    private val localDS: IRepoIssuesLocalDS
) : IRepoIssuesRepository{

    override suspend fun fetchRepoIssues(
        owner: String,
        repoName: String
    ): DataState<List<RepositoryIssues>> {
        return mapDataStateIssueDtoToDomain(remoteDS.fetchRepoIssues(owner, repoName))
    }

    override suspend fun saveRepoIssuesInDatabase(repoIssues: List<RepositoryIssuesDTO>, repoId: Long) {
        localDS.saveRepoIssuesInDatabase(repoIssues, repoId)
    }

    override suspend fun getRepoIssues(id: Long): List<RepositoryIssues> {
        return localDS.getRepoIssues(id).map { mapRepoIssuesDtoToDomain(it) }
    }


}