package com.example.gitrepoviewer.data.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.gitrepoviewer.util.Constants
import com.example.gitrepoviewer.data.model.dto.OwnerDTO

@Entity(tableName = Constants.ALL_REPOS_TABLE)
data class RepositoryEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Long? = null,

    val name: String? = null,

    val fullName: String? = null,

    val owner: OwnerDTO? = null,

    val description: String? = null,
)