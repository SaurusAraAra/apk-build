package com.animex.app.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.animex.app.R
import com.animex.app.databinding.FragmentSearchBinding
import com.animex.app.ui.home.AnimeGridAdapter
import com.animex.app.utils.hide
import com.animex.app.utils.hideKeyboard
import com.animex.app.utils.show

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val vm: SearchViewModel by viewModels()
    private lateinit var adapter: AnimeGridAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = AnimeGridAdapter { slug ->
            val bundle = Bundle().apply { putString("slug", slug) }
            findNavController().navigate(R.id.animeDetailFragment, bundle)
        }

        binding.rvResults.apply {
            layoutManager = GridLayoutManager(context, 3)
            adapter = this@SearchFragment.adapter
        }

        binding.btnSearch.setOnClickListener {
            val q = binding.etSearch.text.toString().trim()
            if (q.isNotEmpty()) {
                binding.root.hideKeyboard()
                vm.search(q)
            }
        }

        binding.etSearch.setOnEditorActionListener { _, _, _ ->
            binding.btnSearch.performClick()
            false
        }

        vm.results.observe(viewLifecycleOwner) { list ->
            binding.progressSearch.hide()
            binding.tvEmpty.hide()
            if (list.isEmpty()) {
                binding.tvEmpty.show()
            } else {
                adapter.submitList(list)
                binding.rvResults.show()
            }
        }

        vm.loading.observe(viewLifecycleOwner) { loading ->
            if (loading) {
                binding.progressSearch.show()
                binding.rvResults.hide()
            }
        }

        // Pre-fill query if navigated from home
        arguments?.getString("query")?.let { q ->
            binding.etSearch.setText(q)
            vm.search(q)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
