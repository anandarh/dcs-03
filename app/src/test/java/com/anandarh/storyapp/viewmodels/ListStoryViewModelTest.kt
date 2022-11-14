package com.anandarh.storyapp.viewmodels

import StoryAdapter
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.ListUpdateCallback
import com.anandarh.storyapp.models.StoryModel
import com.anandarh.storyapp.repositories.StoriesRepository
import com.anandarh.storyapp.utils.DummyData
import com.anandarh.storyapp.utils.MainCoroutineRule
import com.anandarh.storyapp.utils.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@Suppress("DEPRECATION")
@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class ListStoryViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var repository: StoriesRepository

    private val dummyStory = DummyData.generateDummyStories()

    @Test
    fun `Get Story List Success`() = mainCoroutineRule.runBlockingTest {

        val expectedStories = MutableLiveData<PagingData<StoryModel>>()
        expectedStories.value = PagingData.from(dummyStory)
        `when`(repository.fetchStories()).thenReturn(expectedStories)

        val listStoryViewModel = ListStoryViewModel(repository)
        val actualStories = listStoryViewModel.fetchStories.getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = listUpdateCallback,
            mainDispatcher = Dispatchers.Main,
            workerDispatcher = Dispatchers.Main
        )
        differ.submitData(actualStories)

        assertNotNull(differ.snapshot())
        assertEquals(dummyStory, differ.snapshot())
        assertEquals(dummyStory.size, differ.snapshot().size)
        assertEquals(dummyStory[0].name, differ.snapshot()[0]?.name)
    }

    companion object {
        val listUpdateCallback = object : ListUpdateCallback {
            override fun onInserted(position: Int, count: Int) {}
            override fun onRemoved(position: Int, count: Int) {}
            override fun onMoved(fromPosition: Int, toPosition: Int) {}
            override fun onChanged(position: Int, count: Int, payload: Any?) {}
        }
    }
}