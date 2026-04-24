package com.animex.app.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.animex.app.data.model.AnimeCard
import com.animex.app.data.model.StatsResponse
import com.animex.app.data.repository.AnimeRepository
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val repo = AnimeRepository()

    val stats    = MutableLiveData<StatsResponse>()
    val ongoing  = MutableLiveData<List<AnimeCard>>()
    val complete = MutableLiveData<List<AnimeCard>>()
    val error    = MutableLiveData<String>()

    fun load() {
        viewModelScope.launch {
            loadStats()
            loadHome()
        }
    }

    fun loadStats() {
        viewModelScope.launch {
            stats.postValue(repo.getStats())
        }
    }

    private fun loadHome() {
        viewModelScope.launch {
            repo.getHome().fold(
                onSuccess = { resp ->
                    ongoing.postValue(resp.data?.ongoingAnime ?: emptyList())
                    complete.postValue(resp.data?.completeAnime ?: emptyList())
                },
                onFailure = { error.postValue(it.message) }
            )
        }
    }
}
