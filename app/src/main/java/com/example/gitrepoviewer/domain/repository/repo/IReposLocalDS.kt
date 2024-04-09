package com.example.gitrepoviewer.domain.repository.repo

import com.example.gitrepoviewer.data.model.entity.RepositoryEntity


interface IReposLocalDS {
    suspend fun getAllRepos(offset: Int, pageSize: Int) : List<RepositoryEntity>
    suspend fun saveRepos(reposList: List<RepositoryEntity>)
}