plugins {
    // Apply the shared build logic from a convention plugin.
    // The shared code is located in `buildSrc/src/main/kotlin/kotlin-jvm.gradle.kts`.
    id("buildsrc.convention.kotlin-jvm")
    application
    alias(libs.plugins.versions)
}

dependencies {
    implementation(libs.clikt)
    implementation(libs.dbusJavaV6Utils)
}

application {
    mainClass = "com.zugaldia.stargate.generator.GeneratorKt"
}
