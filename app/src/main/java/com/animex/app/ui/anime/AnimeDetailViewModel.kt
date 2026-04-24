package com.animex.app.ui.anime

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.animex.app.data.model.AnimeDetail
import com.animex.app.data.repository.AnimeRepository
import kotlinx.coroutines.launch

class AnimeDetailViewModel : ViewModel() {
    private val repo = AnimeRepository()
    val detail = MutableLiveData<AnimeDetail>()
    val error  = MutableLiveData<String>()

    fun load(slug: String) {
        viewModelScope.launch {
            repo.getAnime(slug).fold(
                onSuccess = { it.data?.let { d -> detail.postValue(d) } },
                onFailure = { error.postValue(it.message) }
            )
        }
    }
}
