plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id("io.realm.kotlin")
}

android {
    val sdkVersion: Int by rootProject.extra
    val minSdkVersion: Int by rootProject.extra
    compileSdk = sdkVersion

    defaultConfig {
        minSdk = minSdkVersion
        targetSdk = sdkVersion

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro")
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
    implementation(libs.kotlin.coroutinesNative)

    implementation(libs.realm.lib)

    implementation(libs.bundles.hilt.libs)
    kapt(libs.bundles.hilt.kapt)
    androidTestImplementation(libs.bundles.hilt.androidTest)
    kaptAndroidTest(libs.bundles.hilt.kapt)

    implementation(libs.timber)
    implementation(project(":common"))

    androidTestImplementation(libs.kotlin.coroutinesTest)

    androidTestImplementation(libs.androidx.testRunner)
    androidTestImplementation(libs.androidx.testRules)
    androidTestImplementation(libs.junit.androidx)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(project(":common-test"))
}