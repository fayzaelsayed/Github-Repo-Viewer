package com.example.gitrepoviewer.domain.use_case.repo

import com.example.gitrepoviewer.domain.model.Repository
import javax.inject.Inject

class SearchReposUseCase @Inject constructor(){
    operator fun invoke(reposList: List<Repository>, searchText: String) : List<Repository>{
        return if (searchText.isBlank()){
            reposList
        }else{
            reposList.filter { it.doesMatchSearchText(searchText) }
        }
    }

}