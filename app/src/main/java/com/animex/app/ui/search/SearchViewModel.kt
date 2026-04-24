package com.animex.app.ui.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.animex.app.data.model.AnimeCard
import com.animex.app.data.repository.AnimeRepository
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {
    private val repo = AnimeRepository()
    val results = MutableLiveData<List<AnimeCard>>()
    val loading = MutableLiveData<Boolean>()

    fun search(query: String) {
        loading.postValue(true)
        viewModelScope.launch {
            repo.search(query).fold(
                onSuccess = { results.postValue(it.searchResults) },
                onFailure = { results.postValue(emptyList()) }
            )
            loading.postValue(false)
        }
    }
}
