package com.animex.app.data.repository

import com.animex.app.data.api.RetrofitClient
import com.animex.app.data.model.*

class AnimeRepository {

    suspend fun getStats() = RetrofitClient.getStatsWithFallback()

    suspend fun heartbeat(state: String, meta: Map<String, String> = emptyMap()) {
        try {
            RetrofitClient.primaryApi.heartbeat(HeartbeatBody(state, meta))
        } catch (_: Exception) {
            try { RetrofitClient.fallbackApi.heartbeat(HeartbeatBody(state, meta)) } catch (_: Exception) {}
        }
    }

    suspend fun getHome() = RetrofitClient.safeCall { getHome() }

    suspend fun search(query: String) = RetrofitClient.safeCall { search(query) }

    suspend fun getAnime(slug: String) = RetrofitClient.safeCall { getAnime(slug) }

    suspend fun getEpisode(slug: String) = RetrofitClient.safeCall { getEpisode(slug) }

    suspend fun getServer(serverId: String) = RetrofitClient.safeCall { getServer(serverId) }

    suspend fun getGenres() = RetrofitClient.safeCall { getGenres() }

    suspend fun getGenreAnime(slug: String, page: Int = 1) = RetrofitClient.safeCall { getGenreAnime(slug, page) }

    suspend fun getSchedule() = RetrofitClient.safeCall { getSchedule() }

    suspend fun getDrachinHome() = RetrofitClient.safeCall { getDrachinHome() }

    suspend fun getDrachinLatest(page: Int = 1) = RetrofitClient.safeCall { getDrachinLatest(page) }

    suspend fun getDrachinPopular(page: Int = 1) = RetrofitClient.safeCall { getDrachinPopular(page) }

    suspend fun searchDrachin(query: String) = RetrofitClient.safeCall { searchDrachin(query) }

    suspend fun getDrachinDetail(slug: String) = RetrofitClient.safeCall { getDrachinDetail(slug) }

    suspend fun getUnlimited() = RetrofitClient.safeCall { getUnlimited() }
}
