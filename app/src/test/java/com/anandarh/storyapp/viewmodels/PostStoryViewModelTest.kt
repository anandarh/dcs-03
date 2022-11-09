package com.anandarh.storyapp.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.anandarh.storyapp.models.ResponseModel
import com.anandarh.storyapp.repositories.StoriesRepository
import com.anandarh.storyapp.utils.DataState
import com.anandarh.storyapp.utils.DummyData
import com.anandarh.storyapp.utils.MainCoroutineRule
import com.anandarh.storyapp.utils.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import java.io.File

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class PostStoryViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var storiesRepository: StoriesRepository
    private lateinit var postStoryViewModel: PostStoryViewModel

    private val dummyResponse = DummyData.generateDummyDefaultResponse()

    @Before
    fun setUp() {
        postStoryViewModel = PostStoryViewModel(storiesRepository)
    }

    @Test
    fun `Post New Story Success`(): Unit = mainCoroutineRule.testScope.runTest {

        val response: Flow<DataState<ResponseModel>> = flow {
            emit(DataState.Success(dummyResponse))
        }

        Mockito.`when`(
            storiesRepository.postStory(
                description = "Test",
                photo = File("/path"),
                lat = 0.0,
                lon = 0.0
            )
        ).thenReturn(response)

        postStoryViewModel.postStory(
            description = "Test",
            photo = File("/path"),
            lat = 0.0,
            lon = 0.0
        )

        advanceUntilIdle()

        val actualResponse = postStoryViewModel.postState.getOrAwaitValue()

        assertNotNull(actualResponse)
        assertEquals(
            actualResponse,
            DataState.Success(dummyResponse)
        )
        assertTrue(actualResponse is DataState.Success)
    }

    @Test
    fun `Post New Story Failed`(): Unit = mainCoroutineRule.testScope.runTest {

        val response: Flow<DataState<ResponseModel>> = flow {
            emit(DataState.Error(Exception()))
        }

        Mockito.`when`(
            storiesRepository.postStory(
                description = "Test",
                photo = File("/path"),
                lat = 0.0,
                lon = 0.0
            )
        ).thenReturn(response)

        postStoryViewModel.postStory(
            description = "Test",
            photo = File("/path"),
            lat = 0.0,
            lon = 0.0
        )

        advanceUntilIdle()

        val actualResponse = postStoryViewModel.postState.getOrAwaitValue()

        assertNotNull(actualResponse)
        assertTrue(actualResponse is DataState.Error)
    }

}