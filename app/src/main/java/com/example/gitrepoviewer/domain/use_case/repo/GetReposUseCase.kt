package com.example.gitrepoviewer.domain.use_case.repo

import com.example.gitrepoviewer.common.BaseUseCase
import com.example.gitrepoviewer.domain.repository.repo.IReposRepository
import javax.inject.Inject

class GetReposUseCase @Inject constructor(private val repository: IReposRepository) : BaseUseCase() {

    suspend operator fun invoke(page: Int) = executeDatabaseRequest { repository.getAllRepos(page, 20) }

}