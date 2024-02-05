package com.example.gitrepoviewer.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.gitrepoviewer.presentation.details.RepoDetailsScreen
import com.example.gitrepoviewer.presentation.issues.RepoIssuesScreen
import com.example.gitrepoviewer.presentation.repos.ReposScreen

@Composable
fun RepoNavigation(){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = RepoScreens.ReposScreen.name){
        composable(RepoScreens.ReposScreen.name){
            // here we pass where this should lead us to
            ReposScreen(navController = navController)
        }

        composable(RepoScreens.RepoDetailsScreen.name + "/{ownerName}" + "/{repoName}" + "/{repoId}", arguments = listOf(navArgument(name = "ownerName") {type = NavType.StringType}, navArgument(name = "repoName") {type = NavType.StringType}, navArgument(name = "repoId") {type = NavType.LongType})){ backStackEntry ->
            RepoDetailsScreen(navController = navController, backStackEntry.arguments?.getString("ownerName"), backStackEntry.arguments?.getString("repoName"), backStackEntry.arguments?.getLong("repoId"))
        }

        composable(RepoScreens.RepoIssuesScreen.name + "/{repoId}", arguments = listOf(navArgument(name = "repoId") {type = NavType.LongType})){ backStackEntry ->
            RepoIssuesScreen(navController = navController, backStackEntry.arguments?.getLong("repoId"))
        }
    }
}