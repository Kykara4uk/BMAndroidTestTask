package app.bettermetesttask.domainmovies.interactors

import app.bettermetesttask.domaincore.utils.Result
import app.bettermetesttask.domainmovies.entries.Movie
import app.bettermetesttask.domainmovies.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ObserveMoviesUseCase @Inject constructor(
    private val repository: MoviesRepository
) {

    suspend operator fun invoke(): Flow<Result<List<Movie>>> {
        //this logic looks wrong because we have liked property in Movie entity,
        //but store liked movies in separate table.
        //I understand that it is because of stub remote data source that do not return likes,
        //but in real app it should be refactored
        //to remove additional table for liked movies
        return repository.getMovies()
            .combine(repository.observeLikedMovieIds()) { moviesResult, likedMoviesIds ->
            when (moviesResult) {
                is Result.Success -> {
                    val movies = moviesResult.data.map {
                        if (likedMoviesIds.contains(it.id)) {
                            it.copy(liked = true)
                        } else {
                            it
                        }
                    }
                    Result.Success(movies)
                }
                is Result.Error -> {
                    moviesResult
                }
            }
        }
    }
}