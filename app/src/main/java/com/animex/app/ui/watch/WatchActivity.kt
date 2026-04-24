package com.animex.app.ui.watch

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.recyclerview.widget.LinearLayoutManager
import com.animex.app.data.model.EpisodeDetail
import com.animex.app.data.model.EpisodeItem
import com.animex.app.data.model.WatchHistory
import com.animex.app.data.repository.AnimeRepository
import com.animex.app.databinding.ActivityWatchBinding
import com.animex.app.utils.HistoryManager
import com.animex.app.utils.hide
import com.animex.app.utils.show
import com.animex.app.utils.toast
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class WatchActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWatchBinding
    private val repo = AnimeRepository()
    private var player: ExoPlayer? = null
    private lateinit var epListAdapter: WatchEpAdapter

    private var epSlug    = ""
    private var animeSlug = ""
    private var animeTitle = ""
    private var animePoster = ""
    private var currentEp: EpisodeDetail? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        binding = ActivityWatchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        epSlug    = intent.getStringExtra("ep_slug")    ?: ""
        animeSlug = intent.getStringExtra("anime_slug") ?: ""

        setupFullscreen()
        setupPlayer()
        setupEpList()
        setupButtons()
        loadEpisode(epSlug)
        startHeartbeat()
    }

    private fun setupFullscreen() {
        window.decorView.systemUiVisibility = (
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
            View.SYSTEM_UI_FLAG_FULLSCREEN
        )
    }

    private fun setupPlayer() {
        player = ExoPlayer.Builder(this).build()
        binding.playerView.player = player
        binding.playerView.useController = true
        player?.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                if (state == Player.STATE_READY) {
                    binding.progressBar.hide()
                } else if (state == Player.STATE_BUFFERING) {
                    binding.progressBar.show()
                }
            }
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                // nothing
            }
        })
    }

    private fun setupEpList() {
        epListAdapter = WatchEpAdapter { ep ->
            loadEpisode(ep.slug)
        }
        binding.rvEpList.apply {
            layoutManager = LinearLayoutManager(this@WatchActivity)
            adapter = epListAdapter
        }
    }

    private fun setupButtons() {
        binding.btnBack.setOnClickListener { finish() }
        binding.btnPrev.setOnClickListener {
            currentEp?.prevSlug?.let { loadEpisode(it) }
        }
        binding.btnNext.setOnClickListener {
            currentEp?.nextSlug?.let { loadEpisode(it) }
        }
        binding.tabEpisodes.setOnClickListener { showTab(0) }
        binding.tabDownload.setOnClickListener { showTab(1) }
    }

    private fun showTab(tab: Int) {
        if (tab == 0) {
            binding.rvEpList.show()
            binding.dlContainer.hide()
            binding.tabEpisodes.alpha = 1f
            binding.tabDownload.alpha = 0.5f
        } else {
            binding.rvEpList.hide()
            binding.dlContainer.show()
            binding.tabEpisodes.alpha = 0.5f
            binding.tabDownload.alpha = 1f
        }
    }

    private fun loadEpisode(slug: String) {
        epSlug = slug
        binding.progressBar.show()
        binding.tvEpTitle.text = "Memuat..."
        binding.btnPrev.isEnabled = false
        binding.btnNext.isEnabled = false

        lifecycleScope.launch {
            repo.getEpisode(slug).fold(
                onSuccess = { resp ->
                    val ep = resp.data ?: return@fold
                    currentEp = ep

                    binding.tvEpTitle.text = ep.episode
                    title = ep.episode

                    // Update prev/next
                    binding.btnPrev.isEnabled = ep.hasPrev
                    binding.btnNext.isEnabled = ep.hasNext

                    // Load stream
                    if (ep.streamUrl.isNotEmpty()) {
                        playStream(ep.streamUrl)
                    } else {
                        binding.progressBar.hide()
                        toast("Stream tidak tersedia")
                    }

                    // Ep list
                    val sorted = ep.episodeList.sortedByDescending { it.episodeNumber }
                    epListAdapter.submitList(sorted, slug)

                    // Downloads
                    renderDownloads(ep)

                    // Save history
                    saveHistory(ep)

                    // Scroll to current ep
                    val idx = sorted.indexOfFirst { it.slug == slug }
                    if (idx >= 0) binding.rvEpList.scrollToPosition(idx)
                },
                onFailure = {
                    binding.progressBar.hide()
                    toast("Gagal memuat episode: ${it.message}")
                }
            )
        }
    }

    private fun playStream(url: String) {
        player?.apply {
            stop()
            setMediaItem(MediaItem.fromUri(url))
            prepare()
            playWhenReady = true
        }
    }

    private fun renderDownloads(ep: EpisodeDetail) {
        binding.dlContainer.removeAllViews()
        val dl = ep.downloadUrls
        if (dl == null) {
            binding.dlContainer.addView(
                android.widget.TextView(this).apply {
                    text = "Tidak ada link download"
                    setTextColor(0xFF5a7080.toInt())
                    textSize = 13f
                    setPadding(16, 16, 16, 16)
                }
            )
            return
        }
        listOf("mp4" to dl.mp4, "mkv" to dl.mkv).forEach { (fmt, groups) ->
            groups?.forEach { group ->
                group.urls.forEach { dlUrl ->
                    val btn = android.widget.Button(this).apply {
                        text = "${fmt.uppercase()} ${group.resolution} — ${dlUrl.provider}"
                        setOnClickListener {
                            try {
                                startActivity(
                                    android.content.Intent(android.content.Intent.ACTION_VIEW,
                                        android.net.Uri.parse(dlUrl.url))
                                )
                            } catch (_: Exception) { toast("Tidak bisa membuka link") }
                        }
                    }
                    binding.dlContainer.addView(btn)
                }
            }
        }
    }

    private fun saveHistory(ep: EpisodeDetail) {
        HistoryManager.save(this, WatchHistory(
            slug         = animeSlug,
            title        = animeTitle.ifEmpty { ep.episode },
            poster       = animePoster,
            lastEpSlug   = epSlug,
            lastEpLabel  = ep.episode
        ))
    }

    private fun startHeartbeat() {
        lifecycleScope.launch {
            while (isActive) {
                repo.heartbeat("watching", mapOf("slug" to epSlug))
                delay(15_000)
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        setupFullscreen()
    }

    override fun onPause() {
        super.onPause()
        player?.pause()
    }

    override fun onResume() {
        super.onResume()
        setupFullscreen()
    }

    override fun onDestroy() {
        super.onDestroy()
        player?.release()
        player = null
    }
}
