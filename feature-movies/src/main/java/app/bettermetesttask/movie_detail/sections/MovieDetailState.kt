package app.bettermetesttask.movie_detail.sections

import app.bettermetesttask.domainmovies.entries.Movie

sealed class MovieDetailState {

    object Initial : MovieDetailState()

    object Loading : MovieDetailState()

    data class Loaded(val movie: Movie) : MovieDetailState()
}