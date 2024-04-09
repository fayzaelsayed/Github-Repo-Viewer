package com.example.gitrepoviewer.domain.model

import com.example.gitrepoviewer.data.model.dto.OwnerDTO

data class Repository(
    val id: Long? = null,
    val name: String? = null,
    val fullName: String? = null,
    val owner: OwnerDTO? = null,
    val description: String? = null,
){
    fun doesMatchSearchText(query: String): Boolean{
        val matchingCombination = listOf(
            "$name",
            "${owner?.login}"
        )
        return matchingCombination.any {
            it.contains(query, ignoreCase = true)
        }
    }
}
