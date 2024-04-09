package com.example.gitrepoviewer.data.repository.repo_details

import com.example.gitrepoviewer.data.local.dao.RepositoryDetailsDAO
import com.example.gitrepoviewer.data.model.entity.RepositoryDetailsEntity
import com.example.gitrepoviewer.domain.repository.repo_details.IRepoDetailsLocalDS
import javax.inject.Inject

class RepoDetailsLocalDSImpl @Inject constructor(
    private val detailsDAO: RepositoryDetailsDAO
): IRepoDetailsLocalDS {

    override suspend fun saveRepoDetailsInDatabase(repoDetailsEntity: RepositoryDetailsEntity) {
        detailsDAO.addRepoDetails(repoDetailsEntity)
    }

    override suspend fun getRepoDetails(id: Long): RepositoryDetailsEntity? {
        return detailsDAO.getRepoDetails(id)
    }
}