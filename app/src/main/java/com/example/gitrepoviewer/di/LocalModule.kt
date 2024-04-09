package com.example.gitrepoviewer.di

import android.content.Context
import androidx.room.Room
import com.example.gitrepoviewer.util.Constants
import com.example.gitrepoviewer.data.local.AppDatabase
import com.example.gitrepoviewer.data.local.dao.RepositoryDAO
import com.example.gitrepoviewer.data.local.dao.RepositoryDetailsDAO
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {

    @Singleton
    @Provides
    fun provideRoomDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, Constants.DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideRepositoryDAO(appDatabase: AppDatabase): RepositoryDAO {
        return appDatabase.repoDao()
    }

    @Singleton
    @Provides
    fun provideRepositoryDetailsDAO(appDatabase: AppDatabase): RepositoryDetailsDAO {
        return appDatabase.detailsDao()
    }

}