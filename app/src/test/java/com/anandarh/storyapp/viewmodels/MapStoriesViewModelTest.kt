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
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@Suppress("DEPRECATION")
@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MapStoriesViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var storiesRepository: StoriesRepository
    private lateinit var mapStoriesViewModel: MapStoriesViewModel

    private val dummyResponse = DummyData.generateDummyMapStoriesResponse()

    @Before
    fun setUp() {
        mapStoriesViewModel = MapStoriesViewModel(storiesRepository)
    }

    @Test
    fun `Get Map Story List Success`(): Unit = mainCoroutineRule.runBlockingTest {

        val response: Flow<DataState<ResponseModel>> = flow {
            emit(DataState.Success(dummyResponse))
        }

        Mockito.`when`(
            storiesRepository.fetchStories(1, 50, 1)
        ).thenReturn(response)

        mapStoriesViewModel.fetchStoriesWithLocation()


        val actualResponse = mapStoriesViewModel.storiesWithLocation.getOrAwaitValue()


        assertTrue(actualResponse is DataState.Success)
        assertNotNull(actualResponse)
        assertEquals(
            actualResponse,
            DataState.Success(dummyResponse)
        )
        assertEquals((actualResponse as DataState.Success).data.listStory?.size, 11)
    }

    @Test
    fun `Get Map Story List Failed`(): Unit = mainCoroutineRule.runBlockingTest {

        val response: Flow<DataState<ResponseModel>> = flow {
            emit(DataState.Error(Exception()))
        }

        Mockito.`when`(
            storiesRepository.fetchStories(1, 50, 1)
        ).thenReturn(response)

        mapStoriesViewModel.fetchStoriesWithLocation()

        val actualResponse = mapStoriesViewModel.storiesWithLocation.getOrAwaitValue()

        assertNotNull(actualResponse)
        assertTrue(actualResponse is DataState.Error)
    }

}