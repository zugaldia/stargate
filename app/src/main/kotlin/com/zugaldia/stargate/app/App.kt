package com.zugaldia.stargate.app

import com.zugaldia.stargate.sdk.DesktopPortal
import org.gnome.gtk.Align
import org.gnome.gtk.Application
import org.gnome.gtk.ApplicationWindow
import org.gnome.gtk.Box
import org.gnome.gtk.Button
import org.gnome.gtk.Label
import org.gnome.gtk.Orientation

private const val DEFAULT_WINDOW_WIDTH = 800
private const val DEFAULT_WINDOW_HEIGHT = 600
private const val DEFAULT_BOX_SPACING = 10

fun main(args: Array<String>) {
    val app = Application("com.zugaldia.stargate.App")
    app.onActivate { activate(app) }
    app.run(args)
}

private fun activate(app: Application) {
    val window = ApplicationWindow(app)
    window.title = "Stargate"
    window.setDefaultSize(DEFAULT_WINDOW_WIDTH, DEFAULT_WINDOW_HEIGHT)

    val box = Box.builder()
        .setOrientation(Orientation.VERTICAL)
        .setHalign(Align.CENTER)
        .setValign(Align.CENTER)
        .setSpacing(DEFAULT_BOX_SPACING)
        .build()

    DesktopPortal.connect().use { portal ->
        box.append(Label.builder().setLabel("Settings Portal").build())
        box.append(Label.builder().setLabel("Version: ${portal.settings.version}").build())

        val colorSchemeText = portal.settings.getColorScheme()
            .fold(
                onSuccess = { "Color Scheme: $it" },
                onFailure = { "Color Scheme: Failed to read (${it.message})" }
            )
        box.append(Label.builder().setLabel(colorSchemeText).build())

        val accentColorText = portal.settings.getAccentColor()
            .fold(
                onSuccess = { "Accent Color: RGB(${it.red}, ${it.green}, ${it.blue})" },
                onFailure = { "Accent Color: Failed to read (${it.message})" }
            )
        box.append(Label.builder().setLabel(accentColorText).build())

        val contrastText = portal.settings.getContrast()
            .fold(
                onSuccess = { "Contrast: $it" },
                onFailure = { "Contrast: Failed to read (${it.message})" }
            )
        box.append(Label.builder().setLabel(contrastText).build())

        val reducedMotionText = portal.settings.getReducedMotion()
            .fold(
                onSuccess = { "Reduced Motion: $it" },
                onFailure = { "Reduced Motion: Failed to read (${it.message})" }
            )
        box.append(Label.builder().setLabel(reducedMotionText).build())
    }

    val button = Button.withLabel("Exit")
    button.onClicked(window::close)

    box.append(button)
    window.child = box
    window.present()
}
