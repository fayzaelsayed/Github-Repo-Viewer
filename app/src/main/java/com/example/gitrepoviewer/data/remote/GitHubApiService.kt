package com.example.gitrepoviewer.data.remote


import com.example.gitrepoviewer.data.model.dto.RepositoryDTO
import com.example.gitrepoviewer.data.model.dto.RepositoryDetailsDTO
import com.example.gitrepoviewer.data.model.dto.RepositoryIssuesDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface GitHubApiService {

    @GET("repositories")
    suspend fun getRepositories(): Response<List<RepositoryDTO>>

    @GET("repos/{owner}/{repo}")
    suspend fun getRepoDetails(
        @Path("owner") owner: String,
        @Path("repo") repoName: String
    ): Response<RepositoryDetailsDTO>

    @GET("repos/{owner}/{repo}/issues")
    suspend fun getRepoIssues(
        @Path("owner") owner: String,
        @Path("repo") repoName: String
    ): Response<List<RepositoryIssuesDTO>>
}