package com.animex.app.ui.genre

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.animex.app.data.model.AnimeCard
import com.animex.app.data.model.Genre
import com.animex.app.data.repository.AnimeRepository
import com.animex.app.databinding.ItemGenreBinding
import kotlinx.coroutines.launch

// ─── ViewModel ───
class GenreViewModel : ViewModel() {
    private val repo = AnimeRepository()
    val genres = MutableLiveData<List<Genre>>()
    val animeList = MutableLiveData<List<AnimeCard>>()
    val selectedGenreName = MutableLiveData<String>()
    val loading = MutableLiveData<Boolean>()

    fun loadGenres() {
        viewModelScope.launch {
            repo.getGenres().fold(
                onSuccess = { genres.postValue(it.data) },
                onFailure = {}
            )
        }
    }

    fun selectGenre(slug: String, name: String) {
        selectedGenreName.postValue(name)
        loading.postValue(true)
        viewModelScope.launch {
            repo.getGenreAnime(slug, 1).fold(
                onSuccess = { animeList.postValue(it.data?.anime ?: emptyList()) },
                onFailure = { animeList.postValue(emptyList()) }
            )
            loading.postValue(false)
        }
    }

    fun clearSelection() {
        selectedGenreName.postValue("")
    }
}

// ─── Adapter ───
class GenreListAdapter(
    private val onClick: (Genre) -> Unit
) : ListAdapter<Genre, GenreListAdapter.VH>(DIFF) {

    inner class VH(val b: ItemGenreBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(item: Genre) {
            b.tvGenreName.text = item.name
            b.root.setOnClickListener { onClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(ItemGenreBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(getItem(position))

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<Genre>() {
            override fun areItemsTheSame(a: Genre, b: Genre) = a.slug == b.slug
            override fun areContentsTheSame(a: Genre, b: Genre) = a == b
        }
    }
}
