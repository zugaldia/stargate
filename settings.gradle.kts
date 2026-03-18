pluginManagement {
    repositories {
        gradlePluginPortal()
        maven { url = uri("offline-repository") } // Used by Flatpak Builder
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        maven { url = uri("offline-repository") } // Used by Flatpak Builder
        maven {
            url = uri("https://central.sonatype.com/repository/maven-snapshots/")
        }
    }
}

include(":app")
include(":sdk")
include(":generator")

rootProject.name = "stargate"
