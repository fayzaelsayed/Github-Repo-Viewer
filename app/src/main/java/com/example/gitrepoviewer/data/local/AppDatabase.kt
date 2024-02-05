package com.example.gitrepoviewer.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.gitrepoviewer.data.local.dao.DetailsDAO
import com.example.gitrepoviewer.data.local.dao.RepoDAO
import com.example.gitrepoviewer.data.local.entities.RepoDetailsEntity
import com.example.gitrepoviewer.data.local.entities.RepoEntity

@Database(entities = [RepoEntity::class, RepoDetailsEntity::class], version = 1, exportSchema = false)
@TypeConverters(DataTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun repoDao() : RepoDAO
    abstract fun detailsDao() : DetailsDAO

}