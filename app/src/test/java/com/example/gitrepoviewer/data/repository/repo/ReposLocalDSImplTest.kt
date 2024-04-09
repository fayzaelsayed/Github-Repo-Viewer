package com.example.gitrepoviewer.data.repository.repo

import com.example.gitrepoviewer.data.local.dao.RepositoryDAO
import com.example.gitrepoviewer.data.model.entity.RepositoryEntity
import com.example.gitrepoviewer.domain.repository.repo.IReposLocalDS
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class ReposLocalDSImplTest {

    @Mock
    private lateinit var repoDAO: RepositoryDAO

    private lateinit var reposLocalDS: IReposLocalDS
    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        reposLocalDS = ReposLocalDSImpl(repoDAO)
    }

    @Test
    fun `getAllRepos() inserting one repo in database successfully, result list of one repo is returned`() = runTest {
        val repoEntity = RepositoryEntity(1)
        val expectedList = listOf(repoEntity)
        Mockito.`when`(repoDAO.getAllRepos(0, 20)).thenReturn(expectedList)
        val actualList = reposLocalDS.getAllRepos(0, 20)
        assert(actualList.isNotEmpty())
        assert(actualList == expectedList)
    }
}