package com.example.gitrepoviewer.presentation.details

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.example.gitrepoviewer.common.RepoViewerException
import com.example.gitrepoviewer.common.Resource
import com.example.gitrepoviewer.data.model.dto.RepositoryIssuesDTO
import com.example.gitrepoviewer.domain.model.RepositoryDetails
import com.example.gitrepoviewer.domain.use_case.repo_details.GetRepoDetailsUseCase
import com.example.gitrepoviewer.domain.use_case.repo_details.RequestRepoDetailsUseCase
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
class RepoDetailsViewModelTest {
    @Mock
    private lateinit var requestRepositoryDetailsUseCase: RequestRepoDetailsUseCase

    @Mock
    private lateinit var getRepositoryDetailsUseCase: GetRepoDetailsUseCase

    @Mock
    private lateinit var savedStateHandle: SavedStateHandle


    private lateinit var dispatcherProvider: DispatcherProvider
    private lateinit var viewModel: RepoDetailsViewModel

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()


    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        dispatcherProvider = TestDispatcherProvider()
        Mockito.`when`(savedStateHandle.get<String>("ownerName")).thenReturn("owner")
        Mockito.`when`(savedStateHandle.get<String>("repoName")).thenReturn("repo")
        Mockito.`when`(savedStateHandle.get<Long>("repoId")).thenReturn(123L)

        runTest {
            val repoDetails = RepositoryDetails(1, "fayza1", repo_issues = emptyList())
            Mockito.`when`(requestRepositoryDetailsUseCase("owner", "repo")).thenReturn(flow {
                Resource.Success(repoDetails)
            })
        }
        viewModel = RepoDetailsViewModel(
            requestRepositoryDetailsUseCase,
            getRepositoryDetailsUseCase,
            dispatcherProvider,
            savedStateHandle
        )
    }

    @Test
    fun `test viewState initial state`() = runTest {
        val initialState = viewModel.viewState.first()
        assert(initialState.isLoading)
        assert(initialState.repoDetails == null)
        assert(initialState.error == null)
    }

    @Test
    fun `test viewState while waiting to fetch repositoryDetails remotely, return viewState loading value true`() = runTest {
        Mockito.`when`(requestRepositoryDetailsUseCase("","")).thenReturn(flowOf(Resource.Loading(true)))
        viewModel.fetchRepoDetails("","",0L)
        viewModel.viewState.test {
            assert(awaitItem().isLoading)
        }
    }

    @Test
    fun `test viewState when fetching repositoryDetails remotely fail, return viewState contain the error message`()= runTest {
        Mockito.`when`(requestRepositoryDetailsUseCase("","")).thenReturn(flowOf(Resource.Failure(RepoViewerException.CustomException("error"))))
        viewModel.fetchRepoDetails("","",0L)
        viewModel.viewState.test {
            assert(awaitItem().error == "error")
        }
    }

    @Test
    fun `test viewState after getting repositoryDetails from database, return viewState contain repositoryDetails`() = runTest {
        val reposDetail = RepositoryDetails(1, "fayza1", repo_issues = listOf( RepositoryIssuesDTO(1, "", "")))
        Mockito.`when`(getRepositoryDetailsUseCase(0)).thenReturn(flowOf(Resource.Success(reposDetail)))
        viewModel.getRepoDetails(0)
        viewModel.viewState.test{
            assert(awaitItem().repoDetails?.name == "fayza1")
        }
    }

    @Test
    fun `test viewState when getting repositoryDetails from database fail, return viewSate contain the error message`() = runTest {
        Mockito.`when`(getRepositoryDetailsUseCase(0)).thenReturn(flowOf(Resource.Failure(RepoViewerException.CustomException("error"))))
        viewModel.getRepoDetails(0)
        viewModel.viewState.test {
            assert(awaitItem().error == "error")
        }
    }

    @Test
    fun `test viewState while loading repositoryDetails from database, return viewState loading value true`() = runTest{
        Mockito.`when`(getRepositoryDetailsUseCase(0)).thenReturn(flowOf(Resource.Loading(true)))
        viewModel.getRepoDetails(0)
        viewModel.viewState.test {
            assert(awaitItem().isLoading)
        }
    }


}