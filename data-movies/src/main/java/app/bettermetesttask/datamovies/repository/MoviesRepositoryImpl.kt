package app.bettermetesttask.datamovies.repository

import app.bettermetesttask.datamovies.repository.stores.MoviesLocalStore
import app.bettermetesttask.datamovies.repository.stores.MoviesMapper
import app.bettermetesttask.datamovies.repository.stores.MoviesRestStore
import app.bettermetesttask.domaincore.utils.Result
import app.bettermetesttask.domainmovies.entries.Movie
import app.bettermetesttask.domainmovies.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MoviesRepositoryImpl @Inject constructor(
    private val localStore: MoviesLocalStore,
    private val mapper: MoviesMapper
) : MoviesRepository {

    private val restStore = MoviesRestStore()

    override suspend fun getMovies(): Flow<Result<List<Movie>>> {
        return flow {
            val localMovies = localStore.getMovies()
            if (localMovies.isNotEmpty()) {
                emit(Result.Success(localMovies.map { mapper.mapFromLocal(it) }))
            }
            val restResult = Result.of { restStore.getMovies() }
            if (restResult is Result.Success) {
                localStore.saveMovies(restResult.data.map { mapper.mapToLocal(it) })
            }
            emit(restResult)
        }
    }

    override suspend fun getMovie(id: Int): Result<Movie> {
        val movie = localStore.getMovie(id) ?: return Result.Error(Exception("Movie not found"))
        val isLiked = isMovieLiked(id)
        return Result.of { mapper.mapFromLocal(movie).copy(liked = isLiked) }
    }

    override fun observeLikedMovieIds(): Flow<List<Int>> {
        return localStore.observeLikedMoviesIds()
    }

    override suspend fun addMovieToFavorites(movieId: Int) {
        localStore.likeMovie(movieId)
    }

    override suspend fun removeMovieFromFavorites(movieId: Int) {
        localStore.dislikeMovie(movieId)
    }

    private suspend fun isMovieLiked(movieId: Int): Boolean {
        return localStore.isMovieLiked(movieId)
    }
}