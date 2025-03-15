package app.bettermetesttask.movie_detail.sections

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import app.bettermetesttask.domainmovies.entries.Movie
import app.bettermetesttask.featurecommon.injection.utils.Injectable
import app.bettermetesttask.featurecommon.injection.viewmodel.SimpleViewModelProviderFactory
import app.bettermetesttask.featurecommon.utils.images.GlideApp
import app.bettermetesttask.featurecommon.utils.views.gone
import app.bettermetesttask.featurecommon.utils.views.visible
import app.bettermetesttask.movies.R
import app.bettermetesttask.movies.databinding.MovieDetailFragmentBinding
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

class MovieDetailFragment : Fragment(R.layout.movie_detail_fragment), Injectable {

    @Inject
    lateinit var viewModelProvider: Provider<MovieDetailViewModel>

    private lateinit var binding: MovieDetailFragmentBinding

    val args: MovieDetailFragmentArgs by navArgs()

    private val viewModel by viewModels<MovieDetailViewModel> { SimpleViewModelProviderFactory(viewModelProvider) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = MovieDetailFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getMovie(args.movieId)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.movieStateFlow.collect {
                    renderMoviesState(it)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.errorStateFlow.collect { error ->
                    error?.let {
                        // show error
                    }
                }
            }
        }
    }

    private fun renderMoviesState(state: MovieDetailState) {
        with(binding) {
            when (state) {
                MovieDetailState.Loading -> {
                    title.gone()
                    description.gone()
                    btnLike.gone()
                    poster.gone()
                    progressBar.visible()
                }
                is MovieDetailState.Loaded -> {
                    renderMovie(state.movie)
                    progressBar.gone()
                    title.visible()
                    description.visible()
                    btnLike.visible()
                    poster.visible()
                }
                else -> {
                    // no op
                    progressBar.gone()
                    title.gone()
                    description.gone()
                    btnLike.gone()
                    poster.gone()
                }
            }
        }
    }

    private fun renderMovie(movie: Movie) {
        with(binding) {
            title.text = movie.title
            description.text = movie.description
            btnLike.setImageDrawable(
                ContextCompat.getDrawable(
                    binding.rootLayout.context,
                    if (movie.liked) {
                        R.drawable.ic_favorite_liked
                    } else {
                        R.drawable.ic_favorite_not_liked
                    }
                )
            )
            GlideApp.with(binding.rootLayout)
                .load(movie.posterPath)
                .into(poster)

            btnLike.setOnClickListener {
                onLikeButtonClicked(movie)
            }
        }
    }

    private fun onLikeButtonClicked(movie: Movie) {
        viewModel.likeMovie(movie)
    }
}