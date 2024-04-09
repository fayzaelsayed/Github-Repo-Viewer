package com.example.gitrepoviewer.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.gitrepoviewer.data.model.dto.RepositoryIssuesDTO
import com.example.gitrepoviewer.data.model.entity.RepositoryDetailsEntity

@Dao
interface RepositoryDetailsDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addRepoDetails(repositoryDetailsEntity: RepositoryDetailsEntity)

    @Query("SELECT * FROM repo_details WHERE id LIKE :id")
    suspend fun getRepoDetails(id: Long) : RepositoryDetailsEntity?

    @Query("UPDATE repo_details SET repo_issues = :issues WHERE id LIKE :id")
    suspend fun updateRepoIssues(id: Long, issues: List<RepositoryIssuesDTO>)
}