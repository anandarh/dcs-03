package com.anandarh.storyapp.viewmodels

import StoryAdapter
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.ListUpdateCallback
import com.anandarh.storyapp.DummyData
import com.anandarh.storyapp.MainCoroutineRule
import com.anandarh.storyapp.models.StoryModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class ListStoryViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var viewModel: ListStoryViewModel

    private val dummyStory = DummyData.generateDummyStories()

    @Test
    fun `Get StoryList Success`() = mainCoroutineRule.testScope.runTest {

        val expectedStories = MutableLiveData<PagingData<StoryModel>>()
        expectedStories.value = PagingData.from(dummyStory)
        `when`(viewModel.fetchStories).thenReturn(expectedStories)

        val actualStories = viewModel.fetchStories.value
        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = listUpdateCallback,
            mainDispatcher = mainCoroutineRule.dispatcher,
            workerDispatcher = mainCoroutineRule.dispatcher
        )
        differ.submitData(actualStories!!)

        advanceUntilIdle()

        Mockito.verify(viewModel).fetchStories
        assertNotNull(actualStories)
        assertNotNull(differ.snapshot())
        assertEquals(dummyStory.size, differ.snapshot().size)
    }

    private val listUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }

}