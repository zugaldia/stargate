plugins {
    // Apply the shared build logic from a convention plugin.
    // The shared code is located in `buildSrc/src/main/kotlin/kotlin-jvm.gradle.kts`.
    id("buildsrc.convention.kotlin-jvm")
    alias(libs.plugins.kotlinPluginSerialization)
    `java-library`
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

    // DBus-Java library
    api(libs.dbusJavaCore)
    implementation(libs.dbusJavaTransportNativeUnixsocket)

    // Logging
    implementation(libs.log4jApi)
    runtimeOnly(libs.log4jCore)
    runtimeOnly(libs.log4jSlf4j2Impl)

    testImplementation(kotlin("test"))
}
