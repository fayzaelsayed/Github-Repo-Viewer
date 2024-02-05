package com.example.gitrepoviewer.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.gitrepoviewer.domain.model.Owner
import com.example.gitrepoviewer.domain.model.RepoIssuesModel
import com.example.gitrepoviewer.util.Constants.REPO_DETAILS_TABLE
import com.google.gson.annotations.SerializedName

@Entity(tableName = REPO_DETAILS_TABLE)
data class RepoDetailsEntity(
    @PrimaryKey(autoGenerate = false)
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
    val subscribersCount: Long? = null,

    val repo_issues : List<RepoIssuesModel>
)
