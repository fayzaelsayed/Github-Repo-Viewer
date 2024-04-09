package com.example.gitrepoviewer.data.repository.repo

import com.example.gitrepoviewer.common.DataState
import com.example.gitrepoviewer.data.mapper.RepoViewerMapper
import com.example.gitrepoviewer.data.model.entity.RepositoryEntity
import com.example.gitrepoviewer.domain.model.Repository
import com.example.gitrepoviewer.domain.repository.repo.IReposLocalDS
import com.example.gitrepoviewer.domain.repository.repo.IReposRemoteDS
import com.example.gitrepoviewer.domain.repository.repo.IReposRepository
import javax.inject.Inject

class ReposRepositoryImpl @Inject constructor(
    private val remoteDS: IReposRemoteDS,
    private val localDS: IReposLocalDS
) : IReposRepository {

    override suspend fun fetchAllRepos() : DataState<List<Repository>> {
        return RepoViewerMapper.mapDataStateRepoDtoToDomain(remoteDS.fetchAllRepos())
    }

    override suspend fun saveReposInDatabase(reposList: List<RepositoryEntity>) {
        localDS.saveRepos(reposList)
    }

    override suspend fun getAllRepos(page: Int, pageSize: Int): List<Repository> {
        val offset = page * pageSize
        return localDS.getAllRepos(offset, pageSize).map { RepoViewerMapper.mapRepoEntityToDomain(it) }

    }
}
