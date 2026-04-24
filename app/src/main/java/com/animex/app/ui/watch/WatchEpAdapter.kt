package com.animex.app.ui.watch

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.animex.app.data.model.EpisodeItem
import com.animex.app.databinding.ItemWatchEpBinding

class WatchEpAdapter(
    private val onClick: (EpisodeItem) -> Unit
) : ListAdapter<EpisodeItem, WatchEpAdapter.VH>(DIFF) {

    private var currentSlug = ""

    inner class VH(val b: ItemWatchEpBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(item: EpisodeItem, isCurrent: Boolean) {
            b.tvEpNum.text  = item.episodeNumber.toString()
            b.tvEpName.text = item.episode
            b.root.isSelected = isCurrent
            b.root.setOnClickListener { onClick(item) }
        }
    }

    fun submitList(list: List<EpisodeItem>, current: String) {
        currentSlug = current
        submitList(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(ItemWatchEpBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) =
        holder.bind(getItem(position), getItem(position).slug == currentSlug)

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<EpisodeItem>() {
            override fun areItemsTheSame(a: EpisodeItem, b: EpisodeItem) = a.slug == b.slug
            override fun areContentsTheSame(a: EpisodeItem, b: EpisodeItem) = a == b
        }
    }
}
