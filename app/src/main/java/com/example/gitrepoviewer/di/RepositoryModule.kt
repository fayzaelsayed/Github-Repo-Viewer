package com.example.gitrepoviewer.di

import com.example.gitrepoviewer.data.local.dao.RepositoryDAO
import com.example.gitrepoviewer.data.local.dao.RepositoryDetailsDAO
import com.example.gitrepoviewer.data.remote.GitHubApiService
import com.example.gitrepoviewer.data.repository.repo.ReposLocalDSImpl
import com.example.gitrepoviewer.data.repository.repo.ReposRemoteDSImpl
import com.example.gitrepoviewer.data.repository.repo.ReposRepositoryImpl
import com.example.gitrepoviewer.data.repository.repo_details.RepoDetailsLocalDSImpl
import com.example.gitrepoviewer.data.repository.repo_details.RepoDetailsRemoteDSImpl
import com.example.gitrepoviewer.data.repository.repo_details.RepoDetailsRepositoryImpl
import com.example.gitrepoviewer.data.repository.repo_issues.RepoIssuesLocalDSImpl
import com.example.gitrepoviewer.data.repository.repo_issues.RepoIssuesRemoteDSImpl
import com.example.gitrepoviewer.data.repository.repo_issues.RepoIssuesRepositoryImpl
import com.example.gitrepoviewer.domain.repository.repo.IReposLocalDS
import com.example.gitrepoviewer.domain.repository.repo.IReposRemoteDS
import com.example.gitrepoviewer.domain.repository.repo.IReposRepository
import com.example.gitrepoviewer.domain.repository.repo_details.IRepoDetailsLocalDS
import com.example.gitrepoviewer.domain.repository.repo_details.IRepoDetailsRemoteDS
import com.example.gitrepoviewer.domain.repository.repo_details.IRepoDetailsRepository
import com.example.gitrepoviewer.domain.repository.repo_issues.IRepoIssuesLocalDS
import com.example.gitrepoviewer.domain.repository.repo_issues.IRepoIssuesRemoteDS
import com.example.gitrepoviewer.domain.repository.repo_issues.IRepoIssuesRepository
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
    fun provideGitRepoRepository(remoteDS: IReposRemoteDS, localDS: IReposLocalDS): IReposRepository {
        return ReposRepositoryImpl(remoteDS, localDS)
    }

    @Singleton
    @Provides
    fun provideRepoDetailsRepository(remoteDS: IRepoDetailsRemoteDS, localDS: IRepoDetailsLocalDS): IRepoDetailsRepository {
        return RepoDetailsRepositoryImpl(remoteDS, localDS)
    }

    @Singleton
    @Provides
    fun provideRepoIssuesRepository(remoteDS: IRepoIssuesRemoteDS, localDS: IRepoIssuesLocalDS): IRepoIssuesRepository {
        return RepoIssuesRepositoryImpl(remoteDS, localDS)
    }



    @Provides
    @Singleton
    fun provideRepoRemoteDS(gitHubApiService: GitHubApiService): IReposRemoteDS {
        return ReposRemoteDSImpl(gitHubApiService)
    }

    @Provides
    @Singleton
    fun provideRepoDetailsRemoteDS(gitHubApiService: GitHubApiService): IRepoDetailsRemoteDS {
        return RepoDetailsRemoteDSImpl(gitHubApiService)
    }

    @Provides
    @Singleton
    fun provideRepoIssuesRemoteDS(gitHubApiService: GitHubApiService): IRepoIssuesRemoteDS {
        return RepoIssuesRemoteDSImpl(gitHubApiService)
    }


    @Provides
    @Singleton
    fun provideRepoLocalDS(repositoryDAO: RepositoryDAO): IReposLocalDS{
        return ReposLocalDSImpl(repositoryDAO)
    }

    @Provides
    @Singleton
    fun provideRepoDetailsLocalDS(repositoryDetailsDAO: RepositoryDetailsDAO): IRepoDetailsLocalDS{
        return RepoDetailsLocalDSImpl(repositoryDetailsDAO)
    }

    @Provides
    @Singleton
    fun provideRepoIssuesLocalDS(repositoryDetailsDAO: RepositoryDetailsDAO): IRepoIssuesLocalDS{
        return RepoIssuesLocalDSImpl(repositoryDetailsDAO)
    }


}