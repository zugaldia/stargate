package com.zugaldia.stargate.app.settings

import com.zugaldia.stargate.app.SIGNAL_STATE_CHANGED
import com.zugaldia.stargate.app.SPACING
import org.gnome.gtk.Align
import org.gnome.gtk.Box
import org.gnome.gtk.Label
import org.gnome.gtk.Orientation
import org.gnome.gtk.Widget

class SettingsScreen(private val viewModel: SettingsViewModel) {
    private lateinit var versionLabel: Label
    private lateinit var colorSchemeLabel: Label
    private lateinit var accentColorLabel: Label
    private lateinit var contrastLabel: Label
    private lateinit var reducedMotionLabel: Label

    fun build(): Widget {
        val box = Box(Orientation.VERTICAL, SPACING)
        box.hexpand = true
        box.vexpand = true
        box.halign = Align.CENTER
        box.valign = Align.CENTER

        versionLabel = Label("Loading...")
        colorSchemeLabel = Label("Loading...")
        accentColorLabel = Label("Loading...")
        contrastLabel = Label("Loading...")
        reducedMotionLabel = Label("Loading...")

        box.append(versionLabel)
        box.append(colorSchemeLabel)
        box.append(accentColorLabel)
        box.append(contrastLabel)
        box.append(reducedMotionLabel)

        updateLabels(viewModel.state)
        viewModel.connect(SIGNAL_STATE_CHANGED, object : SettingsViewModel.StateChanged {
            override fun apply() {
                updateLabels(viewModel.state)
            }
        })

        return box
    }

    private fun updateLabels(state: SettingsState) {
        versionLabel.label = "Version: ${state.version ?: "Unknown"}"
        colorSchemeLabel.label = "Color Scheme: ${state.colorScheme ?: "Unknown"}"
        accentColorLabel.label = "Accent Color: ${formatAccentColor(state)}"
        contrastLabel.label = "Contrast: ${state.contrast ?: "Unknown"}"
        reducedMotionLabel.label = "Reduced Motion: ${state.reducedMotion ?: "Unknown"}"
    }

    private fun formatAccentColor(state: SettingsState): String {
        val color = state.accentColor ?: return "Unknown"
        return "RGB(%.2f, %.2f, %.2f)".format(color.red, color.green, color.blue)
    }
}
