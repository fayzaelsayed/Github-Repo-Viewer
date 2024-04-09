package com.example.gitrepoviewer.presentation.repos

import android.widget.Toast
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
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
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
import com.example.gitrepoviewer.domain.model.Repository
import com.example.gitrepoviewer.presentation.navigation.RepoScreens
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch


@Preview
@Composable
fun ReposScreen(navController: NavController = rememberNavController()) {
    val snackBarHostState = remember {
        SnackbarHostState()
    }
    val viewModel = hiltViewModel<ReposViewModel>()

    HandleErrorWithSnackBar(viewModel, snackBarHostState)

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
private fun MainContent(navController: NavController, viewModel: ReposViewModel) {
    val isLoading = viewModel.viewState.collectAsState().value.isLoading

    if (isLoading) {
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
        DataContent(navController, viewModel)
    }


}


@Composable
private fun DataContent(navController: NavController, viewModel: ReposViewModel) {
    val reposList = viewModel.viewState.collectAsState().value.reposList
    val filteredReposList = viewModel.viewState.collectAsState().value.filteredReposList
    val searchText by viewModel.searchText.collectAsState()
    val isSearching = viewModel.viewState.collectAsState().value.isSearching
    val isPaginating = viewModel.viewState.collectAsState().value.isPaginating
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = false)
    val listState = rememberLazyListState()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .fillMaxSize()
    ) {

        OutlinedTextField(
            value = searchText,
            onValueChange = {
                viewModel.paginate = it.isEmpty()
                viewModel.searchWithFakeDelay(it)
            },
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = MaterialTheme.colorScheme.tertiary,
                unfocusedIndicatorColor = Color.LightGray,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
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
                onRefresh = { viewModel.fetchRepos() }) {
                reposList?.let {
                    if (reposList.isEmpty()) {
                        Toast.makeText(
                            LocalContext.current,
                            stringResource(R.string.no_data_available_please_refresh),
                            Toast.LENGTH_LONG
                        ).show()
                        EmptyListView()
                    } else {
                        if (filteredReposList.isNullOrEmpty()) {
                            EmptyListView()
                        } else {
                            LazyColumn(state = listState) {
                                items(items = filteredReposList) { repo ->
                                    RepoRow(repository = repo) {
                                        navController.navigate(RepoScreens.RepoDetailsScreen.name + "/${it.owner?.login}" + "/${it.name}" + "/${it.id}")
                                    }
                                }
                                if (isPaginating) {
                                    item {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 16.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            CircularProgressIndicator()
                                        }
                                    }
                                }
                            }

                            LaunchedEffect(listState) {
                                snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull() }
                                    .collect { visibleItem ->
                                        val totalItems = listState.layoutInfo.totalItemsCount
                                        val lastVisibleIndex = visibleItem?.index ?: 0

                                        if (lastVisibleIndex >= totalItems - 1) {
                                            if (viewModel.paginate) {
                                                viewModel.getRepos(viewModel.pageNumber + 1)
                                                viewModel.pageNumber = viewModel.pageNumber + 1
                                                viewModel.paginate = false
                                            }
                                        }
                                    }
                            }
                        }

                    }
                }
            }
        }
    }
}


@Composable
private fun EmptyListView() {
    Column(
        Modifier
            .verticalScroll(rememberScrollState())
            .height(600.dp)
    ) {
        Spacer(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
        )
    }
}


@Preview
@Composable
private fun RepoRow(repository: Repository = Repository(), onItemClick: (Repository) -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(66.dp)
            .background(MaterialTheme.colorScheme.surface)
            .clickable {
                onItemClick(repository)
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Surface(
            modifier = Modifier
                .padding(12.dp)
                .size(32.dp), shape = CircleShape, shadowElevation = 4.dp
        ) {
            repository.owner?.avatarURL?.let {
                AsyncImage(
                    model = repository.owner.avatarURL,
                    contentDescription = null,
                )
            } ?: Icon(imageVector = Icons.Default.AccountBox, contentDescription = null)
        }
        Spacer(modifier = Modifier.width(4.dp))
        Column {
            Text(
                text = repository.owner?.login ?: "Owner name",
                style = TextStyle(color = MaterialTheme.colorScheme.onSecondary, fontSize = 14.sp)
            )
            Text(
                text = repository.name ?: "Repo name",
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W500,
                    color = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
private fun HandleErrorWithSnackBar(
    viewModel: ReposViewModel,
    snackBarHostState: SnackbarHostState
) {
    val snackBarScope = rememberCoroutineScope()
    val errorMessage = viewModel.viewState.collectAsState().value.error
    val context = LocalContext.current
    errorMessage?.let { message ->
        if (message.lowercase().contains("no more data")){
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            viewModel.paginate = false
        }else {
            snackBarScope.launch {
                val snackBarResult = snackBarHostState.showSnackbar(
                    message = message + context.getString(R.string.showing_old_data),
                    actionLabel = context.getString(R.string.retry),
                    duration = SnackbarDuration.Long
                )
                if (snackBarResult == SnackbarResult.ActionPerformed) {
                    viewModel.fetchRepos()
                }
            }
        }
        viewModel.clearErrorMessage()
    }
}