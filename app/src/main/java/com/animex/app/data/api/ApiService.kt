package com.animex.app.data.api

import com.animex.app.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // ── STATS ──
    @GET("api/stats")
    suspend fun getStats(): Response<StatsResponse>

    // ── HEARTBEAT ──
    @POST("api/heartbeat")
    suspend fun heartbeat(@Body body: HeartbeatBody): Response<Unit>

    // ── HOME ──
    @GET("api/home")
    suspend fun getHome(): Response<HomeResponse>

    // ── SEARCH ──
    @GET("api/search/{query}")
    suspend fun search(@Path("query") query: String): Response<SearchResponse>

    // ── ANIME DETAIL ──
    @GET("api/anime/{slug}")
    suspend fun getAnime(@Path("slug") slug: String): Response<AnimeDetailResponse>

    // ── EPISODE ──
    @GET("api/episode/{slug}")
    suspend fun getEpisode(@Path("slug") slug: String): Response<EpisodeResponse>

    // ── SERVER ──
    @GET("api/server/{serverId}")
    suspend fun getServer(@Path("serverId") serverId: String): Response<ServerResponse>

    // ── GENRE LIST ──
    @GET("api/genre")
    suspend fun getGenres(): Response<GenreListResponse>

    // ── GENRE DETAIL ──
    @GET("api/genre/{slug}")
    suspend fun getGenreAnime(
        @Path("slug") slug: String,
        @Query("page") page: Int = 1
    ): Response<GenreAnimeResponse>

    // ── SCHEDULE ──
    @GET("api/schedule")
    suspend fun getSchedule(): Response<ScheduleResponse>

    // ── DRACHIN ──
    @GET("api/drachin/home")
    suspend fun getDrachinHome(): Response<DrachinHomeResponse>

    @GET("api/drachin/latest")
    suspend fun getDrachinLatest(@Query("page") page: Int = 1): Response<DrachinListResponse>

    @GET("api/drachin/popular")
    suspend fun getDrachinPopular(@Query("page") page: Int = 1): Response<DrachinListResponse>

    @GET("api/drachin/search/{query}")
    suspend fun searchDrachin(@Path("query") query: String): Response<DrachinListResponse>

    @GET("api/drachin/detail/{slug}")
    suspend fun getDrachinDetail(@Path("slug") slug: String): Response<DrachinDetailResponse>

    // ── UNLIMITED ──
    @GET("api/unlimited")
    suspend fun getUnlimited(): Response<UnlimitedResponse>
}
