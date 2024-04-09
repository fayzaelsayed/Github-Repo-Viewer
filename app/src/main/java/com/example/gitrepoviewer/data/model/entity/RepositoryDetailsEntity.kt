package com.example.gitrepoviewer.data.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.gitrepoviewer.util.Constants.REPO_DETAILS_TABLE
import com.example.gitrepoviewer.data.model.dto.OwnerDTO
import com.example.gitrepoviewer.data.model.dto.RepositoryIssuesDTO

@Entity(tableName = REPO_DETAILS_TABLE)
data class RepositoryDetailsEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Long? = null,

    val name: String? = null,

    val fullName: String? = null,

    val owner: OwnerDTO? = null,

    val description: String? = null,

    val forksCount: Long? = null,

    val stargazersCount: Long? = null,

    val openIssuesCount: Long? = null,

    val createdAt: String? = null,

    val subscribersCount: Long? = null,

    @ColumnInfo(name = "repo_issues")
    val repoIssues : List<RepositoryIssuesDTO>
)
