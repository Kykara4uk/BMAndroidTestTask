package app.bettermetesttask.domainmovies.interactors

import app.bettermetesttask.domainmovies.entries.Movie
import app.bettermetesttask.domainmovies.repository.MoviesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.jupiter.MockitoExtension
import app.bettermetesttask.domaincore.utils.Result

@ExtendWith(MockitoExtension::class)
internal class ObserveMoviesUseCaseTest {

    @Mock
    private lateinit var repository: MoviesRepository

    @Test
    fun testMoviesObserve() = runTest {
        val movies = listOf(
            Movie(id = 1, title = "Movie 1", description = "Desc 1", posterPath = "https://www.themoviedb.org/t/p/w440_and_h660_face/velWPhVMQeQKcxggNEU8YmIo52R.jpg", liked = false),
            Movie(id = 2, title = "Movie 2", description = "Desc 2", posterPath = null, liked = false),
        )
        val likedMovieIds = listOf(1)
        val expectedMovies = listOf(
            Movie(id = 1, title = "Movie 1", description = "Desc 1", posterPath = "https://www.themoviedb.org/t/p/w440_and_h660_face/velWPhVMQeQKcxggNEU8YmIo52R.jpg", liked = true),
            Movie(id = 2, title = "Movie 2", description = "Desc 2", posterPath = null, liked = false),
        )

        `when`(repository.getMovies()).thenReturn(flowOf(Result.Success(movies)))
        `when`(repository.observeLikedMovieIds()).thenReturn(flowOf(likedMovieIds))

        val useCase = ObserveMoviesUseCase(repository)

        val result: Flow<Result<List<Movie>>> = useCase.invoke()

        result.collect { moviesResult ->
            assert(moviesResult is Result.Success)
            assertEquals(expectedMovies, (moviesResult as Result.Success).data)
        }
    }
}