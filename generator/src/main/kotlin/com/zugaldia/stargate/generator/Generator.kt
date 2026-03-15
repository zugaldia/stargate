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

class Generator : CliktCommand() {
    override fun help(context: Context) = "Stargate code generator CLI"
    override fun run() = Unit
}

class GenerateJava : CliktCommand(name = "generate-java") {
    override fun help(context: Context) = "Generate Java source files (in the sdk module)"
    override fun run() {
        val xmlDir = File(XML_DIR)
        val xmlFiles = xmlDir.listFiles { file ->
            file.extension == "xml" && file.name.startsWith("org.freedesktop.portal.")
        } ?: emptyArray()
        xmlFiles.forEach { xmlFile ->
            echo("Processing: ${xmlFile.name}")
            introspect(xmlFile)
        }
    }

    private fun introspect(inputFile: File) {
        val disableFilter = true
        val introspectionData: String = inputFile.readText()
        val objectPath: String = OBJECT_PATH
        val busName: String = BUS_NAME
        val packageName: String? = null
        val propertyMethods = true
        val argumentPrefix: String? = null
        val avoidUsingTuple = false
        val generator = InterfaceCodeGenerator(
            disableFilter,
            introspectionData,
            objectPath,
            busName,
            packageName,
            propertyMethods,
            argumentPrefix,
            avoidUsingTuple
        )

        val ignoreDtd = true
        val result: Map<File, String> = generator.analyze(ignoreDtd)
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

fun main(args: Array<String>) = Generator()
    .subcommands(GenerateJava())
    .main(args)
