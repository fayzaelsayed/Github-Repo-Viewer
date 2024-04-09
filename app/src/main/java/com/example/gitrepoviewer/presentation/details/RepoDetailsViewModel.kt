package com.example.gitrepoviewer.presentation.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gitrepoviewer.common.Resource
import com.example.gitrepoviewer.domain.use_case.repo_details.GetRepoDetailsUseCase
import com.example.gitrepoviewer.domain.use_case.repo_details.RequestRepoDetailsUseCase
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
class RepoDetailsViewModel @Inject constructor(
    private val requestRepositoryDetailsUseCase: RequestRepoDetailsUseCase,
    private val getRepositoryDetailsUseCase: GetRepoDetailsUseCase,
    private val dispatcherProvider: DispatcherProvider,
    savedStateHandle: SavedStateHandle,

    ) : ViewModel() {

    private val _viewState = MutableStateFlow(RepoDetailsViewState())
    val viewState: StateFlow<RepoDetailsViewState> get() = _viewState


    init {
        val ownerName: String = savedStateHandle.get<String>("ownerName")!!
        val repoName: String = savedStateHandle.get<String>("repoName")!!
        val repoId: Long = savedStateHandle.get<Long>("repoId")!!

        fetchRepoDetails(ownerName, repoName, repoId)
    }

     fun fetchRepoDetails(ownerName: String, repoName: String, repoId: Long) {
        viewModelScope.launch(dispatcherProvider.main) {
            requestRepositoryDetailsUseCase(ownerName, repoName).flowOn(dispatcherProvider.io)
                .collect {
                    when (it) {
                        is Resource.Loading -> _viewState.update { viewState ->
                            viewState.copy(isLoading = it.loading, repoDetails = null, error = null)
                        }
                        is Resource.Success -> getRepoDetails(repoId)
                        is Resource.Failure -> {
                            _viewState.update { viewState ->
                                viewState.copy(
                                    isLoading = false,
                                    repoDetails = null,
                                    error = it.exception.localizedMessage
                                )
                            }
                            delay(1000)
                            getRepoDetails(repoId)
                        }
                    }
                }
        }
    }

    fun getRepoDetails(repoId: Long) {
        viewModelScope.launch(dispatcherProvider.main) {
            getRepositoryDetailsUseCase(repoId).flowOn(dispatcherProvider.io)
                .collect { detailsResource ->
                    when (detailsResource) {
                        is Resource.Loading -> _viewState.update { viewState ->
                            viewState.copy(
                                isLoading = detailsResource.loading,
                                repoDetails = null,
                                error = null
                            )
                        }

                        is Resource.Success -> _viewState.update { viewState ->
                            viewState.copy(
                                isLoading = false,
                                repoDetails = detailsResource.data,
                                error = null
                            )
                        }

                        is Resource.Failure -> _viewState.update { viewState ->
                            viewState.copy(
                                isLoading = false,
                                repoDetails = null,
                                error = detailsResource.exception.localizedMessage
                            )
                        }
                    }
                }
        }

    }


    fun clearErrorMessage() {
        _viewState.update { viewState ->
            viewState.copy(error = null)
        }
    }

}