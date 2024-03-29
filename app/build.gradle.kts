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
    namespace = "io.github.staakk.progresstracker"
}


dependencies {
    coreLibraryDesugaring(libs.tools.desugar)

    implementation(libs.kotlin.stdlib)
    implementation(libs.androidx.core)
    implementation(libs.androidx.appcompat)
    testImplementation(libs.junit)
    androidTestImplementation(libs.junit.androidx)

    implementation(libs.bundles.lifecycle.libs)

    implementation(libs.realm.lib)

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

    implementation(libs.android.material.components)

    implementation(project(":common"))
    implementation(project(":common-android"))
    implementation(project(":common-ui-compose"))
    implementation(project(":common-ui-resources"))
    implementation(project(":domain"))
    implementation(project(":data"))

    implementation(project(":ui-exercise"))
    implementation(project(":ui-exercisesearch"))
    implementation(project(":ui-round"))
    implementation(project(":ui-home"))
    implementation(project(":ui-training"))
    implementation(project(":ui-trainings"))
    implementation(project(":ui-set"))
}

kapt {
    correctErrorTypes = true
}