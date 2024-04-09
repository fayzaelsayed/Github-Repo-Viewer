package com.example.gitrepoviewer.data.local

import androidx.room.TypeConverter
import com.example.gitrepoviewer.data.model.dto.OwnerDTO
import com.example.gitrepoviewer.data.model.dto.RepositoryIssuesDTO
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class DataTypeConverters {

    @TypeConverter
    fun fromOwnerToString(owner: OwnerDTO?): String {
        return owner?.let {
            Json.encodeToString(owner)
        } ?: ""
    }

    @TypeConverter
    fun toOwnerFromString(json: String): OwnerDTO {
        return Json.decodeFromString(json)
    }

    @TypeConverter
    fun fromIssuesListToString(listOfIssues: List<RepositoryIssuesDTO>?): String {
        return listOfIssues?.let {
            Json.encodeToString(listOfIssues)
        } ?: ""
    }

    @TypeConverter
    fun toIssuesListFromString(json: String): List<RepositoryIssuesDTO> {
        return Json.decodeFromString(json)
    }

}