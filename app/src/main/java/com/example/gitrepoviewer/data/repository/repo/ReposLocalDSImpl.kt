package com.example.gitrepoviewer.data.repository.repo

import com.example.gitrepoviewer.data.local.dao.RepositoryDAO
import com.example.gitrepoviewer.data.model.entity.RepositoryEntity
import com.example.gitrepoviewer.domain.repository.repo.IReposLocalDS
import javax.inject.Inject

class ReposLocalDSImpl @Inject constructor(
    private val repositoryDAO: RepositoryDAO
) : IReposLocalDS {

    override suspend fun getAllRepos(offset: Int, pageSize: Int): List<RepositoryEntity> {
        return repositoryDAO.getAllRepos(offset, pageSize)
    }

    override suspend fun saveRepos(reposList: List<RepositoryEntity>) {
        for (repo in reposList) {
            repositoryDAO.addRepo(repo)
        }
    }
}