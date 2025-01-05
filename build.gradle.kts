// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.google.android.libraries.mapsplatform.secrets.gradle.plugin) apply false
    alias(libs.plugins.google.gms.google.services) apply false
}
buildscript {
    repositories {
        google() // Ensure this is added
        mavenCentral() // This is typically included by default
    }
    dependencies {
        // Add this line to include the Google services plugin
        classpath ("com.google.gms:google-services:4.3.15")
        classpath ("com.google.gms:google-services:4.4.2")
    }
}