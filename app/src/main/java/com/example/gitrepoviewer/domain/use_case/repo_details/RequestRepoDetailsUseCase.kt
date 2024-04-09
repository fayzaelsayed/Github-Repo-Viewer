package com.example.gitrepoviewer.domain.use_case.repo_details

import com.example.gitrepoviewer.common.BaseUseCase
import com.example.gitrepoviewer.common.Resource
import com.example.gitrepoviewer.data.mapper.RepoViewerMapper.mapRepoDetailsDomainToEntity
import com.example.gitrepoviewer.domain.model.RepositoryDetails
import com.example.gitrepoviewer.domain.repository.repo_details.IRepoDetailsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RequestRepoDetailsUseCase @Inject constructor(private val repository: IRepoDetailsRepository) : BaseUseCase(){
    suspend operator fun invoke(owner: String, repoName: String) : Flow<Resource<RepositoryDetails>> {
        val dataState = repository.fetchRepoDetails(owner, repoName)
        dataState.data?.let {
            val detailsEntity = mapRepoDetailsDomainToEntity(dataState.data)
            return executeApiRequest(networkCall = {dataState}){ repository.saveRepoDetailsInDatabase(detailsEntity) }
        } ?:  return executeApiRequest(networkCall = {dataState})
    }
}