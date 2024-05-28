// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
    id("com.android.library") version "8.3.0" apply false
}

buildscript {
    dependencies {
        classpath("com.google.gms:google-services:4.3.14")
        classpath ("com.android.tools.build:gradle:8.1.3")
        classpath ("org.jetbrains.kotlin:kotlin-gradle-plugin:$1.2.71")
    }
}
