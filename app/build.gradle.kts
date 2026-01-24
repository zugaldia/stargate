plugins {
    // Apply the shared build logic from a convention plugin.
    // The shared code is located in `buildSrc/src/main/kotlin/kotlin-jvm.gradle.kts`.
    id("buildsrc.convention.kotlin-jvm")

    // Apply the Application plugin to add support for building an executable JVM application.
    application
}

dependencies {
    // Project "app" depends on project "sdk". (Project paths are separated with ":", so ":sdk" refers to the top-level "sdk" project.)
    implementation(project(":sdk"))

    // SLF4J 2.x provider via Log4j
    runtimeOnly(libs.log4jSlf4j2Impl)
}

application {
    // Define the Fully Qualified Name for the application main class
    // (Note that Kotlin compiles `App.kt` to a class with FQN `com.example.app.AppKt`.)
    mainClass = "com.zugaldia.stargate.app.AppKt"
}
