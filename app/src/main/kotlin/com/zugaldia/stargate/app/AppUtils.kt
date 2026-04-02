package com.zugaldia.stargate.app

import org.slf4j.LoggerFactory
import org.gnome.gdk.Gdk

private val logger = LoggerFactory.getLogger("AppUtils")

// Gdk.unicodeToKeyval returns this if there is no corresponding symbol
private const val UNICODE_KEYSYM_FLAG = 0x01000000

/**
 * Converts a Unicode code point to its GDK keysym value.
 */
fun unicodeToKeySym(codePoint: Int): Int = Gdk.unicodeToKeyval(codePoint)

/**
 * Converts a GDK keysym value back to its Unicode code point.
 */
fun keySymToUnicode(keySym: Int): Int = Gdk.keyvalToUnicode(keySym)

fun textToKeySym(text: String): List<Int> {
    return text
        .replace("\n", " ") // Convert newlines to spaces
        .filterNot { it.isISOControl() } // Remove control characters (tabs, carriage returns, etc.)
        .map { unicodeToKeySym(it.code) } // Convert each character to its GDK keysym value
        .also { keySyms ->
            val unmapped = keySyms.count { it and UNICODE_KEYSYM_FLAG != 0 }
            if (unmapped > 0) {
                logger.warn("$unmapped characters have no direct GDK keysym mapping")
            }
        }
}
