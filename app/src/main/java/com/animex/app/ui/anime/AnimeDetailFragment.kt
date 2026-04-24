package com.animex.app.ui.anime

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.animex.app.R
import com.animex.app.data.model.EpisodeItem
import com.animex.app.databinding.FragmentAnimeDetailBinding
import com.animex.app.ui.watch.WatchActivity
import com.animex.app.utils.hide
import com.animex.app.utils.loadImage
import com.animex.app.utils.show
import com.animex.app.utils.toast
import com.google.android.material.chip.Chip

class AnimeDetailFragment : Fragment() {

    private var _binding: FragmentAnimeDetailBinding? = null
    private val binding get() = _binding!!
    private val vm: AnimeDetailViewModel by viewModels()
    private lateinit var epAdapter: EpisodeListAdapter
    private var currentSlug = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAnimeDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currentSlug = arguments?.getString("slug") ?: ""

        binding.btnBack.setOnClickListener { findNavController().popBackStack() }

        epAdapter = EpisodeListAdapter { ep -> openWatch(ep.slug) }
        binding.rvEpisodes.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = epAdapter
            isNestedScrollingEnabled = false
        }

        vm.detail.observe(viewLifecycleOwner) { detail ->
            binding.shimmerDetail.hide()
            binding.scrollContent.show()

            binding.imgPoster.loadImage(detail.poster)
            binding.tvTitle.text = detail.title
            binding.tvJpTitle.text = detail.japaneseTitle
            binding.tvRating.text = detail.rating
            binding.tvType.text = detail.type
            binding.tvStatus.text = detail.status
            binding.tvEpCount.text = detail.episodeCount?.toString() ?: "-"
            binding.tvDuration.text = detail.duration
            binding.tvSynopsis.text = detail.synopsis

            binding.chipGenres.removeAllViews()
            detail.genres.forEach { genre ->
                val chip = Chip(requireContext()).apply {
                    text = genre.name
                    setOnClickListener {
                        val bundle = Bundle().apply {
                            putString("slug", genre.slug)
                            putString("name", genre.name)
                        }
                        findNavController().navigate(R.id.genreFragment, bundle)
                    }
                }
                binding.chipGenres.addView(chip)
            }

            val episodes = detail.episodeLists.sortedByDescending { it.episodeNumber }
            epAdapter.submitList(episodes)

            if (detail.batch != null) {
                binding.btnBatch.show()
                binding.btnBatch.setOnClickListener {
                    requireContext().toast("Batch download — buka di browser")
                }
            } else {
                binding.btnBatch.hide()
            }
        }

        vm.error.observe(viewLifecycleOwner) {
            binding.shimmerDetail.hide()
            requireContext().toast("Gagal memuat: $it")
        }

        vm.load(currentSlug)
    }

    private fun openWatch(epSlug: String) {
        Intent(requireContext(), WatchActivity::class.java).also {
            it.putExtra("ep_slug", epSlug)
            it.putExtra("anime_slug", currentSlug)
            startActivity(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
