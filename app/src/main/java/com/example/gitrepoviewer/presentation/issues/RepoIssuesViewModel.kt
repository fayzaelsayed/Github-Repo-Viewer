package com.example.gitrepoviewer.presentation.issues

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gitrepoviewer.common.Resource
import com.example.gitrepoviewer.domain.use_case.repo_issues.GetRepoIssuesUseCase
import com.example.gitrepoviewer.domain.use_case.repo_issues.RequestRepoIssuesUseCase
import com.example.gitrepoviewer.util.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RepoIssuesViewModel @Inject constructor(
    private val requestRepositoryIssuesUseCase: RequestRepoIssuesUseCase,
    private val getRepositoryIssuesUseCase: GetRepoIssuesUseCase,
    private val dispatcherProvider: DispatcherProvider,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _viewState = MutableStateFlow(RepoIssuesViewState())
    val viewState: StateFlow<RepoIssuesViewState> get() = _viewState

    init {
        val ownerName: String = savedStateHandle.get<String>("ownerName")!!
        val repoName: String = savedStateHandle.get<String>("repoName")!!
        val repoId: Long = savedStateHandle.get<Long>("repoId")!!
        requestRepoIssues(ownerName, repoName, repoId)
    }

    fun requestRepoIssues(owner: String, repoName: String, repoId: Long){
        viewModelScope.launch(dispatcherProvider.main){
            requestRepositoryIssuesUseCase(owner, repoName, repoId).flowOn(dispatcherProvider.io).collect{
                when(it){
                    is Resource.Loading -> _viewState.update {  viewState ->
                        viewState.copy(isLoading = it.loading, issues = null, error = null)
                    }
                    is Resource.Success -> getRepoIssues(repoId)
                    is Resource.Failure -> {
                        _viewState.update {  viewState ->
                            viewState.copy(isLoading = true, issues = null, error = it.exception.localizedMessage)
                        }
                        delay(1000)
                        getRepoIssues(repoId)
                    }
                }
            }
        }
    }

    fun getRepoIssues(repoId: Long) {
        viewModelScope.launch(dispatcherProvider.main){
            getRepositoryIssuesUseCase(repoId).flowOn(dispatcherProvider.io).collect{ issuesResource ->
                when(issuesResource){
                    is Resource.Loading -> _viewState.update { viewState ->
                        viewState.copy(isLoading = issuesResource.loading, issues = null, error = null)
                    }
                    is Resource.Success -> _viewState.update {  viewState ->
                        viewState.copy(isLoading = false, issues = issuesResource.data, error = null)
                    }
                    is Resource.Failure -> _viewState.update { viewState ->
                        viewState.copy(isLoading = false, issues = null, error = issuesResource.exception.localizedMessage)
                    }
                }
            }
        }
    }

    fun clearErrorMessage(){
        _viewState.update { viewState ->
            viewState.copy(error = null)
        }
    }
}