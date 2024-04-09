package com.example.gitrepoviewer.data.repository.repo_details

import com.example.gitrepoviewer.data.model.dto.RepositoryDetailsDTO
import com.example.gitrepoviewer.data.remote.GitHubApiService
import com.example.gitrepoviewer.domain.repository.repo_details.IRepoDetailsRemoteDS
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
class RepoDetailsRemoteDSImplTest {

    @Mock
    private lateinit var gitHubApi: GitHubApiService

    private lateinit var detailsRemoteDS: IRepoDetailsRemoteDS
    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        detailsRemoteDS = RepoDetailsRemoteDSImpl(gitHubApi)
    }

    @Test
    fun `fetchRepoDetails() try fetch when response is successful, return repo details with correct value`() = runTest {
        Mockito.`when`(gitHubApi.getRepoDetails("", "")).thenReturn(Response.success(RepositoryDetailsDTO(1, name = "fayza")))
        detailsRemoteDS.fetchRepoDetails("", "")
        assert(detailsRemoteDS.fetchRepoDetails("", "").data != null)
        assert(detailsRemoteDS.fetchRepoDetails("", "").data?.name == "fayza")
    }

    @Test
    fun `fetchRepoDetails() try fetch when there's error code, return error contain error code`() = runTest {
        Mockito.`when`(gitHubApi.getRepoDetails("", "")).thenReturn(Response.error(400, "".toResponseBody("application/json".toMediaTypeOrNull())))
        val result = detailsRemoteDS.fetchRepoDetails("", "")
        assert(result.error?.errorCode == 400)
    }
}