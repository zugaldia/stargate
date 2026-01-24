plugins {
    // Apply the shared build logic from a convention plugin.
    // The shared code is located in `buildSrc/src/main/kotlin/kotlin-jvm.gradle.kts`.
    id("buildsrc.convention.kotlin-jvm")
    // Apply the Java Library plugin for proper dependency management (api vs implementation).
    `java-library`
    // Apply Kotlin Serialization plugin from `gradle/libs.versions.toml`.
    alias(libs.plugins.kotlinPluginSerialization)
}

sourceSets {
    main {
        java {
            srcDir("src/main/generated")
        }
    }
}

dependencies {
    // Apply the kotlinx bundle of dependencies from the version catalog (`gradle/libs.versions.toml`).
    implementation(libs.bundles.kotlinxEcosystem)
    implementation(libs.dbusJavaCore)
    implementation(libs.dbusJavaTransportNativeUnixsocket)
    implementation(libs.log4jApi)
    implementation(libs.log4jCore)
    implementation(libs.log4jSlf4jImpl)
    testImplementation(kotlin("test"))
}