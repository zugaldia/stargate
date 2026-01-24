plugins {
    // Apply the shared build logic from a convention plugin.
    // The shared code is located in `buildSrc/src/main/kotlin/kotlin-jvm.gradle.kts`.
    id("buildsrc.convention.kotlin-jvm")
    application
}

dependencies {
    implementation(project(":sdk"))

    // GTK bindings for Java
    implementation(libs.javaGiAdw)
    implementation(libs.javaGiGtk)

    // SLF4J 2.x provider via Log4j
    runtimeOnly(libs.log4jSlf4j2Impl)
}

application {
    mainClass = "com.zugaldia.stargate.app.AppKt"
    applicationDefaultJvmArgs = listOf("--enable-native-access=ALL-UNNAMED")
}
