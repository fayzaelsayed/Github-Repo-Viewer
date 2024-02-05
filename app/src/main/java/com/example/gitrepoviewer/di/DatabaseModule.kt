package com.example.gitrepoviewer.di

import android.content.Context
import androidx.room.Room
import com.example.gitrepoviewer.data.local.AppDatabase
import com.example.gitrepoviewer.data.local.dao.DetailsDAO
import com.example.gitrepoviewer.data.local.dao.RepoDAO
import com.example.gitrepoviewer.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideRoomDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, Constants.DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideRepoDAO(appDatabase: AppDatabase): RepoDAO {
        return appDatabase.repoDao()
    }

    @Singleton
    @Provides
    fun provideDetailsDAO(appDatabase: AppDatabase): DetailsDAO {
        return appDatabase.detailsDao()
    }

}