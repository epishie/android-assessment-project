package com.vp.favorites.model

import com.vp.database.dao.FavoriteMoviesDao
import com.vp.database.entity.FavoriteMovieEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@OptIn(ExperimentalCoroutinesApi::class)
class FavoriteRepositoryTest {
    @Mock
    private lateinit var favoriteMoviesDao: FavoriteMoviesDao
    private lateinit var mocks: AutoCloseable
    private lateinit var repository: FavoriteRepository
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers
        mocks = MockitoAnnotations.openMocks(this)
        repository = FavoriteRepository(favoriteMoviesDao, testDispatcher)
    }

    @After
    fun tearDown() {
        mocks.close()
    }

    @Test
    fun shouldReturnEmpty() = runTest {
        // given
        val flow = MutableSharedFlow<List<FavoriteMovieEntity>>()
        `when`(favoriteMoviesDao.selectAll()).thenReturn(flow)

        // when
        val values = mutableListOf<List<FavoriteMovie>>()
        backgroundScope.launch(testDispatcher) {
            repository.getFavoriteMovies().toList(values)
        }
        flow.emit(emptyList())

        // then
        assertThat(values).containsExactly(emptyList())
    }

    @Test
    fun shouldReturnMappedList() = runTest {
        // given
        val flow = MutableSharedFlow<List<FavoriteMovieEntity>>()
        `when`(favoriteMoviesDao.selectAll()).thenReturn(flow)

        // when
        val values = mutableListOf<List<FavoriteMovie>>()
        backgroundScope.launch(testDispatcher) {
            repository.getFavoriteMovies().toList(values)
        }
        flow.emit(
            listOf(
                FavoriteMovieEntity("1234", "poster1"),
                FavoriteMovieEntity("5678", "poster2")
            )
        )

        // then
        assertThat(values).containsExactly(
            listOf(
                FavoriteMovie("1234", "poster1"),
                FavoriteMovie("5678", "poster2")
            )
        )
    }

    @Test
    fun shouldReturnUpdates() = runTest {
        // given
        val flow = MutableSharedFlow<List<FavoriteMovieEntity>>()
        `when`(favoriteMoviesDao.selectAll()).thenReturn(flow)

        // when
        val values = mutableListOf<List<FavoriteMovie>>()
        backgroundScope.launch(testDispatcher) {
            repository.getFavoriteMovies().toList(values)
        }
        flow.emit(
            listOf(
                FavoriteMovieEntity("1234", "poster1"),
                FavoriteMovieEntity("5678", "poster2")
            )
        )
        flow.emit(emptyList())

        // then
        assertThat(values).containsExactly(
            listOf(
                FavoriteMovie("1234", "poster1"),
                FavoriteMovie("5678", "poster2")
            ),
            emptyList()
        )
    }
}
