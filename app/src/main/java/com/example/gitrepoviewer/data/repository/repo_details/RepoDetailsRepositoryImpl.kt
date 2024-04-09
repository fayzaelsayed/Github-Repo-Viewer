package com.example.gitrepoviewer.data.repository.repo_details

import com.example.gitrepoviewer.common.DataState
import com.example.gitrepoviewer.data.mapper.RepoViewerMapper.mapDataStateDetailsDtoToDomain
import com.example.gitrepoviewer.data.mapper.RepoViewerMapper.mapRepoDetailsEntityToDomain
import com.example.gitrepoviewer.data.model.entity.RepositoryDetailsEntity
import com.example.gitrepoviewer.domain.model.RepositoryDetails
import com.example.gitrepoviewer.domain.repository.repo_details.IRepoDetailsLocalDS
import com.example.gitrepoviewer.domain.repository.repo_details.IRepoDetailsRemoteDS
import com.example.gitrepoviewer.domain.repository.repo_details.IRepoDetailsRepository
import javax.inject.Inject

class RepoDetailsRepositoryImpl @Inject constructor(
    private val remoteDS: IRepoDetailsRemoteDS,
    private val localDS: IRepoDetailsLocalDS
) : IRepoDetailsRepository {

    override suspend fun fetchRepoDetails(
        owner: String,
        repoName: String
    ): DataState<RepositoryDetails> {
        return mapDataStateDetailsDtoToDomain(remoteDS.fetchRepoDetails(owner, repoName))
    }

    override suspend fun saveRepoDetailsInDatabase(repositoryDetailsEntity: RepositoryDetailsEntity) {
        localDS.saveRepoDetailsInDatabase(repositoryDetailsEntity)
    }

    override suspend fun getRepoDetails(id: Long): RepositoryDetails? {
        return mapRepoDetailsEntityToDomain(localDS.getRepoDetails(id))
    }


}