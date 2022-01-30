import org.jetbrains.kotlin.gradle.tasks.*

buildscript {
    with (project) {
        extra["minSdk"] = 21
        extra["compileSdk"] = 31
        extra["targetSdk"] = 31
    }

    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.0.4")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.10")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.38.1")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven("https://kotlin.bintray.com/kotlinx/")
    }
    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xopt-in=kotlin.RequiresOptIn")
        }
    }
}

tasks.register("clean").configure {
    delete("build")
}

extra["sdkVersion"] = 31
extra["minSdkVersion"] = 21

extra["appVersionCode"] = 1
extra["appVersionName"] = "1.0"