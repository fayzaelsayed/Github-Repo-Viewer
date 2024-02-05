package com.example.gitrepoviewer.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.gitrepoviewer.domain.model.Owner
import com.example.gitrepoviewer.util.Constants.ALL_REPOS_TABLE
import com.google.gson.annotations.SerializedName

@Entity(tableName = ALL_REPOS_TABLE)
data class RepoEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Long? = null,

    val name: String? = null,

    @SerializedName("full_name")
    val fullName: String? = null,

    val owner: Owner? = null,

    val description: String? = null,
){
    fun doesMatchSearchText(query: String): Boolean{
        val matchingCombination = listOf(
            "$name",
            "${owner?.login}"
        )
        return matchingCombination.any {
            it.contains(query, ignoreCase = true)
        }
    }
}