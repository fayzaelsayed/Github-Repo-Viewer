package com.example.gitrepoviewer.common

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

abstract class BaseUseCase {
    protected suspend fun<MODEL> executeApiRequest(networkCall: suspend () -> DataState<MODEL>, databaseCall: (suspend () -> Unit)? =null): Flow<Resource<MODEL>> =
        flow {
            emit(Resource.Loading(true))
            val responseStatus = networkCall.invoke()
            if (responseStatus.status == DataState.Status.SUCCESS) {
                databaseCall?.invoke()
                emit(Resource.Success(responseStatus.data))
            } else if (responseStatus.status == DataState.Status.ERROR) {
                emit(Resource.Failure(
                    ExceptionMapper.mapExceptionToRepoViewerException(
                        responseStatus.error?.exception,
                        responseStatus.error?.errorCode
                    )))
            }
        }

    protected suspend fun<MODEL> executeDatabaseRequest(databaseCall: suspend () -> MODEL): Flow<Resource<MODEL>> =
        flow {
            try {
                emit(Resource.Loading(true))
                val databaseResponse = databaseCall.invoke()
                if (databaseResponse != null) {
                    emit(Resource.Success(databaseResponse))
                }else{
                    emit(Resource.Failure(RepoViewerException.CustomException("No cached data")))
                }
            }catch (e: Exception){
                emit(Resource.Failure(ExceptionMapper.mapExceptionToRepoViewerException(e)))
            }
        }
}