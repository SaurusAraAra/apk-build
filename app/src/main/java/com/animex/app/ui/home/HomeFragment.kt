package com.animex.app.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.animex.app.R
import com.animex.app.data.model.AnimeCard
import com.animex.app.data.model.WatchHistory
import com.animex.app.databinding.FragmentHomeBinding
import com.animex.app.utils.HistoryManager
import com.animex.app.utils.hide
import com.animex.app.utils.show
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val vm: HomeViewModel by viewModels()

    private lateinit var ongoingAdapter: AnimeGridAdapter
    private lateinit var completeAdapter: AnimeGridAdapter
    private lateinit var historyAdapter: HistoryAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclers()
        setupObservers()
        setupSearch()
        vm.load()
        startStatsPoll()
    }

    private fun setupRecyclers() {
        ongoingAdapter = AnimeGridAdapter { navigateToAnime(it) }
        completeAdapter = AnimeGridAdapter { navigateToAnime(it) }
        historyAdapter = HistoryAdapter(
            onClick = { navigateToAnime(it.slug) },
            onClear = {
                HistoryManager.clear(requireContext())
                loadHistory()
            }
        )

        binding.rvOngoing.apply {
            layoutManager = GridLayoutManager(context, 3)
            adapter = ongoingAdapter
            isNestedScrollingEnabled = false
        }
        binding.rvComplete.apply {
            layoutManager = GridLayoutManager(context, 3)
            adapter = completeAdapter
            isNestedScrollingEnabled = false
        }
        binding.rvHistory.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = historyAdapter
        }
    }

    private fun loadHistory() {
        val hist = HistoryManager.getAll(requireContext())
        if (hist.isEmpty()) {
            binding.histSection.hide()
        } else {
            binding.histSection.show()
            historyAdapter.submitList(hist)
        }
    }

    private fun setupObservers() {
        vm.stats.observe(viewLifecycleOwner) { stats ->
            binding.statWatches.text  = stats.totalWatches.toString()
            binding.statOnline.text   = stats.online.toString()
            binding.statWatching.text = stats.watching.toString()
        }
        vm.ongoing.observe(viewLifecycleOwner) { list ->
            ongoingAdapter.submitList(list)
            binding.ongoingShimmer.hide()
            binding.rvOngoing.show()
        }
        vm.complete.observe(viewLifecycleOwner) { list ->
            completeAdapter.submitList(list)
            binding.completeShimmer.hide()
            binding.rvComplete.show()
        }
        vm.error.observe(viewLifecycleOwner) { msg ->
            binding.ongoingShimmer.hide()
            binding.completeShimmer.hide()
        }
    }

    private fun setupSearch() {
        binding.searchBar.setOnEditorActionListener { _, _, _ ->
            val q = binding.searchBar.text.toString().trim()
            if (q.isNotEmpty()) {
                val bundle = Bundle().apply { putString("query", q) }
                findNavController().navigate(R.id.searchFragment, bundle)
            }
            false
        }
    }

    private fun startStatsPoll() {
        lifecycleScope.launch {
            while (true) {
                vm.loadStats()
                kotlinx.coroutines.delay(10_000)
            }
        }
    }

    private fun navigateToAnime(slug: String) {
        val bundle = Bundle().apply { putString("slug", slug) }
        findNavController().navigate(R.id.animeDetailFragment, bundle)
    }

    override fun onResume() {
        super.onResume()
        loadHistory()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
