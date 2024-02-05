package com.example.gitrepoviewer.data.local

import androidx.room.TypeConverter
import com.example.gitrepoviewer.domain.model.Owner
import com.example.gitrepoviewer.domain.model.RepoIssuesModel
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class DataTypeConverters {

    @TypeConverter
    fun fromOwnerToString(owner: Owner?): String {
        return owner?.let {
            Json.encodeToString(owner)
        } ?: ""
    }

    @TypeConverter
    fun toOwnerFromString(json: String): Owner {
        return Json.decodeFromString(json)
    }

    @TypeConverter
    fun fromIssuesListToString(listOfIssues: List<RepoIssuesModel>?): String {
        return listOfIssues?.let {
            Json.encodeToString(listOfIssues)
        } ?: ""
    }

    @TypeConverter
    fun toIssuesListFromString(json: String): List<RepoIssuesModel> {
        return Json.decodeFromString(json)
    }

}