package com.example.gitrepoviewer.presentation.details

import android.os.Build
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material.icons.filled.StarOutline
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.gitrepoviewer.R
import com.example.gitrepoviewer.domain.model.RepositoryDetails
import com.example.gitrepoviewer.presentation.navigation.RepoScreens
import com.example.gitrepoviewer.util.UtilFunctions.Companion.formatDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepoDetailsScreen(
    navController: NavController = rememberNavController(),
    ownerName: String?,
    repoName: String?,
    repoId: Long?
) {
    Scaffold(topBar = {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = stringResource(R.string.details),
                    style = TextStyle(
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
            },
            navigationIcon = {IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }},
            scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
            colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
        )
    }, content = {
        Surface(
            modifier = Modifier
                .padding(top = it.calculateTopPadding())
                .fillMaxHeight(),
            color = MaterialTheme.colorScheme.surface
        ) {
            val viewModel = hiltViewModel<RepoDetailsViewModel>()
            HandlingErrors(viewModel)
            MainContent(navController, viewModel, ownerName, repoName, repoId)
        }
    })
}

@Composable
fun MainContent(
    navController: NavController,
    viewModel: RepoDetailsViewModel,
    ownerName: String?,
    repoName: String?,
    repoId: Long?
) {

    val repoDetails = viewModel.viewState.collectAsState().value.repoDetails
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
        if (repoDetails == null){
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface)
            )
        }else{
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top,
                modifier = Modifier.background(MaterialTheme.colorScheme.surface)
            ) {

                OwnerImageAndName(repoDetails)

                Text(
                    text = repoDetails.name ?: "Repo name",
                    style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface),
                    modifier = Modifier.padding(start = 12.dp, top = 4.dp)
                )

                Text(
                    text = repoDetails.description ?: "Description",
                    style = TextStyle(fontSize = 16.sp, color = MaterialTheme.colorScheme.onSecondary),
                    modifier = Modifier.padding(start = 12.dp, top = 16.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                StarsAndForksRow(repoDetails)

                WatchingRow(repoDetails)

                OpenIssuesRow(repoDetails, navController, ownerName, repoName, repoId)

                CreatedAtRow(repoDetails)

            }
        }
    }

}


@Composable
private fun CreatedAtRow(repositoryDetails: RepositoryDetails?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .background(MaterialTheme.colorScheme.surface)
            .padding(start = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    )
    {
        Icon(
            imageVector = Icons.Default.DateRange,
            contentDescription = null,
            tint = Color.Gray
        )
        Text(
            text = "created at:",
            Modifier.padding(start = 4.dp),
            color = MaterialTheme.colorScheme.onSecondary
        )
        Text(
            text = repositoryDetails?.createdAt?.let {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    formatDate(it)
                } else {
                    repositoryDetails.createdAt
                }
            } ?: "N/A",
            Modifier.padding(start = 4.dp),
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        )
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
private fun OpenIssuesRow(
    repositoryDetails: RepositoryDetails?,
    navController: NavController,
    ownerName: String?,
    repoName: String?,
    repoId: Long?
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .background(MaterialTheme.colorScheme.surface)
            .padding(start = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    )
    {
        Icon(
            imageVector = Icons.Default.ErrorOutline,
            contentDescription = null,
            tint = Color.Gray
        )
        Text(
            text = repositoryDetails?.openIssuesCount?.toString() ?: "N/A",
            Modifier.padding(start = 4.dp),
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        )
        Text(
            text = "issues",
            Modifier.padding(start = 4.dp),
            color = MaterialTheme.colorScheme.onSecondary
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(text = "View issues",
            style = TextStyle(
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.W500,
                fontSize = 16.sp
            ),
            modifier = Modifier
                .padding(end = 12.dp)
                .clickable {
                    navController.navigate(RepoScreens.RepoIssuesScreen.name + "/$ownerName" + "/$repoName" + "/$repoId")
                })
    }
}

@Composable
private fun WatchingRow(repositoryDetails: RepositoryDetails?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .background(MaterialTheme.colorScheme.surface)
            .padding(start = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    )
    {
        Icon(
            imageVector = Icons.Default.RemoveRedEye,
            contentDescription = null,
            tint = Color.Gray
        )
        Text(
            text = repositoryDetails?.subscribersCount?.toString() ?: "N/A",
            Modifier.padding(start = 4.dp),
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        )
        Text(
            text = "watching",
            Modifier.padding(start = 4.dp),
            color = MaterialTheme.colorScheme.onSecondary
        )
    }
}

@Composable
private fun StarsAndForksRow(repositoryDetails: RepositoryDetails?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(30.dp)
            .background(MaterialTheme.colorScheme.surface)
            .padding(start = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    )
    {
        Icon(
            imageVector = Icons.Default.StarOutline,
            contentDescription = null,
            tint = Color.Gray
        )
        Text(
            text = repositoryDetails?.stargazersCount?.toString() ?: "N/A",
            Modifier.padding(start = 4.dp),
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        )
        Text(
            text = "stars",
            Modifier.padding(start = 4.dp),
            color = MaterialTheme.colorScheme.onSecondary
        )
        Spacer(modifier = Modifier.width(12.dp))
        Icon(
            painter = painterResource(id = R.drawable.git_fork),
            contentDescription = null,
            tint = Color.Black
        )
        Text(
            text = repositoryDetails?.forksCount?.toString() ?: "N/A",
            Modifier.padding(start = 4.dp),
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        )
        Text(
            text = "forks",
            Modifier.padding(start = 4.dp),
            color = MaterialTheme.colorScheme.onSecondary
        )
    }
}

@Composable
private fun OwnerImageAndName(repositoryDetails: RepositoryDetails?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(MaterialTheme.colorScheme.surface),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Surface(
            modifier = Modifier
                .padding(12.dp)
                .size(20.dp), shape = CircleShape, shadowElevation = 4.dp
        ) {

            AsyncImage(
                model = repositoryDetails?.owner?.avatarURL,
                contentDescription = null,
            )
        }

        Spacer(modifier = Modifier.width(4.dp))
        Column {
            Text(
                text = repositoryDetails?.owner?.login ?: "Owner name",
                style = TextStyle(color = MaterialTheme.colorScheme.onSecondary, fontSize = 14.sp)
            )
        }
    }
}

@Composable
private fun HandlingErrors(viewModel: RepoDetailsViewModel) {
    val errorMessage = viewModel.viewState.collectAsState().value.error
    val context = LocalContext.current
    errorMessage?.let { message ->
        Toast.makeText(context, message + stringResource(id = R.string.showing_old_data), Toast.LENGTH_SHORT).show()
        viewModel.clearErrorMessage()
    }
}