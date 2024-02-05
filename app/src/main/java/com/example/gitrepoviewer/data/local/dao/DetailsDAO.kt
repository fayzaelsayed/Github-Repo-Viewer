package com.example.gitrepoviewer.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.gitrepoviewer.data.local.entities.RepoDetailsEntity
import com.example.gitrepoviewer.domain.model.RepoIssuesModel
import kotlinx.coroutines.flow.Flow

@Dao
interface DetailsDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addRepoDetails(repoDetailsEntity: RepoDetailsEntity)

    @Query("SELECT * FROM repo_details WHERE id LIKE :id")
    fun getRepoDetails(id: Long) : Flow<RepoDetailsEntity>

    @Query("UPDATE repo_details SET repo_issues = :issues WHERE id LIKE :id")
    suspend fun updateRepoIssues(id: Long, issues: List<RepoIssuesModel>)
}