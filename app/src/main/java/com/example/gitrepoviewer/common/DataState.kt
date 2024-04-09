package com.example.gitrepoviewer.common


data class DataState<out T>(
    val status: Status = Status.INACTIVE,
    val data: T? = null,
    val error: HttpErrorAndException? = null
) {

    enum class Status {
        INACTIVE,
        SUCCESS,
        ERROR,
    }

    companion object {
        fun <T> success(data: T?): DataState<T> {
            return DataState(Status.SUCCESS, data, null)
        }

        fun <T> error(error: HttpErrorAndException?, data: T? = null): DataState<T> {
            return DataState(Status.ERROR, data, error)
        }
    }

    class HttpErrorAndException(
        val exception: Exception? = null,
        val errorCode: Int? = null,
    )
}