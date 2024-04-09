package com.example.gitrepoviewer.data.repository.repo_details

import com.example.gitrepoviewer.data.local.dao.RepositoryDetailsDAO
import com.example.gitrepoviewer.data.model.entity.RepositoryDetailsEntity
import com.example.gitrepoviewer.domain.repository.repo_details.IRepoDetailsLocalDS
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class RepoDetailsLocalDSImplTest {


    @Mock
    private lateinit var detailsDao: RepositoryDetailsDAO
    private lateinit var detailsLocalDS : IRepoDetailsLocalDS

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        detailsLocalDS = RepoDetailsLocalDSImpl(detailsDao)
    }

    @Test
    fun `getRepoDetails() inserting repo details and retrieve it, return same repo details with correct values`() = runTest {
        val repoDetails = RepositoryDetailsEntity(1, name = "fayza", repoIssues = emptyList())
        Mockito.`when`(detailsDao.getRepoDetails(1)).thenReturn(repoDetails)
        val result = detailsLocalDS.getRepoDetails(1)
        assert(result != null)
        assert(result == repoDetails)
    }
}