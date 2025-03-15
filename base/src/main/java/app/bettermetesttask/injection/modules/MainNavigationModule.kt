package app.bettermetesttask.injection.modules

import androidx.navigation.NavController
import androidx.navigation.Navigation
import app.bettermetesttask.R
import app.bettermetesttask.featurecommon.injection.scopes.ActivityScope
import app.bettermetesttask.navigation.HomeCoordinator
import app.bettermetesttask.navigation.HomeCoordinatorImpl
import app.bettermetesttask.navigation.HomeNavigator
import app.bettermetesttask.navigation.HomeNavigatorImpl
import app.bettermetesttask.navigation.MovieCoordinator
import app.bettermetesttask.navigation.MovieCoordinatorImpl
import app.bettermetesttask.navigation.MovieNavigator
import app.bettermetesttask.navigation.MovieNavigatorImpl
import app.bettermetesttask.sections.home.HomeActivity
import dagger.Module
import dagger.Provides

@Module
class MainNavigationModule {

    @Provides
    @ActivityScope
    fun provideNavController(activity: HomeActivity): NavController =
        Navigation.findNavController(activity, R.id.mainNavigationFragment)

    @Provides
    fun bindNavigator(navigatorImpl: HomeNavigatorImpl): HomeNavigator {
        return navigatorImpl
    }

    @Provides
    fun bindCoordinator(coordinatorImpl: HomeCoordinatorImpl): HomeCoordinator {
        return coordinatorImpl
    }

    @Provides
    fun bindMovieNavigator(navigatorImpl: MovieNavigatorImpl): MovieNavigator {
        return navigatorImpl
    }

    @Provides
    fun bindMovieCoordinator(coordinatorImpl: MovieCoordinatorImpl): MovieCoordinator {
        return coordinatorImpl
    }
}