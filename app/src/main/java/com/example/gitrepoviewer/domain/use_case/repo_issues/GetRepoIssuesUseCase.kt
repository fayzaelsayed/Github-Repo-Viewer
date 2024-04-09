package com.example.gitrepoviewer.domain.use_case.repo_issues

import com.example.gitrepoviewer.common.BaseUseCase
import com.example.gitrepoviewer.domain.repository.repo_issues.IRepoIssuesRepository
import javax.inject.Inject

class GetRepoIssuesUseCase @Inject constructor(private val repository: IRepoIssuesRepository) : BaseUseCase() {
    suspend operator fun invoke(id: Long) = executeDatabaseRequest { repository.getRepoIssues(id) }
}