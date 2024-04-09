package com.example.gitrepoviewer.data.repository.repo_issues

import com.example.gitrepoviewer.data.local.dao.RepositoryDetailsDAO
import com.example.gitrepoviewer.data.model.dto.RepositoryIssuesDTO
import com.example.gitrepoviewer.domain.repository.repo_issues.IRepoIssuesLocalDS
import javax.inject.Inject

class RepoIssuesLocalDSImpl @Inject constructor(
    private val repositoryDetailsDAO: RepositoryDetailsDAO
) : IRepoIssuesLocalDS {

    override suspend fun getRepoIssues(id: Long): List<RepositoryIssuesDTO> {
        return repositoryDetailsDAO.getRepoDetails(id)?.repoIssues ?: emptyList()
    }

    override suspend fun saveRepoIssuesInDatabase(
        repoIssues: List<RepositoryIssuesDTO>,
        repoId: Long
    ) {
        repositoryDetailsDAO.updateRepoIssues(repoId, repoIssues)
    }
}