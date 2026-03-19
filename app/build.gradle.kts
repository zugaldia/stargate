plugins {
    // Apply the shared build logic from a convention plugin.
    // The shared code is located in `buildSrc/src/main/kotlin/kotlin-jvm.gradle.kts`.
    id("buildsrc.convention.kotlin-jvm")
    alias(libs.plugins.flatpakGradleGenerator)
    alias(libs.plugins.shadow)
    alias(libs.plugins.versions)
    application
}

dependencies {
    implementation(project(":sdk"))
    implementation(libs.kotlinxCoroutines)
    implementation(libs.log4jCore)
    implementation(libs.log4jSlf4j2Impl)

    // GTK bindings for Java
    implementation(libs.javaGiGtk)
    implementation(libs.javaGiAdw)
}

application {
    mainClass = "com.zugaldia.stargate.app.AppKt"

    // See: https://java-gi.org/usage/#linux
    applicationDefaultJvmArgs = listOf("--enable-native-access=ALL-UNNAMED")
}

tasks.shadowJar {
    archiveFileName.set("stargate.jar")
}

tasks.flatpakGradleGenerator {
    outputFile = file("flatpak-sources.json")
    downloadDirectory = "offline-repository"
    excludeConfigurations = listOf("testCompileClasspath", "testRuntimeClasspath")
}
