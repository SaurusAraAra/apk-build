package com.animex.app.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.animex.app.data.model.WatchHistory
import com.animex.app.databinding.ItemHistoryCardBinding
import com.animex.app.utils.loadImage

class HistoryAdapter(
    private val onClick: (WatchHistory) -> Unit,
    private val onClear: () -> Unit
) : ListAdapter<WatchHistory, HistoryAdapter.VH>(DIFF) {

    inner class VH(val b: ItemHistoryCardBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(item: WatchHistory) {
            b.imgPoster.loadImage(item.poster)
            b.tvTitle.text = item.title
            b.tvLastEp.text = item.lastEpLabel
            b.root.setOnClickListener { onClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(ItemHistoryCardBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(getItem(position))

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<WatchHistory>() {
            override fun areItemsTheSame(a: WatchHistory, b: WatchHistory) = a.slug == b.slug
            override fun areContentsTheSame(a: WatchHistory, b: WatchHistory) = a == b
        }
    }
}
