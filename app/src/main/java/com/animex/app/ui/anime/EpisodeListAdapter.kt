package com.animex.app.ui.anime

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.animex.app.data.model.EpisodeItem
import com.animex.app.databinding.ItemEpisodeBinding

class EpisodeListAdapter(
    private val onClick: (EpisodeItem) -> Unit
) : ListAdapter<EpisodeItem, EpisodeListAdapter.VH>(DIFF) {

    inner class VH(val b: ItemEpisodeBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(item: EpisodeItem) {
            b.tvEpNum.text    = item.episodeNumber.toString()
            b.tvEpName.text   = item.episode
            b.tvEpDate.text   = item.releaseDate
            b.root.setOnClickListener { onClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(ItemEpisodeBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(getItem(position))

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<EpisodeItem>() {
            override fun areItemsTheSame(a: EpisodeItem, b: EpisodeItem) = a.slug == b.slug
            override fun areContentsTheSame(a: EpisodeItem, b: EpisodeItem) = a == b
        }
    }
}
