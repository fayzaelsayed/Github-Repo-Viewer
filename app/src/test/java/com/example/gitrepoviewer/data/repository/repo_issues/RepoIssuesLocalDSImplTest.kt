package com.example.gitrepoviewer.data.repository.repo_issues

import com.example.gitrepoviewer.data.local.dao.RepositoryDetailsDAO
import com.example.gitrepoviewer.data.model.dto.RepositoryIssuesDTO
import com.example.gitrepoviewer.data.model.entity.RepositoryDetailsEntity
import com.example.gitrepoviewer.domain.repository.repo_issues.IRepoIssuesLocalDS
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class RepoIssuesLocalDSImplTest {

    @Mock
    private lateinit var detailsDao: RepositoryDetailsDAO

    private lateinit var issuesLocalDS: IRepoIssuesLocalDS
    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        issuesLocalDS = RepoIssuesLocalDSImpl(detailsDao)
    }

    @Test
    fun `getRepoIssues() inserting list of one repo issue and retrieve it, return same list with one correct issue`() = runTest {
        val repoIssueList = listOf(RepositoryIssuesDTO(1))
        val expectedDetails = RepositoryDetailsEntity(id = 1, repoIssues = repoIssueList)
        Mockito.`when`(detailsDao.getRepoDetails(1)).thenReturn(expectedDetails)
        val actualDetails = issuesLocalDS.getRepoIssues(1)
        assert(actualDetails.isNotEmpty())
        assert(actualDetails == expectedDetails.repoIssues)
    }
}