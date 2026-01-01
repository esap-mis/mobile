// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    val kotlinVersion = "2.2.20"
    id("com.android.application") version "8.12.3" apply false
    id("org.jetbrains.kotlin.plugin.compose") version kotlinVersion apply false
    id("org.jetbrains.kotlin.android") version kotlinVersion apply false
    id("com.google.gms.google-services") version "4.4.0" apply false
    id("com.google.devtools.ksp") version "2.3.4"
}

buildscript {
    dependencies {
        classpath("com.google.gms:google-services:4.4.0")
    }
}