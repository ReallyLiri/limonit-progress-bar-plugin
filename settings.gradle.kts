rootProject.name = "limonit-progress-bar-plugin"

pluginManagement {
	repositories {
		gradlePluginPortal()
		mavenCentral()
	}

	// Apply the Foojay toolchains resolver so Gradle can download JDKs for requested toolchains
	plugins {
		id("org.gradle.toolchains.foojay-resolver") version "0.6.0"
	}
}

