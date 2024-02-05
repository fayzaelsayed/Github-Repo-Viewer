package com.example.gitrepoviewer.domain.model

import com.example.gitrepoviewer.data.local.entities.RepoDetailsEntity
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class RepoDetailsModel (
    val id: Long? = null,

    @SerializedName("node_id")
    val nodeID: String? = null,

    val name: String? = null,

    @SerializedName("full_name")
    val fullName: String? = null,

    val owner: Owner? = null,

    val description: String? = null,

    val homepage: String? = null,

    @SerializedName("forks_count")
    val forksCount: Long? = null,

    val forks: Long? = null,

    @SerializedName("stargazers_count")
    val stargazersCount: Long? = null,

    @SerializedName("watchers_count")
    val watchersCount: Long? = null,

    val watchers: Long? = null,
    val size: Long? = null,

    @SerializedName("default_branch")
    val defaultBranch: String? = null,

    @SerializedName("open_issues_count")
    val openIssuesCount: Long? = null,

    @SerializedName("open_issues")
    val openIssues: Long? = null,

    @SerializedName("created_at")
    val createdAt: String? = null,

    @SerializedName("subscribers_count")
    val subscribersCount: Long? = null
){
    fun fromModelToEntity() : RepoDetailsEntity{
        return RepoDetailsEntity(
            id = id,
            nodeID = nodeID,
            name = name,
            fullName = fullName,
            owner = owner,
            description = description,
            homepage = homepage,
            forksCount = forksCount,
            forks = forks,
            stargazersCount = stargazersCount,
            watchersCount = watchersCount,
            watchers = watchers,
            size = size,
            defaultBranch = defaultBranch,
            openIssuesCount = openIssuesCount,
            createdAt = createdAt,
            subscribersCount = subscribersCount,
            repo_issues = emptyList()
        )
    }
}

