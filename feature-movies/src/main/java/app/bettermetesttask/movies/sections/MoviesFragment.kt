package app.bettermetesttask.movies.sections

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import app.bettermetesttask.featurecommon.injection.utils.Injectable
import app.bettermetesttask.featurecommon.injection.viewmodel.SimpleViewModelProviderFactory
import app.bettermetesttask.featurecommon.utils.views.gone
import app.bettermetesttask.featurecommon.utils.views.visible
import app.bettermetesttask.movies.R
import app.bettermetesttask.movies.databinding.MoviesFragmentBinding
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

class MoviesFragment : Fragment(R.layout.movies_fragment), Injectable {

    @Inject
    lateinit var viewModelProvider: Provider<MoviesViewModel>

    @Inject
    lateinit var adapter: MoviesAdapter

    private lateinit var binding: MoviesFragmentBinding

    private val viewModel by viewModels<MoviesViewModel> { SimpleViewModelProviderFactory(viewModelProvider) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = MoviesFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            val llm = LinearLayoutManager(requireContext())
            llm.orientation = LinearLayoutManager.VERTICAL
            rvList.layoutManager = llm
            rvList.adapter = adapter
        }


        adapter.onItemClicked = { movie ->
            viewModel.openMovieDetails(movie)
        }

        adapter.onItemLiked = { movie ->
            viewModel.likeMovie(movie)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.moviesStateFlow.collect {
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

    override fun onResume() {
        super.onResume()

        viewModel.loadMovies()
    }

    private fun renderMoviesState(state: MoviesState) {
        with(binding) {
            when (state) {
                MoviesState.Loading -> {
                    rvList.gone()
                    progressBar.visible()
                }
                is MoviesState.Loaded -> {
                    adapter.submitList(state.movies)
                    progressBar.gone()
                    rvList.visible()
                }
                else -> {
                    // no op
                    progressBar.gone()
                    rvList.gone()
                }
            }
        }
    }
}