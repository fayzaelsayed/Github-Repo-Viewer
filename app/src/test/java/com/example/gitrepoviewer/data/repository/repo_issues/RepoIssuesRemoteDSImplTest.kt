package com.example.gitrepoviewer.data.repository.repo_issues

import com.example.gitrepoviewer.data.model.dto.RepositoryIssuesDTO
import com.example.gitrepoviewer.data.remote.GitHubApiService
import com.example.gitrepoviewer.domain.repository.repo_issues.IRepoIssuesRemoteDS
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response

@RunWith(MockitoJUnitRunner::class)
class RepoIssuesRemoteDSImplTest {

    @Mock
    private lateinit var gitHubApi: GitHubApiService

    private lateinit var issuesRemoteDS: IRepoIssuesRemoteDS

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        issuesRemoteDS = RepoIssuesRemoteDSImpl(gitHubApi)
    }

    @Test
    fun `fetchRepoIssues() try fetch when response is successful, return repo issues list not empty`() = runTest {
        Mockito.`when`(gitHubApi.getRepoIssues("", "")).thenReturn(Response.success(listOf(RepositoryIssuesDTO(1, title = "fayza"))))
        issuesRemoteDS.fetchRepoIssues("", "")
        assert(!issuesRemoteDS.fetchRepoIssues("", "").data.isNullOrEmpty())
        assert(issuesRemoteDS.fetchRepoIssues("", "").data?.get(0)?.title == "fayza")
    }

    @Test
    fun `fetchRepoIssues() try fetch when there's error code, return error contain error code`() = runTest {
        Mockito.`when`(gitHubApi.getRepoIssues("", "")).thenReturn(Response.error(400, "".toResponseBody("application/json".toMediaTypeOrNull())))
        val result = issuesRemoteDS.fetchRepoIssues("", "")
        assert(result.error?.errorCode == 400)
    }
}