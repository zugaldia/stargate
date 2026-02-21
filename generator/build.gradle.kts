plugins {
    // Apply the shared build logic from a convention plugin.
    // The shared code is located in `buildSrc/src/main/kotlin/kotlin-jvm.gradle.kts`.
    id("buildsrc.convention.kotlin-jvm")
    application
}

dependencies {
    implementation(libs.clikt)
    implementation(libs.dbusJavaUtils)
}

application {
    mainClass = "com.zugaldia.stargate.generator.GeneratorKt"
}
