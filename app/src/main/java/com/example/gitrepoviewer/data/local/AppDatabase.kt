package com.example.gitrepoviewer.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.gitrepoviewer.data.local.dao.RepositoryDAO
import com.example.gitrepoviewer.data.local.dao.RepositoryDetailsDAO
import com.example.gitrepoviewer.data.model.entity.RepositoryDetailsEntity
import com.example.gitrepoviewer.data.model.entity.RepositoryEntity

@Database(entities = [RepositoryEntity::class, RepositoryDetailsEntity::class], version = 1, exportSchema = false)
@TypeConverters(DataTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun repoDao() : RepositoryDAO
    abstract fun detailsDao() : RepositoryDetailsDAO

}