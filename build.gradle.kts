// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.2.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.10" apply false
    id("com.google.gms.google-services") version "4.4.0" apply false

    id("com.google.dagger.hilt.android") version "2.48.1" apply false
    id("androidx.navigation.safeargs.kotlin") version "2.6.0" apply false

}

buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        val nav_version = "2.7.5"
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$nav_version")

        classpath("com.android.tools.build:gradle:8.1.4")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.0")
    }
}