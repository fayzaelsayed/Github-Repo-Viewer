package com.example.gitrepoviewer.data.local.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.example.gitrepoviewer.data.local.AppDatabase
import com.example.gitrepoviewer.data.model.dto.OwnerDTO
import com.example.gitrepoviewer.data.model.entity.RepositoryEntity
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
class RepositoryDAOTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_db")
    lateinit var database: AppDatabase
    private lateinit var dao: RepositoryDAO

    @Before
    fun setup() {
        hiltRule.inject()
        dao = database.repoDao()
    }

    @Test
    fun insertValidRepoGetAllReposAfter_returnReposListContainsMyInsertedRepo() = runBlockingTest {
        val repoEntity = RepositoryEntity(1, "fayza", "fayze's repo", OwnerDTO(), "")
        dao.addRepo(repoEntity)
        val allRepositories = dao.getAllRepos(0,20)
        assert(allRepositories.contains(repoEntity))
    }

    @After
    fun teardown() {
        database.close()
    }
}