package com.example.gitrepoviewer.presentation.repos

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.example.gitrepoviewer.common.RepoViewerException
import com.example.gitrepoviewer.common.Resource
import com.example.gitrepoviewer.domain.model.Repository
import com.example.gitrepoviewer.domain.use_case.repo.GetReposUseCase
import com.example.gitrepoviewer.domain.use_case.repo.RequestReposUseCase
import com.example.gitrepoviewer.domain.use_case.repo.SearchReposUseCase
import com.example.gitrepoviewer.util.DispatcherProvider
import com.example.gitrepoviewer.util.TestDispatcherProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class ReposViewModelTest {

    @Mock
    private lateinit var requestReposUseCase: RequestReposUseCase
    @Mock
    private lateinit var getReposUseCase: GetReposUseCase
    @Mock
    private lateinit var searchReposUseCase: SearchReposUseCase

    private lateinit var dispatcherProvider: DispatcherProvider

    private lateinit var viewModel: ReposViewModel

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        dispatcherProvider = TestDispatcherProvider()
        runTest {
            Mockito.`when`(requestReposUseCase()).thenReturn(flow{ Resource.Success(listOf(Repository(1, "fayza1"), Repository(2, "fayza2")))})
        }
        viewModel = ReposViewModel(requestReposUseCase, getReposUseCase, searchReposUseCase, dispatcherProvider)
    }


    @Test
    fun `test viewState initial state`() = runTest {
        val initialState = viewModel.viewState.first()
        assert(initialState.isLoading)
        assert(!initialState.isSearching)
        assert(initialState.reposList.isNullOrEmpty())
        assert(initialState.error == null)
        assert(!initialState.isSearching)
        assert(initialState.filteredReposList == null)
        assert(!initialState.isPaginating)
    }

    @Test
    fun `test viewState while waiting to fetch repos remotely, return viewState loading value true`() = runTest {
        Mockito.`when`(requestReposUseCase()).thenReturn(flowOf(Resource.Loading(true)))
        viewModel.fetchRepos()
        viewModel.viewState.test {
            assert(awaitItem().isLoading)
        }
    }

    @Test
    fun `test viewState when fetching repos remotely fail, return viewState contain the error message`() = runTest {
        Mockito.`when`(requestReposUseCase()).thenReturn(flowOf(Resource.Failure(RepoViewerException.CustomException("error"))))
        viewModel.fetchRepos()
        viewModel.viewState.test {
            assert(awaitItem().error == "error")
        }
    }

    @Test
    fun `test viewState after getting repos list from database, return viewState contain list of repos`() = runTest {
        val listOfRepos = listOf(Repository(1, "fayza1"), Repository(2, "fayza2"))
        Mockito.`when`(getReposUseCase(0)).thenReturn(flowOf(Resource.Success(listOfRepos)))
        viewModel.getRepos(0)
        viewModel.viewState.test{
            assert(awaitItem().reposList?.size == 2)
        }
    }

    @Test
    fun `test viewState when getting repos from database fail, return viewSate contain the error message`() = runTest {
        Mockito.`when`(getReposUseCase(0)).thenReturn(flowOf(Resource.Failure(RepoViewerException.CustomException("error"))))
        viewModel.getRepos(0)
        viewModel.viewState.test {
            assert(awaitItem().error == "error")
        }
    }

    @Test
    fun `test viewState while loading repos from database, return viewState loading value true`() = runTest{
        Mockito.`when`(getReposUseCase(0)).thenReturn(flowOf(Resource.Loading(true)))
        viewModel.getRepos(0)
        viewModel.viewState.test {
            assert(awaitItem().isLoading)
        }
    }

    @Test
    fun `test searching in repos list , result viewState contains filtered repos list with the correct results`() = runTest {
        val reposList = listOf(Repository(1, "fayza1"), Repository(2, "fayza2"), Repository(3, "fayza3"))
        val filteredList = listOf(Repository(1, "fayza1"))
        Mockito.`when`(getReposUseCase(0)).thenReturn(flowOf(Resource.Success(reposList)))
        viewModel.getRepos(0)
        Mockito.`when`(searchReposUseCase(reposList, "fayza1")).thenReturn(filteredList)
        viewModel.searchRepos("fayza1")
        viewModel.viewState.test{
            assert(awaitItem().filteredReposList?.size == 1)
        }
    }
}