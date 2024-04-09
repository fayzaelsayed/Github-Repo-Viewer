package com.example.gitrepoviewer.presentation.repos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gitrepoviewer.common.Resource
import com.example.gitrepoviewer.domain.model.Repository
import com.example.gitrepoviewer.domain.use_case.repo.GetReposUseCase
import com.example.gitrepoviewer.domain.use_case.repo.RequestReposUseCase
import com.example.gitrepoviewer.domain.use_case.repo.SearchReposUseCase
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
class ReposViewModel @Inject constructor(
    private val requestReposUseCase: RequestReposUseCase,
    private val getReposUseCase: GetReposUseCase,
    private val searchReposUseCase: SearchReposUseCase,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    private val _viewState = MutableStateFlow(ReposViewState())
    val viewState: StateFlow<ReposViewState> get() = _viewState

    private val _searchText = MutableStateFlow("")
    val searchText: StateFlow<String> get() = _searchText

    private val _reposList = mutableListOf<Repository>()
    private val _filteredReposList = mutableListOf<Repository>()
    var pageNumber = 0
    var paginate = true

    init {
        fetchRepos()
    }

    fun fetchRepos() {
        viewModelScope.launch(dispatcherProvider.main) {
            pageNumber = 0
            _searchText.value = ""
            requestReposUseCase().flowOn(dispatcherProvider.io).collect {
                when (it) {
                    is Resource.Loading -> _viewState.update { viewState ->
                        viewState.copy(
                            isLoading = it.loading,
                            reposList = null,
                            filteredReposList = null,
                            error = null,
                            isSearching = false,
                            isPaginating = false
                        )
                    }

                    is Resource.Success -> {
                        _reposList.clear()
                        _filteredReposList.clear()
                        _viewState.update { viewState ->
                            viewState.copy(reposList = emptyList(), filteredReposList = emptyList())
                        }
                        getRepos(0)
                    }

                    is Resource.Failure -> {
                        _viewState.update { viewState ->
                            viewState.copy(
                                isLoading = false,
                                reposList = null,
                                filteredReposList = null,
                                error = it.exception.localizedMessage,
                                isSearching = false,
                                isPaginating = false
                            )
                        }
                        delay(1000)
                        getRepos(0)
                    }
                }
            }
        }
    }

    fun getRepos(pageNumber: Int) {
        viewModelScope.launch(dispatcherProvider.main) {
            getReposUseCase(pageNumber).flowOn(dispatcherProvider.io).collect {
                when (it) {
                    is Resource.Loading ->
                        if (pageNumber == 0) {
                            _viewState.update { viewState ->
                                viewState.copy(
                                    isLoading = true,
                                    reposList = emptyList(),
                                    filteredReposList = emptyList(),
                                    error = null,
                                    isSearching = false,
                                    isPaginating = false
                                )
                            }
                        } else {
                            _viewState.update { viewState ->
                                viewState.copy(
                                    isLoading = false,
                                    error = null,
                                    isSearching = false,
                                    isPaginating = true
                                )
                            }
                        }

                    is Resource.Success -> {
                        if (pageNumber == 0) {
                            _reposList.clear()
                            _filteredReposList.clear()
                        } else {
                            delay(1000)
                        }
                        _reposList.addAll(it.data!!)
                        _filteredReposList.addAll(it.data)
                        _viewState.update { viewState ->
                            viewState.copy(
                                isLoading = false,
                                reposList = _reposList.toList(),
                                filteredReposList = _filteredReposList.toList(),
                                error = if (it.data.isEmpty() && pageNumber > 0) "No more data available" else null,
                                isSearching = false,
                                isPaginating = false
                            )
                        }
                        paginate = true
                    }

                    is Resource.Failure -> {
                        _viewState.update { viewState ->
                            viewState.copy(
                                isLoading = false,
                                reposList = null,
                                filteredReposList = null,
                                error = it.exception.localizedMessage,
                                isSearching = false,
                                isPaginating = false
                            )
                        }
                    }
                }
            }
        }
    }

    fun searchWithFakeDelay(text: String) {
        _searchText.value = text
        viewModelScope.launch(dispatcherProvider.main) {
            _viewState.update { viewState ->
                viewState.copy(isSearching = true)
            }
            delay(1000)
            searchRepos(text)
        }
    }

    fun searchRepos(text: String) {
        _viewState.update { viewState ->
            viewState.copy(
                filteredReposList = searchReposUseCase(viewState.reposList!!, text),
                isSearching = false
            )
        }
    }

    fun clearErrorMessage() {
        _viewState.update { viewState ->
            viewState.copy(error = null)
        }
    }

}