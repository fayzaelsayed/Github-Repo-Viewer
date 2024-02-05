package com.example.gitrepoviewer.presentation.repos

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.gitrepoviewer.R
import com.example.gitrepoviewer.data.local.entities.RepoEntity
import com.example.gitrepoviewer.navigation.RepoScreens
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch

@Preview
@Composable
fun ReposScreen(navController: NavController = rememberNavController()) {
    val snackBarHostState = remember {
        SnackbarHostState()
    }
    val snackBarScope = rememberCoroutineScope()
    val viewModel = hiltViewModel<ReposViewModel>()
    val errorMessageResource = viewModel.errorMessageResourceId.collectAsState(initial = null).value
    val context = LocalContext.current
    errorMessageResource?.let { messageResource ->
        snackBarScope.launch {
            val snackBarResult = snackBarHostState.showSnackbar(
                message = context.getString(messageResource) + context.getString(R.string.showing_old_data),
                actionLabel = context.getString(R.string.retry),
                duration = SnackbarDuration.Long
            )
            if (snackBarResult == SnackbarResult.ActionPerformed) {
                viewModel.retryFetchRepos()
            }
        }
        viewModel.clearErrorMessage()
    }

    Scaffold(snackbarHost = { SnackbarHost(hostState = snackBarHostState) }, topBar = {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 2.dp
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.repositories),
                    style = TextStyle(
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
            }
        }
    }, content = {
        Surface(
            modifier = Modifier.padding(top = it.calculateTopPadding()),
            color = MaterialTheme.colorScheme.surface
        ) {
            MainContent(navController, viewModel)
        }
    })
}

@Composable
fun MainContent(navController: NavController, viewModel: ReposViewModel) {
    val reposList by viewModel.repos.collectAsState(initial = emptyList())
    val searchText by viewModel.searchText.collectAsState()
    val isSearching by viewModel.isSearching.collectAsState()
    val isFetchingDone by viewModel.isFetchingDone.collectAsState()
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = false)

    if (!isFetchingDone) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center, modifier = Modifier
                .fillMaxSize()
                .background(
                    MaterialTheme.colorScheme.surface
                )
        )
        {
            CircularProgressIndicator()
        }
    } else {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .fillMaxSize()
        ) {

            OutlinedTextField(
                value = searchText,
                onValueChange = viewModel::onSearchTextChange,
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = MaterialTheme.colorScheme.tertiary, unfocusedIndicatorColor = Color.LightGray,
                    focusedContainerColor = MaterialTheme.colorScheme.surface, unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedLabelColor = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, top = 6.dp)
                    .fillMaxWidth(),
                label = { Text(text = stringResource(R.string.search)) },
                shape = RoundedCornerShape(16.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Ascii,
                    imeAction = ImeAction.Done
                ),
                maxLines = 2,
            )

            if (isSearching) {
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            } else {
                SwipeRefresh(
                    state = swipeRefreshState,
                    onRefresh = { viewModel.retryFetchRepos() }) {
                    if (reposList.isEmpty()) {
                        Column(
                            Modifier
                                .verticalScroll(rememberScrollState())
                                .height(600.dp)) {
                            Spacer(modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth())
                        }
                    } else {
                        LazyColumn {
                            items(items = reposList) { repo ->
                                RepoRow(repo = repo) {
                                    navController.navigate(RepoScreens.RepoDetailsScreen.name + "/${it.owner?.login}" + "/${it.name}" + "/${it.id}")
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}

@Preview
@Composable
fun RepoRow(repo: RepoEntity = RepoEntity(), onItemClick: (RepoEntity) -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(66.dp)
            .background(MaterialTheme.colorScheme.surface)
            .clickable {
                onItemClick(repo)
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Surface(
            modifier = Modifier
                .padding(12.dp)
                .size(32.dp), shape = CircleShape, shadowElevation = 4.dp
        ) {
            repo.owner?.avatarURL?.let {
                AsyncImage(
                    model = repo.owner.avatarURL,
                    contentDescription = null,
                )
            } ?: Icon(imageVector = Icons.Default.AccountBox, contentDescription = null)
        }
        Spacer(modifier = Modifier.width(4.dp))
        Column {
            Text(
                text = repo.owner?.login ?: "Owner name",
                style = TextStyle(color = MaterialTheme.colorScheme.onSecondary, fontSize = 14.sp)
            )
            Text(
                text = repo.name ?: "Repo name",
                style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.W500, color = MaterialTheme.colorScheme.onSurface),
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}
