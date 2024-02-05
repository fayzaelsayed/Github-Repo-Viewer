package com.example.gitrepoviewer.data.remote


import com.example.gitrepoviewer.domain.model.RepoDetailsModel
import com.example.gitrepoviewer.domain.model.RepoIssuesModel
import com.example.gitrepoviewer.domain.model.RepoResponseModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface GitHubApi {

    @GET("/repositories")
    suspend fun getRepositories() : Response<List<RepoResponseModel>>

    @GET("/repos/{owner}/{repo}")
    suspend fun getRepoDetails(
        @Path("owner") owner: String,
        @Path("repo") repoName: String) : Response<RepoDetailsModel>

    @GET("/repos/{owner}/{repo}/issues")
    suspend fun getRepoIssues(
        @Path("owner") owner: String,
        @Path("repo") repoName: String
    ) : Response<List<RepoIssuesModel>>
}