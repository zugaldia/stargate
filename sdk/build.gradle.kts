import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

plugins {
    // Apply the shared build logic from a convention plugin.
    // The shared code is located in `buildSrc/src/main/kotlin/kotlin-jvm.gradle.kts`.
    id("buildsrc.convention.kotlin-jvm")
    alias(libs.plugins.kotlinPluginSerialization)
    `java-library`
    `maven-publish`
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
    implementation(libs.slf4jApi)

    // DBus-Java library
    api(libs.dbusJavaCore)
    implementation(libs.dbusJavaTransportNativeUnixsocket)

    testImplementation(kotlin("test"))
}

java {
    withSourcesJar()
    withJavadocJar()
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(22))
    }
}

/*
 * Publish to GitHub Registry
 * https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-gradle-registry
 */

val baseVersion: String = "0.1.0"
val timestamp: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
val projectVersion: String = "$baseVersion-$timestamp"

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/zugaldia/stargate")
            credentials {
                username = "zugaldia"
                password = project.findProperty("gpr.key") as String? ?: System.getenv("GITHUB_TOKEN")
            }
        }
    }
    publications {
        register<MavenPublication>("gpr") {
            from(components["java"])

            groupId = "com.zugaldia.stargate"
            artifactId = "stargate"
            version = projectVersion

            pom {
                name.set("Stargate SDK")
                description.set("Kotlin JVM library for XDG Desktop Portals on Linux")
                url.set("https://github.com/zugaldia/stargate")

                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }

                developers {
                    developer {
                        id.set("zugaldia")
                        name.set("Antonio Zugaldia")
                    }
                }

                scm {
                    connection.set("scm:git:git://github.com/zugaldia/stargate.git")
                    developerConnection.set("scm:git:ssh://github.com:zugaldia/stargate.git")
                    url.set("https://github.com/zugaldia/stargate")
                }
            }
        }
    }
}
