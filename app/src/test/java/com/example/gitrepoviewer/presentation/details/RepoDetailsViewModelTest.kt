package com.example.gitrepoviewer.presentation.details

import com.example.gitrepoviewer.data.local.entities.RepoDetailsEntity
import com.example.gitrepoviewer.data.repository.DetailsRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class RepoDetailsViewModelTest {

    @Mock
    private lateinit var detailsRepositoryImpl: DetailsRepositoryImpl

    private lateinit var viewModel: RepoDetailsViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        `when`(detailsRepositoryImpl.isDetailsFetchingDone).thenReturn(MutableStateFlow(true))
        `when`(detailsRepositoryImpl.errorMessageResourceId).thenReturn(MutableStateFlow(-1))
        `when`(detailsRepositoryImpl.getRepoDetails(1)).thenReturn(MutableStateFlow(RepoDetailsEntity(repo_issues = emptyList())))
        viewModel = RepoDetailsViewModel(detailsRepositoryImpl)
    }

    @Test
    fun `get repoId value after setting it, result should be 13`() = runBlocking {
        viewModel.setRepoId(13)
        assert(viewModel.repoId.value == 13L)
    }

    @Test
    fun `get repoName value after setting it, result should be rpName`() = runBlocking {
        viewModel.setRepoName("rpName")
        assert(viewModel.repoName.value == "rpName")
    }

    @Test
    fun `get ownerName value after setting it, result should be owName`() = runBlocking {
        viewModel.setOwnerName("owName")
        assert(viewModel.ownerName.value == "owName")
    }

    @Test
    fun `get error message when there's no error, result is -1`() = runBlocking {
        detailsRepositoryImpl.fetchRepoDetails("owName", "rpName", 1)
        assert(viewModel.errorMessage.first() == -1)
    }

}