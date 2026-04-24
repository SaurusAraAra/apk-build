package com.animex.app.data.api

import android.util.Log
import com.animex.app.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    private const val TAG = "RetrofitClient"

    private val PRIMARY_URL = BuildConfig.API_BASE_PRIMARY    // https://stream.saurus.qzz.io/
    private val FALLBACK_URL = BuildConfig.API_BASE_FALLBACK  // http://lordsaurus.sharoni-official.biz.id:2005/

    private var currentBaseUrl = PRIMARY_URL
    private var useFallback = false

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(12, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        })
        .build()

    private fun buildRetrofit(baseUrl: String): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val primaryApi: ApiService = buildRetrofit(PRIMARY_URL).create(ApiService::class.java)
    val fallbackApi: ApiService = buildRetrofit(FALLBACK_URL).create(ApiService::class.java)

    // Try primary; on failure switch to fallback
    suspend fun <T> safeCall(call: suspend ApiService.() -> Response<T>): Result<T> {
        return withContext(Dispatchers.IO) {
            try {
                val response = primaryApi.call()
                if (response.isSuccessful && response.body() != null) {
                    Log.d(TAG, "Primary OK")
                    useFallback = false
                    Result.success(response.body()!!)
                } else {
                    throw Exception("Primary returned ${response.code()}")
                }
            } catch (e: Exception) {
                Log.w(TAG, "Primary failed: ${e.message}, trying fallback")
                try {
                    val response = fallbackApi.call()
                    if (response.isSuccessful && response.body() != null) {
                        useFallback = true
                        Log.d(TAG, "Fallback OK")
                        Result.success(response.body()!!)
                    } else {
                        Result.failure(Exception("Both APIs failed. Fallback: ${response.code()}"))
                    }
                } catch (e2: Exception) {
                    Log.e(TAG, "Both failed: ${e2.message}")
                    Result.failure(e2)
                }
            }
        }
    }

    // Stats-specific call (for the live counter)
    suspend fun getStatsWithFallback(): com.animex.app.data.model.StatsResponse {
        return withContext(Dispatchers.IO) {
            try {
                val r = primaryApi.getStats()
                if (r.isSuccessful && r.body() != null) r.body()!!
                else throw Exception("primary ${r.code()}")
            } catch (e: Exception) {
                try {
                    val r = fallbackApi.getStats()
                    if (r.isSuccessful && r.body() != null) r.body()!!
                    else com.animex.app.data.model.StatsResponse()
                } catch (e2: Exception) {
                    com.animex.app.data.model.StatsResponse()
                }
            }
        }
    }
}
