package com.animex.app.ui.genre

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.animex.app.R
import com.animex.app.databinding.FragmentGenreBinding
import com.animex.app.ui.home.AnimeGridAdapter
import com.animex.app.utils.hide
import com.animex.app.utils.show
import com.animex.app.utils.toast

class GenreFragment : Fragment() {

    private var _binding: FragmentGenreBinding? = null
    private val binding get() = _binding!!
    private val vm: GenreViewModel by viewModels()
    private lateinit var genreListAdapter: GenreListAdapter
    private lateinit var animeAdapter: AnimeGridAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentGenreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        genreListAdapter = GenreListAdapter { genre ->
            vm.selectGenre(genre.slug, genre.name)
        }
        animeAdapter = AnimeGridAdapter { slug ->
            val bundle = Bundle().apply { putString("slug", slug) }
            findNavController().navigate(R.id.animeDetailFragment, bundle)
        }

        binding.rvGenres.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = genreListAdapter
        }
        binding.rvAnime.apply {
            layoutManager = GridLayoutManager(context, 3)
            adapter = animeAdapter
        }

        vm.genres.observe(viewLifecycleOwner) {
            genreListAdapter.submitList(it)
        }
        vm.selectedGenreName.observe(viewLifecycleOwner) {
            binding.tvSelectedGenre.text = it
            binding.rvAnime.show()
            binding.rvGenres.hide()
            binding.btnBackGenre.show()
        }
        vm.animeList.observe(viewLifecycleOwner) {
            animeAdapter.submitList(it)
        }
        vm.loading.observe(viewLifecycleOwner) { loading ->
            if (loading) binding.progressGenre.show() else binding.progressGenre.hide()
        }

        binding.btnBackGenre.setOnClickListener {
            vm.clearSelection()
            binding.rvGenres.show()
            binding.rvAnime.hide()
            binding.btnBackGenre.hide()
            binding.tvSelectedGenre.text = "Genre"
        }

        // Deep link from anime detail
        arguments?.getString("slug")?.let { slug ->
            val name = arguments?.getString("name") ?: slug
            vm.selectGenre(slug, name)
        } ?: vm.loadGenres()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
