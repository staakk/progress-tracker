import org.jetbrains.kotlin.gradle.tasks.*

buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(libs.android.pluginGradle)
        classpath(libs.kotlin.pluginGradle)
        classpath(libs.hilt.pluginGradle)
        classpath(libs.realm.pluginGradle)
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