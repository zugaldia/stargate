pluginManagement {
    repositories {
        gradlePluginPortal()
        maven { url = uri("../offline-repository") } // Used by Flatpak Builder
    }
}

dependencyResolutionManagement {
    // Use Maven Central and the Gradle Plugin Portal for resolving dependencies in the shared build logic (`buildSrc`) project.
    @Suppress("UnstableApiUsage")
    repositories {
        mavenCentral()
        maven { url = uri("../offline-repository") } // Used by Flatpak Builder
    }

    // Reuse the version catalog from the main build.
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}

rootProject.name = "buildSrc"
