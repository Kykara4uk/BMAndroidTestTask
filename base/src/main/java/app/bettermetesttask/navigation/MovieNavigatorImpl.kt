package app.bettermetesttask.navigation

import androidx.navigation.NavController
import app.bettermetesttask.featurecommon.utils.navigation.executeSafeNavAction
import app.bettermetesttask.movies.sections.MoviesFragmentDirections
import dagger.Lazy
import javax.inject.Inject

class MovieNavigatorImpl @Inject constructor(
    private val navController: Lazy<NavController>,
) : MovieNavigator {

    override fun navigateToMovieDetails(movieId: Int) {
        executeSafeNavAction {
            val action = MoviesFragmentDirections.actionShowMovieDetails(movieId = movieId)
            navController.get().navigate(action)
        }
    }
}