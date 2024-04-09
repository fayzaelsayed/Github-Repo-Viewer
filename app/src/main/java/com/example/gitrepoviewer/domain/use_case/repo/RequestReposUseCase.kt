package com.example.gitrepoviewer.domain.use_case.repo

import com.example.gitrepoviewer.common.BaseUseCase
import com.example.gitrepoviewer.common.Resource
import com.example.gitrepoviewer.data.mapper.RepoViewerMapper
import com.example.gitrepoviewer.domain.model.Repository
import com.example.gitrepoviewer.domain.repository.repo.IReposRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RequestReposUseCase @Inject constructor(private val repository: IReposRepository) : BaseUseCase() {

    suspend operator fun invoke() : Flow<Resource<List<Repository>>>{
        val dataState = repository.fetchAllRepos()
        dataState.data?.let {
            val mappedReposList = dataState.data.map { RepoViewerMapper.mapRepoDomainToEntity(it) }
            return executeApiRequest(networkCall = {dataState}){ repository.saveReposInDatabase(mappedReposList) }
        } ?: return executeApiRequest(networkCall = {dataState})
    }

}