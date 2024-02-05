package com.example.gitrepoviewer.data.repository

import com.example.gitrepoviewer.R
import com.example.gitrepoviewer.data.local.dao.DetailsDAO
import com.example.gitrepoviewer.data.local.entities.RepoDetailsEntity
import com.example.gitrepoviewer.data.remote.GitHubApiService
import com.example.gitrepoviewer.domain.model.RepoDetailsModel
import com.example.gitrepoviewer.domain.model.RepoIssuesModel
import com.example.gitrepoviewer.util.UtilFunctions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.JsonNull
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
class DetailsRepositoryImplTest {

    @Mock
    private lateinit var gitHubApi: GitHubApiService
    @Mock
    private lateinit var detailsDAO: DetailsDAO
    @Mock
    private lateinit var utilFunctions: UtilFunctions

    private lateinit var detailsRepositoryImpl: DetailsRepositoryImpl

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        detailsRepositoryImpl = DetailsRepositoryImpl(gitHubApi, detailsDAO, utilFunctions)
    }

    private suspend fun prepare(){
        `when`(utilFunctions.isInternetWorking()).thenReturn(true)
        `when`(gitHubApi.getRepoDetails("", "")).thenReturn(Response.success(RepoDetailsModel()))
        `when`(gitHubApi.getRepoIssues("", "")).thenReturn(Response.success(listOf(RepoIssuesModel())))
    }

    // test repo details functions

    @Test
    fun `fetchRepoDetails() try fetch repo details when Internet is down, result Internet Down string id`() = runBlocking {
        prepare()
        `when`(utilFunctions.isInternetWorking()).thenReturn(false)
        detailsRepositoryImpl.fetchRepoDetails("", "", 1)
        assert(detailsRepositoryImpl.errorMessageResourceId.value == R.string.no_internet_connection)
    }

    @Test
    fun `fetchRepoDetails() get repository details fail, result isSuccessful is false and string id is error occurred`() = runBlocking {
        prepare()
        `when`(gitHubApi.getRepoDetails("", "")).thenReturn(Response.error(400, JsonNull.content.toResponseBody(null)))
        detailsRepositoryImpl.fetchRepoDetails("", "", 1)
        assert(detailsRepositoryImpl.errorMessageResourceId.value == R.string.request_not_successful)
    }

    @Test
    fun `fetchRepoDetails() when repository details is null, result is no details found string id`() = runBlocking {
        prepare()
        `when`(gitHubApi.getRepoDetails("","")).thenReturn(Response.success(null))
        detailsRepositoryImpl.fetchRepoDetails("", "", 1)
        assert(detailsRepositoryImpl.errorMessageResourceId.value == R.string.no_details)
    }

    @Test
    fun `fetchRepoDetails() when insert repository details, result one repository details inserted`() = runBlocking {
        prepare()
        val repoDetails = RepoDetailsModel()
        val list = mutableListOf<RepoDetailsModel>()

        `when`(detailsDAO.addRepoDetails(any())).then {
            list.add(repoDetails)
        }
        detailsRepositoryImpl.fetchRepoDetails("", "", 1)
        assert(list.size == 1)
    }

    @Test
    fun `fetchRepoDetails() get repository details from database when there's a repo details inserted, result we get repo details entity`() = runBlocking {
        prepare()
        val repoDetails = RepoDetailsEntity(1, repo_issues = emptyList())
        `when`(detailsDAO.getRepoDetails(1)).thenReturn(MutableStateFlow(repoDetails))
        detailsRepositoryImpl.fetchRepoDetails("", "", 1)
        assert(detailsDAO.getRepoDetails(1).first().id == 1L)
    }

    // test repo issues functions

    @Test
    fun `fetchRepoIssues() try fetch repo issues when internet is down, result Internet Down string id`() = runBlocking {
        prepare()
        `when`(utilFunctions.isInternetWorking()).thenReturn(false)
        detailsRepositoryImpl.fetchRepoIssues("", "", 1)
        assert(detailsRepositoryImpl.errorMessageResourceId.value == R.string.no_internet_connection)
    }

    @Test
    fun `fetchRepoIssues() getting repository issues fail, result isSuccessful is false and string id is error occurred`() = runBlocking {
        prepare()
        `when`(gitHubApi.getRepoIssues("", "")).thenReturn(Response.error(400, JsonNull.content.toResponseBody(null)))
        detailsRepositoryImpl.fetchRepoIssues("", "", 1)
        assert(detailsRepositoryImpl.errorMessageResourceId.value == R.string.could_not_get_repo_issues)
    }

    @Test
    fun `fetchRepoIssues() when issues list is null or empty, result is no issues found string id`() = runBlocking {
        prepare()
        `when`(gitHubApi.getRepoIssues("","")).thenReturn(Response.success(emptyList()))
        detailsRepositoryImpl.fetchRepoIssues("", "", 1)
        assert(detailsRepositoryImpl.errorMessageResourceId.value == R.string.no_issues)
    }

    @Test
    fun `fetchRepoIssues() when getting repo issues list and update database, result repository details include repo issues`() = runBlocking {
        prepare()
        val repoIssue = RepoIssuesModel()
        val list = mutableListOf<RepoIssuesModel>()

        `when`(detailsDAO.updateRepoIssues(1, listOf(RepoIssuesModel()))).then {
            list.add(repoIssue)
        }
        detailsRepositoryImpl.fetchRepoIssues("","",1)
        assert(list.size == 1)
    }

}