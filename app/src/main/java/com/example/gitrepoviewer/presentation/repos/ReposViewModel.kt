package com.example.gitrepoviewer.presentation.repos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gitrepoviewer.data.local.entities.RepoEntity
import com.example.gitrepoviewer.data.repository.GitRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReposViewModel @Inject constructor(
    private val gitRepositoryImpl: GitRepositoryImpl
) : ViewModel() {

    val isFetchingDone = gitRepositoryImpl.isFetchingDone.asStateFlow()

    val errorMessageResourceId = gitRepositoryImpl.errorMessageResourceId.asStateFlow()

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private val _repos = MutableStateFlow<List<RepoEntity>>(emptyList())
    val reposState = _repos.asStateFlow()
    val repos = searchText
        .onEach { _isSearching.update { true } }
        .combine(_repos) { text, repos ->
        if (text.isBlank()) {
            repos
        } else {
            delay(1000L)
            repos.filter { it.doesMatchSearchText(text) }
        }
    }
        .onEach { _isSearching.update { false } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), _repos.value)


    init {
        viewModelScope.launch(Dispatchers.IO) {
            gitRepositoryImpl.fetchAllRepos()
            gitRepositoryImpl.getAllRepos().collect { reposList ->
                _repos.value = reposList
            }
        }
    }

    fun retryFetchRepos(){
        viewModelScope.launch(Dispatchers.IO){
            gitRepositoryImpl.isFetchingDone.value = false
            gitRepositoryImpl.fetchAllRepos()
        }
    }


    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

    fun clearErrorMessage() {
        gitRepositoryImpl.errorMessageResourceId.value = null
    }

    fun getReposForTest() = searchText
        .onEach { _isSearching.update { true } }
        .combine(_repos) { text, repos ->
            if (text.isBlank()) {
                repos
            } else {
                delay(1000L)
                repos.filter { it.doesMatchSearchText(text) }
            }
        }
        .onEach { _isSearching.update { false } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), _repos.value)
}