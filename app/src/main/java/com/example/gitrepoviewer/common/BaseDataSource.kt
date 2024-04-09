package com.example.gitrepoviewer.common

import retrofit2.Response

abstract class BaseDataSource {
    protected suspend fun <T> getResult(call: suspend () -> Response<T>): DataState<T> {
        try {
            val response = call()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null)  return DataState.success(body)
            }

            return DataState.error(DataState.HttpErrorAndException(errorCode = response.code()))

        } catch (e: Exception) {
            return DataState.error(DataState.HttpErrorAndException(exception = e))
        }
    }

}