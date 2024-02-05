package com.example.gitrepoviewer.di

import com.example.gitrepoviewer.data.local.dao.DetailsDAO
import com.example.gitrepoviewer.data.local.dao.RepoDAO
import com.example.gitrepoviewer.data.remote.GitHubApi
import com.example.gitrepoviewer.data.repository.DetailsRepositoryImpl
import com.example.gitrepoviewer.data.repository.GitRepositoryImpl
import com.example.gitrepoviewer.util.UtilFunctions
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideGitRepository(gitHubApi: GitHubApi,repoDao: RepoDAO, utilFunctions: UtilFunctions): GitRepositoryImpl {
        return GitRepositoryImpl(gitHubApi, repoDao, utilFunctions)
    }

    @Singleton
    @Provides
    fun provideDetailsRepository(gitHubApi: GitHubApi, detailsDAO: DetailsDAO, utilFunctions: UtilFunctions): DetailsRepositoryImpl {
        return DetailsRepositoryImpl(gitHubApi, detailsDAO, utilFunctions)
    }

}