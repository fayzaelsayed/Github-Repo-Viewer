package com.example.gitrepoviewer.presentation.repos


import com.example.gitrepoviewer.data.local.entities.RepoEntity
import com.example.gitrepoviewer.data.repository.GitRepositoryImpl
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ReposViewModelTest {

    @Mock
    private lateinit var gitRepositoryImpl: GitRepositoryImpl
    private lateinit var viewModel: ReposViewModel


    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        `when`(gitRepositoryImpl.isFetchingDone).thenReturn(MutableStateFlow(true))
        `when`(gitRepositoryImpl.errorMessageResourceId).thenReturn(MutableStateFlow(-1))
        val listOfRepositories = listOf(RepoEntity(1, name = "fa1"), RepoEntity(1, name = "fa2"), RepoEntity(1, name = "mm"))
        `when`(gitRepositoryImpl.getAllRepos()).thenReturn(MutableStateFlow(listOfRepositories))
        viewModel = ReposViewModel(gitRepositoryImpl)
    }


    @Test
    fun `check if repos is not empty, result is true`() = runBlocking {
        assert(viewModel.reposState.value.isNotEmpty())
    }

    @Test
    fun `get repositories contain name fa, result is two repositories`() = runBlocking {
        delay(1000)
        viewModel.onSearchTextChange("fa")
        assert(viewModel.getReposForTest().value.isNotEmpty())
    }

    @Test
    fun `check if fetching is done when it is done, result is true`() = runBlocking {
        assert(viewModel.isFetchingDone.first())
    }

    @Test
    fun `check error message when there's no error, result is -1`() = runBlocking {
        assert(viewModel.errorMessageResourceId.first() == -1 )
    }
}