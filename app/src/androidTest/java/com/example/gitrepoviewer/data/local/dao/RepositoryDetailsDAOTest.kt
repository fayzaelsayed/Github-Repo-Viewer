package com.example.gitrepoviewer.data.local.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.example.gitrepoviewer.data.local.AppDatabase
import com.example.gitrepoviewer.data.model.dto.OwnerDTO
import com.example.gitrepoviewer.data.model.dto.RepositoryIssuesDTO
import com.example.gitrepoviewer.data.model.entity.RepositoryDetailsEntity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
class RepositoryDetailsDAOTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_db")
    lateinit var database: AppDatabase
    private lateinit var dao: RepositoryDetailsDAO

    @Before
    fun setup() {
        hiltRule.inject()
        dao = database.detailsDao()
    }

    @Test
    fun insertRepoDetailsAndRetrieveIt_returnSameRepoDetailsWithTheCorrectValues() = runBlockingTest {
        val repoDetailsEntity = RepositoryDetailsEntity(1, "fayza", "fayze's repo", OwnerDTO(), "", repoIssues = emptyList())
        dao.addRepoDetails(repoDetailsEntity)
        val details = dao.getRepoDetails(1)
        assert(details?.name.equals("fayza"))
    }

    @Test
    fun insertRepoDetailsThenUpdateItsValue_returnAnUpdatedValueOfThisRepoDetails() = runBlockingTest {
        val repoDetailsEntity = RepositoryDetailsEntity(1, "fayza","fayze's repo",  OwnerDTO(), "", repoIssues = emptyList())
        dao.addRepoDetails(repoDetailsEntity)
        dao.updateRepoIssues(1, listOf(RepositoryIssuesDTO(1)))
        val details = dao.getRepoDetails(1)
        assert(!details?.repoIssues.isNullOrEmpty())
    }

    @After
    fun teardown() {
        database.close()
    }
}