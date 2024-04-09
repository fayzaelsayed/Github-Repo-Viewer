package com.example.gitrepoviewer.presentation.issues

import android.annotation.SuppressLint
import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.gitrepoviewer.R
import com.example.gitrepoviewer.domain.model.RepositoryIssues
import com.example.gitrepoviewer.util.UtilFunctions

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepoIssuesScreen(
    navController: NavController = rememberNavController()
) {
    Scaffold(topBar = {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = stringResource(R.string.issues),
                    style = TextStyle(
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
            },
            navigationIcon = {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            },
            scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
            colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
        )
    }, content = {
        val viewModel = hiltViewModel<RepoIssuesViewModel>()
        HandlingErrors(viewModel)
        IssuesMainContent(it, viewModel)
    })
}

@Composable
fun IssuesMainContent(paddingValues: PaddingValues, viewModel: RepoIssuesViewModel) {
    Surface(
        modifier = Modifier.padding(top = paddingValues.calculateTopPadding()),
        color = MaterialTheme.colorScheme.surface
    ) {
        val issuesList = viewModel.viewState.collectAsState().value.issues
        val isLoading = viewModel.viewState.collectAsState().value.isLoading
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        } else {
            if (issuesList.isNullOrEmpty()){
                EmptyListView()
            }else {
                LazyColumn {
                    items(items = issuesList) { issue ->
                        IssueRow(issue = issue)
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyListView() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = stringResource(R.string.no_issues_available_for_this_repository), style = TextStyle(
            fontSize = 19.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface
        ) )
    }
}

@Preview
@Composable
fun IssueRow(issue: RepositoryIssues = RepositoryIssues()) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(MaterialTheme.colorScheme.surface),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {

        IssueInfoRow(issue)
        IssueStatusRow(issue)

    }
}

@Composable
fun IssueInfoRow(issue: RepositoryIssues){
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(top = 12.dp)
            .fillMaxWidth()
            .wrapContentHeight()) {
        Surface(
            modifier = Modifier
                .padding(12.dp)
                .size(32.dp), shape = CircleShape, shadowElevation = 4.dp
        ) {
            issue.user?.avatarURL?.let {
                AsyncImage(
                    model = issue.user.avatarURL,
                    contentDescription = null,
                )
            } ?: Icon(imageVector = Icons.Default.AccountBox, contentDescription = null)
        }
        Spacer(modifier = Modifier.width(4.dp))
        Column {
            Text(
                text = issue.user?.login ?: "Owner name",
                style = TextStyle(color = MaterialTheme.colorScheme.onSecondary, fontSize = 14.sp)
            )
            Text(
                text = issue.title ?: "Repo name",
                style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.W500, color = MaterialTheme.colorScheme.onSurface),
                modifier = Modifier.padding(top = 4.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp)
                    .background(MaterialTheme.colorScheme.surface),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            )
            {
                Text(
                    text = "opened on:",
                    style = TextStyle(color = MaterialTheme.colorScheme.onSecondary, fontSize = 12.sp)
                )
                Text(
                    text = issue.createdAt?.let {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            UtilFunctions.formatDate(it)
                        } else {
                            issue.createdAt
                        }
                    } ?: "N/A",
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
        }
    }
}


@Composable
fun IssueStatusRow(issue: RepositoryIssues){
    Row(modifier = Modifier
        .fillMaxWidth()
        .background(MaterialTheme.colorScheme.surface)
        .padding(end = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start)
    {
        Spacer(modifier = Modifier.weight(1f)) // Add this line
        if (issue.state == "open") {
            Box(
                modifier = Modifier
                    .background(
                        Color(0xFF4169e2),
                        shape = RoundedCornerShape(bottomStart = 33.dp, topEnd = 33.dp)
                    )
                    .width(100.dp)
                    .height(26.dp)
            ) {
                Text(
                    text = issue.state,
                    style = TextStyle(fontWeight = FontWeight.Bold, color = Color.White),
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }else{
            Box(
                modifier = Modifier
                    .background(
                        Color(0Xff3ed13e),
                        shape = RoundedCornerShape(bottomStart = 33.dp, topEnd = 33.dp)
                    )
                    .width(100.dp)
                    .height(26.dp)
            ) {
                issue.state?.let {
                    Text(
                        text = issue.state,
                        style = TextStyle(fontWeight = FontWeight.Bold, color = Color.White),
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }

    }
    Spacer(modifier = Modifier.height(12.dp))
}

@Composable
private fun HandlingErrors(viewModel: RepoIssuesViewModel) {
    val errorMessage = viewModel.viewState.collectAsState().value.error
    val context = LocalContext.current
    errorMessage?.let { message ->
        Toast.makeText(context, message + stringResource(id = R.string.showing_old_data), Toast.LENGTH_SHORT).show()
        viewModel.clearErrorMessage()
    }
}