package com.example.gitrepoviewer.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.gitrepoviewer.data.model.entity.RepositoryEntity

@Dao
interface RepositoryDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addRepo(repositoryEntity: RepositoryEntity)

    @Query("SELECT * FROM all_repos ORDER BY id LIMIT :pageSize OFFSET :offset")
    suspend fun getAllRepos(offset: Int, pageSize: Int): List<RepositoryEntity>

}