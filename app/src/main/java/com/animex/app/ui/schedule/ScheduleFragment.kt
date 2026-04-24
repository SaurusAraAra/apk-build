package com.animex.app.ui.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.animex.app.R
import com.animex.app.data.model.ScheduleDay
import com.animex.app.data.repository.AnimeRepository
import com.animex.app.databinding.FragmentScheduleBinding
import com.animex.app.utils.hide
import com.animex.app.utils.show
import kotlinx.coroutines.launch

class ScheduleViewModel : ViewModel() {
    private val repo = AnimeRepository()
    val schedule = MutableLiveData<List<ScheduleDay>>()
    val loading  = MutableLiveData<Boolean>()

    fun load() {
        loading.postValue(true)
        viewModelScope.launch {
            repo.getSchedule().fold(
                onSuccess = { schedule.postValue(it.data) },
                onFailure = { schedule.postValue(emptyList()) }
            )
            loading.postValue(false)
        }
    }
}

class ScheduleFragment : Fragment() {

    private var _binding: FragmentScheduleBinding? = null
    private val binding get() = _binding!!
    private val vm: ScheduleViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentScheduleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vm.loading.observe(viewLifecycleOwner) { loading ->
            if (loading) binding.progressSchedule.show() else binding.progressSchedule.hide()
        }

        vm.schedule.observe(viewLifecycleOwner) { days ->
            binding.scheduleContainer.removeAllViews()
            days.forEach { day ->
                // Day header
                val header = TextView(requireContext()).apply {
                    text = day.day
                    textSize = 14f
                    setTypeface(null, android.graphics.Typeface.BOLD)
                    setTextColor(0xFF00D4FF.toInt())
                    setPadding(0, 24, 0, 8)
                }
                binding.scheduleContainer.addView(header)

                day.animeList.forEach { anime ->
                    val row = TextView(requireContext()).apply {
                        text = "• ${anime.animeName}"
                        textSize = 13f
                        setTextColor(0xFFD0E8F0.toInt())
                        setPadding(16, 6, 0, 6)
                        setOnClickListener {
                            val bundle = Bundle().apply { putString("slug", anime.slug) }
                            findNavController().navigate(R.id.animeDetailFragment, bundle)
                        }
                    }
                    binding.scheduleContainer.addView(row)
                }
            }
        }

        vm.load()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
