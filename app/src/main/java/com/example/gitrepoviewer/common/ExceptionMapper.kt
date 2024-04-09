package com.example.gitrepoviewer.common

import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLHandshakeException

object ExceptionMapper {
    fun mapExceptionToRepoViewerException(exception: Exception? = null, httpErrorCode: Int? = null): RepoViewerException {
        return when (exception) {
            is UnknownHostException -> RepoViewerException.Network.UnknownHostException
            is SocketTimeoutException -> RepoViewerException.Network.SocketTimeoutException
            is ConnectException -> RepoViewerException.Network.ConnectException
            is SSLHandshakeException -> RepoViewerException.Network.SSLHandshakeException
            is IOException -> RepoViewerException.IOException
            else -> {
                if (httpErrorCode != null) {
                    when(httpErrorCode){
                        401 -> RepoViewerException.HTTP.Unauthorized(httpErrorCode)
                        403 -> RepoViewerException.HTTP.AccessDenied(httpErrorCode)
                        404 -> RepoViewerException.HTTP.NotFound(httpErrorCode)
                        500 -> RepoViewerException.HTTP.InternalServerError(httpErrorCode)
                        else -> RepoViewerException.HTTP.UnknownHttpError(httpErrorCode)
                    }
                } else{
                    RepoViewerException.UnknownException
                }
            }
        }
    }
}