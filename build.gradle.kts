import org.jetbrains.kotlin.gradle.tasks.*

buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.0.0-alpha08")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.28-alpha")
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