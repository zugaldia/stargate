plugins {
    // The Kotlin DSL plugin provides a convenient way to develop convention plugins.
    // Convention plugins are located in `src/main/kotlin`, with the file extension `.gradle.kts`,
    // and are applied in the project's `build.gradle.kts` files as required.
    `kotlin-dsl`
    alias(libs.plugins.flatpakGradleGenerator)
}


dependencies {
    implementation(libs.kotlinGradlePlugin)
    implementation(libs.detektGradlePlugin)
}

tasks.flatpakGradleGenerator {
    outputFile = file("flatpak-sources.json")
    downloadDirectory = "offline-repository"
    excludeConfigurations = listOf("testCompileClasspath", "testRuntimeClasspath")
}
