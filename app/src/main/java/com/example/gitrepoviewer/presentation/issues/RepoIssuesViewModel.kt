package com.example.gitrepoviewer.presentation.issues

import androidx.lifecycle.ViewModel
import com.example.gitrepoviewer.data.local.entities.RepoDetailsEntity
import com.example.gitrepoviewer.data.repository.DetailsRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class RepoIssuesViewModel @Inject constructor(
    private val detailsRepositoryImpl: DetailsRepositoryImpl
) : ViewModel() {

    fun getRepoIssues(id: Long): Flow<RepoDetailsEntity?> = detailsRepositoryImpl.getRepoIssues(id)
}