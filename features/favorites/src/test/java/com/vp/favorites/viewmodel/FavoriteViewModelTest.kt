package com.vp.favorites.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.vp.favorites.model.FavoriteMovie
import com.vp.favorites.model.FavoriteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@OptIn(ExperimentalCoroutinesApi::class)
class FavoriteViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: FavoriteRepository
    private lateinit var mocks: AutoCloseable
    private lateinit var viewModel: FavoriteViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        mocks = MockitoAnnotations.openMocks(this)
    }

    @After

    fun tearDown() {
        mocks.close()
        Dispatchers.resetMain()
    }

    @Test
    fun shouldReturnFavoriteMovies() = runTest {
        // given
        val flow = MutableSharedFlow<List<FavoriteMovie>>()
        `when`(repository.getFavoriteMovies()).thenReturn(flow)
        viewModel = FavoriteViewModel(repository)

        // when
        val observer: Observer<List<FavoriteMovie>> = mock()
        viewModel.favoriteMovies.observeForever(observer)
        flow.emit(emptyList())
        flow.emit(
            listOf(
                FavoriteMovie("1234", "poster1"),
                FavoriteMovie("5678", "poster2")
            )
        )

        // then
        verify(observer).onChanged(emptyList())
        verify(observer).onChanged(
            listOf(
                FavoriteMovie("1234", "poster1"),
                FavoriteMovie("5678", "poster2")
            )
        )
    }
}
