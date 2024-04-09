package com.example.gitrepoviewer.data.mapper

import com.example.gitrepoviewer.common.DataState
import com.example.gitrepoviewer.data.model.dto.RepositoryDTO
import com.example.gitrepoviewer.data.model.dto.RepositoryDetailsDTO
import com.example.gitrepoviewer.data.model.dto.RepositoryIssuesDTO
import com.example.gitrepoviewer.data.model.entity.RepositoryDetailsEntity
import com.example.gitrepoviewer.data.model.entity.RepositoryEntity
import com.example.gitrepoviewer.domain.model.Repository
import com.example.gitrepoviewer.domain.model.RepositoryDetails
import com.example.gitrepoviewer.domain.model.RepositoryIssues

object RepoViewerMapper {

    // ** Repos screen mappers ** \\

    fun mapRepoDomainToEntity(repository: Repository): RepositoryEntity{
        return RepositoryEntity(
            id = repository.id,
            name = repository.name,
            fullName = repository.fullName,
            owner = repository.owner,
            description = repository.description,
        )
    }

    private fun mapRepoDtoToDomain(repositoryDTO: RepositoryDTO): Repository{
        return Repository(
            id = repositoryDTO.id,
            name = repositoryDTO.name,
            fullName = repositoryDTO.fullName,
            owner = repositoryDTO.owner,
            description = repositoryDTO.description,
        )
    }

    fun mapRepoEntityToDomain(repositoryEntity: RepositoryEntity): Repository{
        return Repository(
            id = repositoryEntity.id,
            name = repositoryEntity.name,
            fullName = repositoryEntity.fullName,
            owner = repositoryEntity.owner,
            description = repositoryEntity.description,
        )
    }


    fun mapDataStateRepoDtoToDomain(dataState: DataState<List<RepositoryDTO>>): DataState<List<Repository>>{
        return DataState(
            status = dataState.status,
            data = dataState.data?.map { mapRepoDtoToDomain(it) },
            error = dataState.error
        )
    }


    // ** Details screen mappers ** \\

    fun mapRepoDetailsDomainToEntity(repositoryDetails: RepositoryDetails): RepositoryDetailsEntity {
        return RepositoryDetailsEntity(
            id = repositoryDetails.id,
            name = repositoryDetails.name,
            fullName = repositoryDetails.fullName,
            owner = repositoryDetails.owner,
            description = repositoryDetails.description,
            forksCount = repositoryDetails.forksCount,
            stargazersCount = repositoryDetails.stargazersCount,
            openIssuesCount = repositoryDetails.openIssuesCount,
            createdAt = repositoryDetails.createdAt,
            subscribersCount = repositoryDetails.subscribersCount,
            repoIssues = repositoryDetails.repo_issues
        )
    }

    private fun mapRepoDetailsDtoToDomain(repositoryDetailsDTO: RepositoryDetailsDTO): RepositoryDetails {
        return RepositoryDetails(
            id = repositoryDetailsDTO.id,
            name = repositoryDetailsDTO.name,
            fullName = repositoryDetailsDTO.fullName,
            owner = repositoryDetailsDTO.owner,
            description = repositoryDetailsDTO.description,
            forksCount = repositoryDetailsDTO.forksCount,
            stargazersCount = repositoryDetailsDTO.stargazersCount,
            openIssuesCount = repositoryDetailsDTO.openIssuesCount,
            createdAt = repositoryDetailsDTO.createdAt,
            subscribersCount = repositoryDetailsDTO.subscribersCount,
            repo_issues = emptyList()
        )
    }

    fun mapRepoDetailsEntityToDomain(repositoryDetailsEntity: RepositoryDetailsEntity?): RepositoryDetails? {
        repositoryDetailsEntity?.let {
            return RepositoryDetails(
                id = repositoryDetailsEntity.id,
                name = repositoryDetailsEntity.name,
                fullName = repositoryDetailsEntity.fullName,
                owner = repositoryDetailsEntity.owner,
                description = repositoryDetailsEntity.description,
                forksCount = repositoryDetailsEntity.forksCount,
                stargazersCount = repositoryDetailsEntity.stargazersCount,
                openIssuesCount = repositoryDetailsEntity.openIssuesCount,
                createdAt = repositoryDetailsEntity.createdAt,
                subscribersCount = repositoryDetailsEntity.subscribersCount,
                repo_issues = repositoryDetailsEntity.repoIssues
            )
        } ?: return null
    }

    fun mapRepoIssuesDomainToDTO(repositoryIssues: RepositoryIssues): RepositoryIssuesDTO {
        return RepositoryIssuesDTO(
            id = repositoryIssues.id,
            state = repositoryIssues.state,
            title = repositoryIssues.title,
            user = repositoryIssues.user,
            createdAt = repositoryIssues.createdAt
        )
    }

    fun mapRepoIssuesDtoToDomain(repositoryIssuesDTO: RepositoryIssuesDTO): RepositoryIssues {
        return RepositoryIssues(
            id = repositoryIssuesDTO.id,
            state = repositoryIssuesDTO.state,
            title = repositoryIssuesDTO.title,
            user = repositoryIssuesDTO.user,
            createdAt = repositoryIssuesDTO.createdAt
        )
    }

    fun mapDataStateDetailsDtoToDomain(dataState: DataState<RepositoryDetailsDTO>): DataState<RepositoryDetails>{
        return DataState(
            status = dataState.status,
            data = dataState.data?.let { mapRepoDetailsDtoToDomain(dataState.data) } ,
            error = dataState.error
        )
    }

    fun mapDataStateIssueDtoToDomain(dataState: DataState<List<RepositoryIssuesDTO>>): DataState<List<RepositoryIssues>>{
        return DataState(
            status = dataState.status,
            data = dataState.data?.map { mapRepoIssuesDtoToDomain(it) } ,
            error = dataState.error
        )
    }

}