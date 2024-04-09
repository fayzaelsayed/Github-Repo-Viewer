package com.example.gitrepoviewer.common

sealed class Resource<out T> {
    data class Loading(val loading: Boolean): Resource<Nothing>()
    data class Success<T>(val data: T?): Resource<T>()
    data class Failure(val exception: RepoViewerException) : Resource<Nothing>()
}