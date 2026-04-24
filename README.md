# AnimeX Android App

Aplikasi anime streaming native Android (bukan WebView) — fullscreen, no ads.

## Stack
- **Language**: Kotlin
- **Player**: ExoPlayer (Media3) — native HLS/MP4 streaming
- **HTTP**: Retrofit2 + OkHttp
- **Image**: Glide
- **Nav**: Navigation Component + BottomNavigationView
- **Architecture**: MVVM (ViewModel + LiveData)

## API Setup

Primary URL  : `https://stream.saurus.qzz.io/`  
Fallback URL : `http://lordsaurus.sharoni-official.biz.id:2005/`

Semua request otomatis fallback ke URL kedua kalau primary gagal/timeout.

Endpoints yang dipakai:
- `GET /api/stats` → total_watches, online, watching (live poll 10 detik)
- `POST /api/heartbeat` → kirim state watching/online tiap 15 detik
- `GET /api/home` → ongoing + complete anime
- `GET /api/search/:query`
- `GET /api/anime/:slug`
- `GET /api/episode/:slug`
- `GET /api/genre`, `/api/genre/:slug`
- `GET /api/schedule`
- `GET /api/drachin/*`

## Cara Build APK

### Syarat
- Android Studio Hedgehog (2023.1.1) atau lebih baru
- JDK 17
- Android SDK 34

### Langkah
1. Buka folder `AnimeX-Android` di Android Studio
2. Tunggu Gradle sync selesai
3. **Build → Make Project**
4. **Build → Generate Signed Bundle/APK → APK**
5. Pilih keystore (buat baru kalau belum ada)
6. APK output ada di `app/release/app-release.apk`

### Debug APK (tanpa signing)
```
./gradlew assembleDebug
```
Output: `app/build/outputs/apk/debug/app-debug.apk`

## Fitur
- ✅ Home: Ongoing + Complete anime grid
- ✅ Live stats counter (total tonton / online / menonton)
- ✅ Riwayat tonton lokal (30 episode terakhir)
- ✅ Search anime
- ✅ Detail anime + list episode
- ✅ Player fullscreen native ExoPlayer (HLS + MP4)
- ✅ Prev/Next episode in player
- ✅ Download link di player
- ✅ Genre browser
- ✅ Jadwal tayang
- ✅ Dark theme (cyan #00D4FF + #FF3C6E accent)
- ✅ No ads, no webview
