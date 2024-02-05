package com.example.gitrepoviewer.data.repository


import com.example.gitrepoviewer.R
import com.example.gitrepoviewer.data.local.dao.RepoDAO
import com.example.gitrepoviewer.data.local.entities.RepoEntity
import com.example.gitrepoviewer.data.remote.GitHubApiService
import com.example.gitrepoviewer.domain.model.RepoResponseModel
import com.example.gitrepoviewer.util.UtilFunctions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.JsonNull.content
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import retrofit2.Response

@RunWith(MockitoJUnitRunner::class)
class GitRepositoryImplTest {

    @Mock
    private lateinit var gitHubApi: GitHubApiService
    @Mock
    private lateinit var repoDAO: RepoDAO
    @Mock
    private lateinit var utilFunctions: UtilFunctions

    private lateinit var gitRepositoryImpl: GitRepositoryImpl

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        gitRepositoryImpl = GitRepositoryImpl(gitHubApi, repoDAO, utilFunctions)
    }

    private suspend fun prepare(){
        `when`(utilFunctions.isInternetWorking()).thenReturn(true)
        `when`(gitHubApi.getRepositories()).thenReturn(Response.success(listOf(RepoResponseModel(1))))
    }


    @Test
    fun `fetchAllRepos() try fetch when Internet Connection is down, return error message no internet connection`() = runBlocking {
        prepare()
        `when`(utilFunctions.isInternetWorking()).thenReturn(false)
        gitRepositoryImpl.fetchAllRepos()
        assert(gitRepositoryImpl.errorMessageResourceId.value == R.string.no_internet_connection)
    }

    @Test
    fun `fetchAllRepos() try fetch repositories but response not successful fails, return error occurred isSuccessful is false`() = runBlocking {
        prepare()
        `when`(gitHubApi.getRepositories()).thenReturn(Response.error(400, content.toResponseBody(null)))
        gitRepositoryImpl.fetchAllRepos()
        assert(gitRepositoryImpl.errorMessageResourceId.value == R.string.request_not_successful)
    }

    @Test
    fun `fetchAllRepos() check response list is empty when no repositories found, result no repositories found`() = runBlocking {
        prepare()
        `when`(gitHubApi.getRepositories()).thenReturn(Response.success(emptyList()))
        gitRepositoryImpl.fetchAllRepos()
        assert(gitRepositoryImpl.errorMessageResourceId.value == R.string.no_repositories)
    }

    @Test
    fun `fetchAllRepos() inserting one repository in database successfully, result one repository is inserted`() = runBlocking {
            prepare()
            val repoEntity = RepoEntity(1)
            val list = mutableListOf<RepoEntity>()

            `when`(repoDAO.addRepo(any())).then {
                list.add(repoEntity)
            }
            gitRepositoryImpl.fetchAllRepos()
            assert(list.size == 1)
        }

    @Test
    fun `fetchAllRepos() get all repositories from database with flow when list not empty, result list of repo entities`() = runBlocking {
        prepare()
        val repoEntity = RepoEntity(1)
        `when`(repoDAO.getAllRepos()).thenReturn(MutableStateFlow(listOf(repoEntity)))
        gitRepositoryImpl.fetchAllRepos()
        assert(gitRepositoryImpl.getAllRepos().first().isNotEmpty())
    }

}