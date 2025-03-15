package app.bettermetesttask.movie_detail.injection

import app.bettermetesttask.featurecommon.injection.scopes.FragmentScope
import app.bettermetesttask.movie_detail.sections.MovieDetailFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MovieDetailFragmentBuildersModule {

    @FragmentScope
    @ContributesAndroidInjector(modules = [MovieDetailScreenModule::class])
    abstract fun createMovieDetailFragmentInjector(): MovieDetailFragment

}