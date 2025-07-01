// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    id("com.google.dagger.hilt.android") version "2.56.2" apply false
}
/*
buildscript {
    //ext.kotlin_version = ("1.7.20")
    //ext.hilt_version = ("2.40.1")
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
    dependencies {
        classpath ("com.android.tools.build:gradle:8.2.2")
        classpath ("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.0")
        classpath ("com.google.dagger:hilt-android-gradle-plugin:2.48")
        //classpath("com.google.devtools.ksp:com.google.devtools.ksp.gradle.plugin:1.8.0-1.0.8")
    }
}*/
