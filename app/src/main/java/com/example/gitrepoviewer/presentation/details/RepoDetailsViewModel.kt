package com.example.gitrepoviewer.presentation.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gitrepoviewer.data.local.entities.RepoDetailsEntity
import com.example.gitrepoviewer.data.repository.DetailsRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RepoDetailsViewModel @Inject constructor(
    private val detailsRepositoryImpl: DetailsRepositoryImpl
) : ViewModel() {

    private val _repoId = MutableStateFlow<Long?>(null)
    val repoId = _repoId.asStateFlow()

    private val _ownerName = MutableStateFlow<String?>(null)
    val ownerName = _ownerName.asStateFlow()

    private val _repoName = MutableStateFlow<String?>(null)
    val repoName = _repoName.asStateFlow()

    val errorMessage = detailsRepositoryImpl.errorMessageResourceId.asStateFlow()

    private val _details = MutableStateFlow<RepoDetailsEntity?>(null)
    val details = _details.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            combine(_repoName, _repoId, _ownerName) { repoName, repoId, ownerName ->
                Triple(repoName, repoId, ownerName)
            }.collect { (repoName, repoId, ownerName) ->
                if (repoName != null && repoId != null && ownerName != null) {
                    detailsRepositoryImpl.fetchRepoDetails(ownerName, repoName, repoId)
                    detailsRepositoryImpl.getRepoDetails(repoId).collect { repoDetailsEntity ->
                        _details.value = repoDetailsEntity
                    }
                }
            }
        }
    }

    fun setRepoId(id: Long) {
        _repoId.value = id
    }

    fun setRepoName(repoName: String) {
        _repoName.value = repoName
    }

    fun setOwnerName(ownerName: String) {
        _ownerName.value = ownerName
    }

    fun clearErrorMessage() {
        detailsRepositoryImpl.errorMessageResourceId.value = null
    }

}