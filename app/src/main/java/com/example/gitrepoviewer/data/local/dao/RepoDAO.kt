package com.example.gitrepoviewer.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.gitrepoviewer.data.local.entities.RepoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RepoDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addRepo(repo: RepoEntity)

    @Query("SELECT * FROM all_repos")
    fun getAllRepos(): Flow<List<RepoEntity>>

}