package com.example.gitrepoviewer.data.repository.repo

import com.example.gitrepoviewer.data.model.dto.RepositoryDTO
import com.example.gitrepoviewer.data.remote.GitHubApiService
import com.example.gitrepoviewer.domain.repository.repo.IReposRemoteDS
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
class ReposRemoteDSImplTest {

    @Mock
    private lateinit var gitHubApi: GitHubApiService

    private lateinit var reposRemoteDS: IReposRemoteDS
    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        reposRemoteDS = ReposRemoteDSImpl(gitHubApi)
    }

    @Test
    fun `fetchAllRepos() try fetch when response is successful, return list not empty`() = runTest {
        Mockito.`when`(gitHubApi.getRepositories()).thenReturn(Response.success(listOf(RepositoryDTO(1))))
        reposRemoteDS.fetchAllRepos()
        assert(!reposRemoteDS.fetchAllRepos().data.isNullOrEmpty())
    }

    @Test
    fun `fetchAllRepos() try fetch when there's error code, return error contain error code`() = runTest {
        Mockito.`when`(gitHubApi.getRepositories()).thenReturn(Response.error(500, "".toResponseBody("application/json".toMediaTypeOrNull())))
        val result = reposRemoteDS.fetchAllRepos()
        assert(result.error?.errorCode == 500)
    }


}