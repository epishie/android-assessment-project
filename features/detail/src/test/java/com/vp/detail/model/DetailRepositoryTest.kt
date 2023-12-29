package com.vp.detail.model

import com.vp.database.dao.FavoriteMoviesDao
import com.vp.database.entity.FavoriteMovieEntity
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
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@OptIn(ExperimentalCoroutinesApi::class)
class DetailRepositoryTest {
    @Mock
    private lateinit var favoriteMoviesDao: FavoriteMoviesDao
    private lateinit var mocks: AutoCloseable
    private lateinit var repository: DetailRepository
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        mocks = MockitoAnnotations.openMocks(this)
        repository = DetailRepository(favoriteMoviesDao, testDispatcher)
    }

    @After
    fun tearDown() {
        mocks.close()
    }

    @Test
    fun shouldSaveFavoriteMovie() = runTest {
        // when
        repository.saveFavoriteMovie("1234", "poster1")

        // then
        verify(favoriteMoviesDao).insert(FavoriteMovieEntity("1234", "poster1"))
    }

    @Test
    fun shouldRemoveFavoriteMovie() = runTest {
        // when
        repository.removeFavoriteMovie("1234")

        // then
        verify(favoriteMoviesDao).delete(FavoriteMovieEntity("1234"))
    }

    @Test
    fun shouldGetFavoriteMovieState() = runTest {
        // given
        val flow = MutableSharedFlow<FavoriteMovieEntity?>()
        `when`(favoriteMoviesDao.selectById("1234")).thenReturn(flow)

        // when
        val values = mutableListOf<Boolean>()
        backgroundScope.launch(testDispatcher) {
            repository.getFavoriteMovieState("1234").toList(values)
        }
        flow.emit(null)
        flow.emit(FavoriteMovieEntity("1234", "poster1"))

        // then
        assertThat(values).containsExactly(false, true)
    }
}
