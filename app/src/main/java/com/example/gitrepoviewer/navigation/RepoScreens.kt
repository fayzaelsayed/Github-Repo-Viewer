package com.example.gitrepoviewer.navigation

enum class RepoScreens {
    ReposScreen,
    RepoDetailsScreen,
    RepoIssuesScreen;

    companion object{
        fun fromRoute(route:String?): RepoScreens = when(route?.substringBefore("/")){
            ReposScreen.name -> ReposScreen
            RepoDetailsScreen.name -> RepoDetailsScreen
            RepoIssuesScreen.name -> RepoIssuesScreen
            null -> ReposScreen
            else -> throw IllegalArgumentException("Route $route is not recognized")
        }
    }
}