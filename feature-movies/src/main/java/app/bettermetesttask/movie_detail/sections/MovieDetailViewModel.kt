package app.bettermetesttask.movie_detail.sections

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.bettermetesttask.domaincore.utils.Result
import app.bettermetesttask.domaincore.utils.coroutines.AppDispatchers
import app.bettermetesttask.domainmovies.entries.Movie
import app.bettermetesttask.domainmovies.interactors.AddMovieToFavoritesUseCase
import app.bettermetesttask.domainmovies.interactors.GetMovieUseCase
import app.bettermetesttask.domainmovies.interactors.RemoveMovieFromFavoritesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class MovieDetailViewModel @Inject constructor(
    private val getMovieUseCase: GetMovieUseCase,
    private val likeMovieUseCase: AddMovieToFavoritesUseCase,
    private val dislikeMovieUseCase: RemoveMovieFromFavoritesUseCase,
) : ViewModel() {

    private val movieMutableFlow: MutableStateFlow<MovieDetailState> = MutableStateFlow(MovieDetailState.Initial)

    val movieStateFlow: StateFlow<MovieDetailState>
        get() = movieMutableFlow.asStateFlow()

    private val errorFlow: MutableStateFlow<String?> = MutableStateFlow(null)
    val errorStateFlow: StateFlow<String?>
        get() = errorFlow.asStateFlow()

    fun getMovie(id: Int) {
        viewModelScope.launch(AppDispatchers.io()) {
            val result = getMovieUseCase.invoke(id)
            when (result) {
                is Result.Success -> {
                    movieMutableFlow.value = MovieDetailState.Loaded(result.data)
                }
                is Result.Error -> {
                    errorFlow.value = result.error.localizedMessage
                }
            }
        }
    }

    fun likeMovie(movie: Movie) {
        viewModelScope.launch(AppDispatchers.io()) {
            if (!movie.liked) {
                likeMovieUseCase(movie.id)
            } else {
                dislikeMovieUseCase(movie.id)
            }
            getMovie(movie.id)
        }
    }
}