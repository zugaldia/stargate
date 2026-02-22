// The code in this file is a convention plugin - a Gradle mechanism for sharing reusable build logic.
// `buildSrc` is a Gradle-recognized directory, and every plugin there will be available in the rest of the build.
package buildsrc.convention

import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    // Apply the Kotlin JVM plugin to add support for Kotlin in JVM projects.
    kotlin("jvm")
    // Apply the Detekt plugin for static code analysis.
    id("dev.detekt")
}

kotlin {
    // Java-GI requires JDK 22+ for the Panama Foreign Function & Memory API.
    jvmToolchain(22)
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()

    // Log information about all test results, not only the failed ones.
    testLogging {
        events(
            TestLogEvent.FAILED,
            TestLogEvent.PASSED,
            TestLogEvent.SKIPPED
        )
    }
}
