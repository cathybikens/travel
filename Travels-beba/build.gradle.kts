// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
}

buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        // Use the latest stable version of the Gradle plugin
        classpath ("com.android.tools.build:gradle:8.0.2") // Update to the latest version
        classpath ("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.0") // Update to the latest version
        // Other dependencies can be added here
    }
}
