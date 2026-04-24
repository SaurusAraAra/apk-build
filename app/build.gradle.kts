plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.animex.app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.animex.app"
        minSdk = 24
        targetSdk = 34
        versionCode = 3
        versionName = "3.0.0"

        buildConfigField("String", "BASE_URL_PRIMARY", "\"https://stream.saurus.qzz.io/\"")
        buildConfigField("String", "BASE_URL_FALLBACK", "\"http://lordsaurus.sharoni-official.biz.id:2005/\"")
        buildConfigField("String", "API_BASE_PRIMARY", "\"https://stream.saurus.qzz.io/\"")
        buildConfigField("String", "API_BASE_FALLBACK", "\"http://lordsaurus.sharoni-official.biz.id:2005/\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui)
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)
    implementation(libs.glide)
    implementation(libs.gson)
    implementation(libs.swiperefresh)
    implementation(libs.media3.exoplayer)
    implementation(libs.media3.ui)
    implementation(libs.media3.exoplayer.hls)
    implementation(libs.coroutines.android)
    implementation(libs.viewpager2)
    implementation(libs.shimmer)
}
