package com.vp.list.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.vp.list.model.ListItem
import com.vp.list.model.SearchResponse
import com.vp.list.service.SearchService
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.anyInt
import org.mockito.Mockito.anyString
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import retrofit2.mock.Calls
import java.io.IOException

class ListViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun shouldReturnErrorState() {
        //given
        val searchService: SearchService = mock()
        `when`(searchService.search(anyString(), anyInt())).thenReturn(Calls.failure(IOException()))
        val listViewModel = ListViewModel(searchService)

        //when
        listViewModel.searchMoviesByTitle("title", 1)

        //then
        assertThat(listViewModel.observeMovies().value?.listState).isEqualTo(ListState.ERROR)
    }

    @Test
    fun shouldReturnInProgressState() {
        //given
        val searchService: SearchService = mock()
        `when`(searchService.search(anyString(), anyInt())).thenReturn(Calls.response(mock(SearchResponse::class.java)))
        val listViewModel = ListViewModel(searchService)
        val mockObserver: Observer<SearchResult> = mock()
        listViewModel.observeMovies().observeForever(mockObserver)

        //when
        listViewModel.searchMoviesByTitle("title", 1)

        //then
        verify(mockObserver).onChanged(SearchResult.inProgress())
    }

    @Test
    fun shouldReturnSuccessState() {
        //given
        val search = MutableList<ListItem>(10) { mock() }
        val searchResponse: SearchResponse = mock()
        `when`(searchResponse.search).thenReturn(search)
        `when`(searchResponse.totalResults).thenReturn(search.size)
        val searchService: SearchService = mock()
        `when`(searchService.search(anyString(), anyInt())).thenReturn(Calls.response(searchResponse))
        val listViewModel = ListViewModel(searchService)
        val mockObserver: Observer<SearchResult> = mock()
        listViewModel.observeMovies().observeForever(mockObserver)

        //when
        listViewModel.searchMoviesByTitle("title", 1)

        //then
        verify(mockObserver).onChanged(SearchResult.inProgress())
        verify(mockObserver).onChanged(SearchResult.success(search, 10))
    }
}
