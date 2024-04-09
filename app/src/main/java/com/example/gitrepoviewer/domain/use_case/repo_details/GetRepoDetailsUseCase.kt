package com.example.gitrepoviewer.domain.use_case.repo_details

import com.example.gitrepoviewer.common.BaseUseCase
import com.example.gitrepoviewer.domain.repository.repo_details.IRepoDetailsRepository
import javax.inject.Inject

class GetRepoDetailsUseCase @Inject constructor(private val repository: IRepoDetailsRepository) : BaseUseCase(){
    suspend operator fun invoke(id : Long) = executeDatabaseRequest { repository.getRepoDetails(id) }
}