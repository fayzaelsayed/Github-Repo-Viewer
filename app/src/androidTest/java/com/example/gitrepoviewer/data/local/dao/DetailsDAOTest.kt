package com.example.gitrepoviewer.data.local.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.example.gitrepoviewer.data.local.AppDatabase
import com.example.gitrepoviewer.data.local.entities.RepoDetailsEntity
import com.example.gitrepoviewer.domain.model.Owner
import com.example.gitrepoviewer.domain.model.RepoIssuesModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

@ExperimentalCoroutinesApi
@SmallTest
@HiltAndroidTest
class DetailsDAOTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("repo_db")
    lateinit var database: AppDatabase
    private lateinit var dao: DetailsDAO

    @Before
    fun setup() {
        hiltRule.inject()
        dao = database.detailsDao()
    }

    @Test
    fun insertRepoDetailsAndRetrieveIt_returnSameRepoDetailsWithTheCorrectValues() = runBlockingTest {
        val repoDetailsEntity = RepoDetailsEntity(1, "","fayza", "fayze's repo", Owner(), "", repo_issues = emptyList())
        dao.addRepoDetails(repoDetailsEntity)
        val details = dao.getRepoDetails(1).first()
        assert(details.name.equals("fayza"))
    }

    @Test
    fun insertRepoDetailsThenUpdateItsValue_returnAnUpdatedValueOfThisRepoDetails() = runBlockingTest {
        val repoDetailsEntity = RepoDetailsEntity(1, "","fayza", "fayze's repo", Owner(), "", repo_issues = emptyList())
        dao.addRepoDetails(repoDetailsEntity)
        dao.updateRepoIssues(1, listOf(RepoIssuesModel(1)))
        val details = dao.getRepoDetails(1).first()
        assert(details.repo_issues.isNotEmpty())
    }

    @After
    fun teardown() {
        database.close()
    }
}