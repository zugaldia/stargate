package com.zugaldia.stargate.app

import org.gnome.gdk.Gdk

fun textToKeySym(text: String): List<Int> {
    return text
        .replace("\n", " ") // Convert newlines to spaces
        .filterNot { it.isISOControl() } // Remove control characters (tabs, carriage returns, etc.)
        .map { Gdk.unicodeToKeyval(it.code) } // Convert each character to its GDK keysym value
}
