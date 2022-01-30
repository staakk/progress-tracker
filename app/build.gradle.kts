plugins {
    id ("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id("kotlin-parcelize")
}

android {
    val sdkVersion: Int by rootProject.extra
    val minSdkVersion: Int by rootProject.extra
    val appVersionCode: Int by rootProject.extra
    val appVersionName: String by rootProject.extra
    compileSdk = sdkVersion

    defaultConfig {
        applicationId = "io.github.staakk.progresstracker"
        minSdk = minSdkVersion
        targetSdk = sdkVersion
        versionCode = appVersionCode
        versionName = appVersionName

        testInstrumentationRunner = "io.github.staakk.progresstracker.HiltTestRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures {
        compose = true
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.get()
    }

    packagingOptions {
        resources.excludes += "META-INF/**"
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}


dependencies {
    coreLibraryDesugaring(libs.tools.desugar)

    implementation(libs.kotlin.stdlib)
    implementation(libs.androidx.core)
    implementation(libs.androidx.appcompat)
    testImplementation(libs.junit)
    androidTestImplementation(libs.junit.androidx)

    implementation(libs.bundles.lifecycle.libs)

    implementation(libs.bundles.room.libs)
    kapt(libs.bundles.room.kapt)
    testImplementation(libs.bundles.room.test)

    implementation(libs.bundles.compose.libs)
    implementation(libs.navigation.compose)
    implementation(libs.activity.compose)
    implementation(libs.constraintlayout.compose)
    androidTestImplementation(libs.bundles.compose.androidTest)

    implementation(libs.bundles.hilt.libs)
    kapt(libs.bundles.hilt.kapt)
    androidTestImplementation(libs.bundles.hilt.androidTest)
    kaptAndroidTest(libs.bundles.hilt.kapt)

    implementation(libs.timber)
    debugImplementation(libs.leakcanary)

    testImplementation(libs.mockk)

    implementation(libs.espresso.idlingResources)
    androidTestImplementation(libs.espresso.core)

    implementation(project(":common"))
    implementation(project(":common-android"))
    implementation(project(":common-ui-compose"))
    implementation(project(":domain"))
    implementation(project(":data"))
    implementation(project(":ui-exercise"))
}

kapt {
    correctErrorTypes = true
}