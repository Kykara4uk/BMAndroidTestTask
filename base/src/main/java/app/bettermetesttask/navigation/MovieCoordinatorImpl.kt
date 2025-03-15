package app.bettermetesttask.navigation

import javax.inject.Inject

class MovieCoordinatorImpl @Inject constructor(
    private val navigator: MovieNavigator
) : MovieCoordinator {

    override fun toMovieDetails(id: Int) {
        navigator.navigateToMovieDetails(id)
    }
}