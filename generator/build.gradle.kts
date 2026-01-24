plugins {
    // Apply the shared build logic from a convention plugin.
    // The shared code is located in `buildSrc/src/main/kotlin/kotlin-jvm.gradle.kts`.
    id("buildsrc.convention.kotlin-jvm")

    // Apply the Application plugin to add support for building an executable JVM application.
    application
}

dependencies {
    // No dependencies on other modules - this is a standalone code generator
    implementation(libs.clikt)
    implementation(libs.dbusJavaUtils)
}

application {
    // Define the Fully Qualified Name for the application main class
    // (Note that Kotlin compiles `Generator.kt` to a class with FQN `com.zugaldia.stargate.generator.GeneratorKt`.)
    mainClass = "com.zugaldia.stargate.generator.GeneratorKt"
}
