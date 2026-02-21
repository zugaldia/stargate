plugins {
    // Apply the shared build logic from a convention plugin.
    // The shared code is located in `buildSrc/src/main/kotlin/kotlin-jvm.gradle.kts`.
    id("buildsrc.convention.kotlin-jvm")
    `java-library`
    alias(libs.plugins.kotlinPluginSerialization)
    alias(libs.plugins.mavenPublish)
}

sourceSets {
    main {
        java {
            srcDir("src/main/generated")
        }
    }
}

dependencies {
    implementation(libs.bundles.kotlinxEcosystem)
    implementation(libs.slf4jApi)

    // DBus-Java library
    api(libs.dbusJavaCore)
    implementation(libs.dbusJavaTransportNativeUnixsocket)

    testImplementation(kotlin("test"))
}

val releaseVersion: String? = project.findProperty("releaseVersion") as String?
val baseVersion: String = releaseVersion ?: "0.2.0"
val isSnapshot: Boolean = releaseVersion == null
val projectVersion: String = if (isSnapshot) "$baseVersion-SNAPSHOT" else baseVersion

mavenPublishing {
    publishToMavenCentral()
    signAllPublications()
    coordinates("com.github.zugaldia", "stargate", projectVersion)

    pom {
        name.set("Stargate SDK")
        description.set("Kotlin JVM library for XDG Desktop Portals on Linux")
        inceptionYear.set("2026")
        url.set("https://github.com/zugaldia/stargate")
        licenses {
            license {
                name.set("MIT License")
                url.set("https://opensource.org/licenses/MIT")
                distribution.set("https://opensource.org/licenses/MIT")
            }
        }
        developers {
            developer {
                id.set("zugaldia")
                name.set("Antonio Zugaldia")
                url.set("https://github.com/zugaldia")
            }
        }
        scm {
            url.set("https://github.com/zugaldia/stargate")
            connection.set("scm:git:git://github.com/zugaldia/stargate.git")
            developerConnection.set("scm:git:ssh://github.com:zugaldia/stargate.git")
        }
    }
}
