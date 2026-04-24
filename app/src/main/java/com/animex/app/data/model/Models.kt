package com.animex.app.data.model

import com.google.gson.annotations.SerializedName

// ── STATS ──
data class StatsResponse(
    @SerializedName("total_watches") val totalWatches: Int = 0,
    @SerializedName("online") val online: Int = 0,
    @SerializedName("watching") val watching: Int = 0
)

// ── HEARTBEAT ──
data class HeartbeatBody(
    @SerializedName("state") val state: String = "online",
    @SerializedName("meta") val meta: Map<String, String> = emptyMap()
)

// ── ANIME CARD ──
data class AnimeCard(
    @SerializedName("slug") val slug: String = "",
    @SerializedName("title") val title: String = "",
    @SerializedName("poster") val poster: String = "",
    @SerializedName("rating") val rating: String = "",
    @SerializedName("type") val type: String = "",
    @SerializedName("status") val status: String = "",
    @SerializedName("episode_count") val episodeCount: Any? = null,
    @SerializedName("current_episode") val currentEpisode: String = "",
    @SerializedName("newest_release_date") val newestReleaseDate: String = "",
    @SerializedName("genres") val genres: List<Genre> = emptyList()
)

// ── GENRE ──
data class Genre(
    @SerializedName("name") val name: String = "",
    @SerializedName("slug") val slug: String = ""
)

// ── HOME ──
data class HomeResponse(
    @SerializedName("data") val data: HomeData? = null
)

data class HomeData(
    @SerializedName("ongoing_anime") val ongoingAnime: List<AnimeCard> = emptyList(),
    @SerializedName("complete_anime") val completeAnime: List<AnimeCard> = emptyList()
)

// ── SEARCH ──
data class SearchResponse(
    @SerializedName("search_results") val searchResults: List<AnimeCard> = emptyList()
)

// ── ANIME DETAIL ──
data class AnimeDetailResponse(
    @SerializedName("data") val data: AnimeDetail? = null
)

data class AnimeDetail(
    @SerializedName("title") val title: String = "",
    @SerializedName("japanese_title") val japaneseTitle: String = "",
    @SerializedName("poster") val poster: String = "",
    @SerializedName("rating") val rating: String = "",
    @SerializedName("type") val type: String = "",
    @SerializedName("status") val status: String = "",
    @SerializedName("episode_count") val episodeCount: Any? = null,
    @SerializedName("duration") val duration: String = "",
    @SerializedName("synopsis") val synopsis: String = "",
    @SerializedName("genres") val genres: List<Genre> = emptyList(),
    @SerializedName("episode_lists") val episodeLists: List<EpisodeItem> = emptyList(),
    @SerializedName("recommendations") val recommendations: List<AnimeCard> = emptyList(),
    @SerializedName("batch") val batch: BatchInfo? = null
)

data class EpisodeItem(
    @SerializedName("slug") val slug: String = "",
    @SerializedName("episode") val episode: String = "",
    @SerializedName("episode_number") val episodeNumber: Int = 0,
    @SerializedName("release_date") val releaseDate: String = ""
)

data class BatchInfo(
    @SerializedName("otakudesu_url") val otakudesuUrl: String = "",
    @SerializedName("uploaded_at") val uploadedAt: String = ""
)

// ── EPISODE ──
data class EpisodeResponse(
    @SerializedName("data") val data: EpisodeDetail? = null
)

data class EpisodeDetail(
    @SerializedName("stream_url") val streamUrl: String = "",
    @SerializedName("episode") val episode: String = "",
    @SerializedName("download_urls") val downloadUrls: DownloadUrls? = null,
    @SerializedName("has_prev") val hasPrev: Boolean = false,
    @SerializedName("has_next") val hasNext: Boolean = false,
    @SerializedName("prev_slug") val prevSlug: String? = null,
    @SerializedName("next_slug") val nextSlug: String? = null,
    @SerializedName("episode_list") val episodeList: List<EpisodeItem> = emptyList()
)

data class DownloadUrls(
    @SerializedName("mp4") val mp4: List<QualityGroup>? = null,
    @SerializedName("mkv") val mkv: List<QualityGroup>? = null
)

data class QualityGroup(
    @SerializedName("resolution") val resolution: String = "",
    @SerializedName("urls") val urls: List<DownloadUrl> = emptyList()
)

data class DownloadUrl(
    @SerializedName("provider") val provider: String = "",
    @SerializedName("url") val url: String = ""
)

// ── SERVER ──
data class ServerResponse(
    @SerializedName("url") val url: String = ""
)

// ── GENRE LIST ──
data class GenreListResponse(
    @SerializedName("data") val data: List<Genre> = emptyList()
)

// ── GENRE ANIME ──
data class GenreAnimeResponse(
    @SerializedName("data") val data: GenreAnimeData? = null
)

data class GenreAnimeData(
    @SerializedName("anime") val anime: List<AnimeCard> = emptyList(),
    @SerializedName("pagination") val pagination: Pagination? = null
)

data class Pagination(
    @SerializedName("has_previous_page") val hasPreviousPage: Boolean = false,
    @SerializedName("has_next_page") val hasNextPage: Boolean = false,
    @SerializedName("last_visible_page") val lastVisiblePage: Int = 1,
    @SerializedName("current_page") val currentPage: Int = 1
)

// ── SCHEDULE ──
data class ScheduleResponse(
    @SerializedName("data") val data: List<ScheduleDay> = emptyList()
)

data class ScheduleDay(
    @SerializedName("day") val day: String = "",
    @SerializedName("anime_list") val animeList: List<ScheduleAnime> = emptyList()
)

data class ScheduleAnime(
    @SerializedName("slug") val slug: String = "",
    @SerializedName("anime_name") val animeName: String = "",
    @SerializedName("poster") val poster: String = ""
)

// ── DRACHIN ──
data class DrachinHomeResponse(
    @SerializedName("data") val data: DrachinHomeData? = null
)

data class DrachinHomeData(
    @SerializedName("slider") val slider: List<Map<String, Any>> = emptyList(),
    @SerializedName("latest") val latest: List<Map<String, Any>> = emptyList(),
    @SerializedName("popular") val popular: List<Map<String, Any>> = emptyList()
)

data class DrachinListResponse(
    @SerializedName("data") val data: List<Map<String, Any>> = emptyList(),
    @SerializedName("pagination") val pagination: DrachinPagination? = null
)

data class DrachinPagination(
    @SerializedName("current_page") val currentPage: Int = 1,
    @SerializedName("has_next") val hasNext: Boolean = false
)

data class DrachinDetailResponse(
    @SerializedName("data") val data: Map<String, Any> = emptyMap()
)

// ── UNLIMITED ──
data class UnlimitedResponse(
    @SerializedName("data") val data: UnlimitedData? = null
)

data class UnlimitedData(
    @SerializedName("list") val list: List<AlphaGroup> = emptyList()
)

data class AlphaGroup(
    @SerializedName("startWith") val startWith: String = "",
    @SerializedName("animeList") val animeList: List<AlphaAnime> = emptyList()
)

data class AlphaAnime(
    @SerializedName("animeId") val animeId: String = "",
    @SerializedName("title") val title: String = ""
)

// ── WATCH HISTORY (local) ──
data class WatchHistory(
    val slug: String,
    val title: String,
    val poster: String,
    val lastEpSlug: String = "",
    val lastEpLabel: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
