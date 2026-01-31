package com.zugaldia.stargate.app.globalshortcuts

import com.zugaldia.stargate.app.ERROR_LABEL_MAX_WIDTH_CHARS
import com.zugaldia.stargate.app.SIGNAL_STATE_CHANGED
import com.zugaldia.stargate.app.SPACING
import org.gnome.gtk.Align
import org.gnome.gtk.Box
import org.gnome.gtk.Button
import org.gnome.gtk.Label
import org.gnome.gtk.Orientation
import org.gnome.gtk.Widget

class GlobalShortcutsScreen(private val viewModel: GlobalShortcutsViewModel) {
    private lateinit var startButton: Button
    private lateinit var listButton: Button
    private lateinit var stopButton: Button
    private lateinit var errorLabel: Label

    fun build(): Widget {
        val box = Box(Orientation.VERTICAL, SPACING)
        box.halign = Align.CENTER
        box.valign = Align.CENTER

        startButton = Button.withLabel("Start Session")
        startButton.onClicked { viewModel.startSession() }
        box.append(startButton)

        listButton = Button.withLabel("List Shortcuts")
        listButton.onClicked { viewModel.listShortcuts() }
        box.append(listButton)

        stopButton = Button.withLabel("Stop Session")
        stopButton.onClicked { viewModel.stopSession() }
        box.append(stopButton)

        errorLabel = Label("")
        errorLabel.addCssClass("error")
        errorLabel.wrap = true
        errorLabel.maxWidthChars = ERROR_LABEL_MAX_WIDTH_CHARS
        errorLabel.visible = false
        box.append(errorLabel)

        updateUI(viewModel.state)
        viewModel.connect(SIGNAL_STATE_CHANGED, object : GlobalShortcutsViewModel.StateChanged {
            override fun apply() {
                updateUI(viewModel.state)
            }
        })

        return box
    }

    private fun updateUI(state: GlobalShortcutsState) {
        updateStartButton(state)
        updateListButton(state)
        updateStopButton(state)
        updateErrorLabel(state)
    }

    private fun updateStartButton(state: GlobalShortcutsState) {
        startButton.sensitive = !state.isLoading && !state.isSessionActive
        startButton.label = when {
            state.isLoading -> "Starting..."
            state.isSessionActive -> "Session Active"
            else -> "Start Session"
        }
    }

    private fun updateListButton(state: GlobalShortcutsState) {
        listButton.sensitive = state.isSessionActive
    }

    private fun updateStopButton(state: GlobalShortcutsState) {
        stopButton.sensitive = state.isSessionActive
    }

    private fun updateErrorLabel(state: GlobalShortcutsState) {
        if (state.error != null) {
            errorLabel.label = state.error
            errorLabel.visible = true
        } else {
            errorLabel.visible = false
        }
    }
}
