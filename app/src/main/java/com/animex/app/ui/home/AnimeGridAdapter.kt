package com.animex.app.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.animex.app.data.model.AnimeCard
import com.animex.app.databinding.ItemAnimeCardBinding
import com.animex.app.utils.loadImage

class AnimeGridAdapter(
    private val onClick: (String) -> Unit
) : ListAdapter<AnimeCard, AnimeGridAdapter.VH>(DIFF) {

    inner class VH(val b: ItemAnimeCardBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(item: AnimeCard) {
            b.imgPoster.loadImage(item.poster)
            b.tvTitle.text = item.title
            b.tvEpisode.text = item.currentEpisode.ifEmpty { item.episodeCount?.toString() ?: "" }
            b.root.setOnClickListener { onClick(item.slug) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(ItemAnimeCardBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(getItem(position))

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<AnimeCard>() {
            override fun areItemsTheSame(a: AnimeCard, b: AnimeCard) = a.slug == b.slug
            override fun areContentsTheSame(a: AnimeCard, b: AnimeCard) = a == b
        }
    }
}
