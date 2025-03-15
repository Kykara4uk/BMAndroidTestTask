package app.bettermetesttask.movies.injection

import app.bettermetesttask.featurecommon.injection.scopes.FragmentScope
import app.bettermetesttask.movie_detail.injection.MovieDetailScreenModule
import app.bettermetesttask.movies.sections.MoviesFragment
import app.bettermetesttask.movies.sections.compose.MoviesComposeFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MoviesFragmentBuildersModule {

    @FragmentScope
    @ContributesAndroidInjector(modules = [MovieDetailScreenModule::class])
    abstract fun createMoviesFragmentInjector(): MoviesFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = [MovieDetailScreenModule::class])
    abstract fun createMoviesComposeFragmentInjector(): MoviesComposeFragment
}