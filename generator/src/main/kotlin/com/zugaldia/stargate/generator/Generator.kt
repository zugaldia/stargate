package com.zugaldia.stargate.generator

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.Context
import com.github.ajalt.clikt.core.main
import com.github.ajalt.clikt.core.subcommands
import org.freedesktop.dbus.utils.generator.InterfaceCodeGenerator
import java.io.File

const val BUS_NAME = "org.freedesktop.portal.Desktop"
const val OBJECT_PATH = "/"
const val XML_DIR = "../vendor/xdg-desktop-portal/data"
const val SDK_GENERATED_DIR = "../sdk/src/main/generated"

val EXCLUDED_PORTALS = listOf<String>()

val STATUS_NOTIFIER_RESOURCES = listOf(
    Triple("org.kde.StatusNotifierWatcher.xml", "org.kde.StatusNotifierWatcher", "/StatusNotifierWatcher"),
    Triple("org.kde.StatusNotifierItem.xml", "org.kde.StatusNotifierItem", "/StatusNotifierItem"),
)

class Generator : CliktCommand() {
    override fun help(context: Context) = "Stargate code generator CLI"
    override fun run() {
        val versionInfo = javaClass.classLoader.getResource("dbus-java.version")?.readText()?.trim()
        if (versionInfo != null) {
            echo("dbus-java: ${versionInfo.replace("\n", ", ")}")
        }
    }
}

// New in dbus-java V6: when true, generates Struct-based return values instead of Tuples.
// We keep this false so the generated code uses Tuples, which are compatible with dbus-java V5.
// See: https://github.com/hypfvieh/dbus-java/blob/master/UPGRADE_TO_6x.md
fun buildGenerator(introspectionData: String, busName: String, objectPath: String): InterfaceCodeGenerator =
    InterfaceCodeGenerator(
        /* disableFilter   */ true,
        introspectionData,
        objectPath,
        busName,
        /* packageName     */ null,
        /* propertyMethods */ true,
        /* argumentPrefix  */ null,
        /* avoidUsingTuple */ false,
    )

class GenerateJava : CliktCommand(name = "generate-java") {
    override fun help(context: Context) = "Generate Java source files (in the sdk module)"
    override fun run() {
        val xmlDir = File(XML_DIR)
        val xmlFiles = xmlDir.listFiles { file ->
            file.extension == "xml" && (
                file.name.startsWith("org.freedesktop.portal.") || // Portals
                    file.name.startsWith("org.freedesktop.host.portal.") // Registry
                )
        } ?: emptyArray()
        xmlFiles.forEach { xmlFile ->
            echo("Processing: ${xmlFile.name}")
            introspect(xmlFile)
        }
    }

    private fun introspect(inputFile: File) {
        val result: Map<File, String> = buildGenerator(
            introspectionData = inputFile.readText(),
            busName = BUS_NAME,
            objectPath = OBJECT_PATH,
        ).analyze(true)
        val outputBaseDir = File(SDK_GENERATED_DIR)
        result.forEach { (file, content) ->
            if (file.path in EXCLUDED_PORTALS) {
                echo("Excluded: ${file.path}")
            } else {
                val outputFile = File(outputBaseDir, file.path)
                outputFile.parentFile.mkdirs()
                outputFile.writeText(content)
                echo("Generated: ${outputFile.path}")
            }
        }
    }
}

class GenerateStatusNotifier : CliktCommand(name = "generate-status-notifier") {
    override fun help(context: Context) = "Generate Java source files for StatusNotifier interfaces (in the sdk module)"
    override fun run() {
        STATUS_NOTIFIER_RESOURCES.forEach { (resourceName, busName, objectPath) ->
            echo("Processing: $resourceName")
            val introspectionData = javaClass.classLoader.getResource(resourceName)?.readText()
                ?: error("Resource not found: $resourceName")
            introspect(introspectionData, busName, objectPath)
        }
    }

    private fun introspect(introspectionData: String, busName: String, objectPath: String) {
        val result: Map<File, String> = buildGenerator(introspectionData, busName, objectPath).analyze(true)
        val outputBaseDir = File(SDK_GENERATED_DIR)
        result.forEach { (file, content) ->
            val outputFile = File(outputBaseDir, file.path)
            outputFile.parentFile.mkdirs()
            outputFile.writeText(content)
            echo("Generated: ${outputFile.path}")
        }
    }
}

fun main(args: Array<String>) = Generator()
    .subcommands(GenerateJava(), GenerateStatusNotifier())
    .main(args)
