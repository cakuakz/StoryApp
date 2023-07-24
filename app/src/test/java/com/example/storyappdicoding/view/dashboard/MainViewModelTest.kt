package com.example.storyappdicoding.view.dashboard

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.ListUpdateCallback
import com.example.storyappdicoding.model.data.repository.AuthRepository
import com.example.storyappdicoding.model.data.repository.StoryPagingSource
import com.example.storyappdicoding.model.data.repository.StoryRepository
import com.example.storyappdicoding.model.data.response.ListStoryItem
import com.example.storyappdicoding.view.adapter.StoryAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest{

    companion object {
        val noopListUpdateCallback = object : ListUpdateCallback {
            override fun onInserted(position: Int, count: Int) {}
            override fun onRemoved(position: Int, count: Int) {}
            override fun onMoved(fromPosition: Int, toPosition: Int) {}
            override fun onChanged(position: Int, count: Int, payload: Any?) {}
        }
    }

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var authRepository : AuthRepository

    @Mock
    private lateinit var storyRepository: StoryRepository

    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    @Test
    fun `getAllStoriesPaging() function should not return null, the actual size is returned, and the first data is returned exactly the same`() =
        runTest {
            val dataDummy = listOf(
                ListStoryItem(
                    "https://source.unsplash.com/random/300x300/?nature",
                    "2021-01-01",
                    "Rafi",
                    "My name is rafi and im testing this story.",
                    20.0,
                    "1",
                    4.5
                ),
                ListStoryItem(
                    "https://source.unsplash.com/random/300x300/?love",
                    "2021-01-04",
                    "Rafa",
                    "My name is rafa and im testing this story.",
                    20.0,
                    "2",
                    4.5
                ),
            )
            val data: PagingData<ListStoryItem> = StoryPagingSourceTestUtil.createSnapshot(dataDummy)
            val expectedStory = MutableLiveData<PagingData<ListStoryItem>>()
            expectedStory.value = data

            `when`(storyRepository.getAllStoriesPaging("token")).thenReturn(expectedStory)
            val mainViewModel = MainViewModel(storyRepository, authRepository)
            val realStory: PagingData<ListStoryItem> = mainViewModel.getAllStoriesPaging("token").getOrAwaitValue()

            val differ = AsyncPagingDataDiffer(
                diffCallback = StoryAdapter.DIFF_CALLBACK,
                updateCallback = noopListUpdateCallback,
                workerDispatcher = Dispatchers.Main,
            )
            differ.submitData(realStory)

            Assert.assertNotNull(differ.snapshot())
            Assert.assertEquals(dataDummy.size, differ.snapshot().size)
            Assert.assertEquals(dataDummy[0], differ.snapshot()[0])
        }

    @Test
    fun `getAllStoriesPaging() make sure that data returned is zero`() = runTest {
        val data: PagingData<ListStoryItem> = PagingData.from(emptyList())
        val expectedStory = MutableLiveData<PagingData<ListStoryItem>>()
        expectedStory.value = data

        `when`(storyRepository.getAllStoriesPaging("token")).thenReturn(expectedStory)
        val mainViewModel = MainViewModel(storyRepository, authRepository)
        val realStory: PagingData<ListStoryItem> = mainViewModel.getAllStoriesPaging("token").getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(realStory)

        Assert.assertEquals(0, differ.snapshot().size)
    }
}