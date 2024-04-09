package com.example.gitrepoviewer.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.gitrepoviewer.presentation.navigation.RepoNavigation
import com.example.gitrepoviewer.presentation.theme.GitRepoViewerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp {
                RepoNavigation()
            }
        }
    }
}

@Composable
fun MyApp(content: @Composable () -> Unit){
    GitRepoViewerTheme {
        content()
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    GitRepoViewerTheme {
        RepoNavigation()
    }
}