package com.example.gitrepoviewer.data.local.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.example.gitrepoviewer.data.local.AppDatabase
import com.example.gitrepoviewer.data.local.entities.RepoEntity
import com.example.gitrepoviewer.domain.model.Owner
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
class RepoDAOTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("repo_db")
    lateinit var database: AppDatabase
    private lateinit var dao: RepoDAO

    @Before
    fun setup() {
        hiltRule.inject()
        dao = database.repoDao()
    }

    @Test
    fun insertValidRepoGetAllReposAfter_returnReposListContainsMyInsertedRepo() = runBlockingTest {
        val repoEntity = RepoEntity(1, "fayza", "fayze's repo", Owner(), "")
        dao.addRepo(repoEntity)
        val allRepositories = dao.getAllRepos().first()
        assert(allRepositories.contains(repoEntity))
    }

    @After
    fun teardown() {
        database.close()
    }
}