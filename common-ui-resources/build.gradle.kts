plugins {
    id("com.android.library")
}

android {
    val sdkVersion: Int by rootProject.extra
    val minSdkVersion: Int by rootProject.extra
    compileSdk = sdkVersion

    defaultConfig {
        minSdk = minSdkVersion
        targetSdk = sdkVersion
    }
    namespace = "io.github.staakk.progresstracker.common.ui.resources"
}