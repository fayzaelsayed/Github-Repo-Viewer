package com.example.gitrepoviewer.presentation.issues

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.example.gitrepoviewer.common.RepoViewerException
import com.example.gitrepoviewer.common.Resource
import com.example.gitrepoviewer.domain.model.RepositoryIssues
import com.example.gitrepoviewer.domain.use_case.repo_issues.GetRepoIssuesUseCase
import com.example.gitrepoviewer.domain.use_case.repo_issues.RequestRepoIssuesUseCase
import com.example.gitrepoviewer.util.DispatcherProvider
import com.example.gitrepoviewer.util.TestDispatcherProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
class RepoIssuesViewModelTest {

    @Mock
    lateinit var requestIssuesUseCase: RequestRepoIssuesUseCase

    @Mock
    lateinit var getIssuesUseCase: GetRepoIssuesUseCase

    @Mock
    lateinit var savedStateHandle: SavedStateHandle

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var dispatcherProvider: DispatcherProvider

    private lateinit var viewModel: RepoIssuesViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        dispatcherProvider = TestDispatcherProvider()
        Mockito.`when`(savedStateHandle.get<String>("ownerName")).thenReturn("owner")
        Mockito.`when`(savedStateHandle.get<String>("repoName")).thenReturn("repo")
        Mockito.`when`(savedStateHandle.get<Long>("repoId")).thenReturn(123L)

        runTest {
            Mockito.`when`(requestIssuesUseCase("owner", "repo", 123L)).thenReturn(
                flow {
                    Resource.Success(
                        listOf(RepositoryIssues(1))
                    )
                }
            )
        }
        viewModel = RepoIssuesViewModel(
            requestIssuesUseCase,
            getIssuesUseCase,
            dispatcherProvider,
            savedStateHandle
        )
    }

    @Test
    fun `test viewState while waiting to fetch repositoryIssues remotely, return viewState loading value is true`() =
        runTest {
            Mockito.`when`(requestIssuesUseCase("", "", 0L))
                .thenReturn(flowOf(Resource.Loading(true)))
            viewModel.requestRepoIssues("", "", 0L)
            viewModel.viewState.test {
                assert(awaitItem().isLoading)
            }
        }

    @Test
    fun `test viewState when fetching repositoryIssues remotely fail, return viewState contain the error message`() =
        runTest {
            Mockito.`when`(requestIssuesUseCase("", "", 0L))
                .thenReturn(flowOf(Resource.Failure(RepoViewerException.CustomException("fetch fail"))))
            viewModel.requestRepoIssues("", "", 0L)
            viewModel.viewState.test {
                assert(awaitItem().error == "fetch fail")
            }
        }

    @Test
    fun `test viewState while loading repositoryIssues from database, return viewState loading value is true`() =
        runTest {
            Mockito.`when`(getIssuesUseCase(0L)).thenReturn(flowOf(Resource.Loading(true)))
            viewModel.getRepoIssues(0L)
            viewModel.viewState.test {
                assert(awaitItem().isLoading)
            }
        }

    @Test
    fun `test viewState after getting repositoryIssues from database success, return viewState contains list of issues`() =
        runTest {
            val issuesList = listOf(RepositoryIssues(1), RepositoryIssues(2))
            Mockito.`when`(getIssuesUseCase(0L)).thenReturn(flowOf(Resource.Success(issuesList)))
            viewModel.getRepoIssues(0L)
            viewModel.viewState.test {
                assert(awaitItem().issues?.size == 2)
            }
        }

    @Test
    fun `test viewState when getting repositoryIssues from database fail, return viewSate contain the error message`() =
        runTest {
            Mockito.`when`(getIssuesUseCase(0L))
                .thenReturn(flowOf(Resource.Failure(RepoViewerException.CustomException("get failed"))))
            viewModel.getRepoIssues(0L)
            viewModel.viewState.test {
                assert(awaitItem().error == "get failed")
            }
        }

}