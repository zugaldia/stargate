package com.zugaldia.stargate.app.globalshortcuts

import com.zugaldia.stargate.app.LABEL_MAX_WIDTH_CHARS
import com.zugaldia.stargate.app.PREFERRED_TRIGGER
import com.zugaldia.stargate.app.SIGNAL_STATE_CHANGED
import com.zugaldia.stargate.app.SPACING
import org.gnome.gtk.Align
import org.gnome.gtk.Box
import org.gnome.gtk.Button
import org.gnome.gtk.Label
import org.gnome.gtk.Orientation
import org.gnome.gtk.Widget

class GlobalShortcutsScreen(private val viewModel: GlobalShortcutsViewModel) {
    private lateinit var createSessionButton: Button
    private lateinit var bindButton: Button
    private lateinit var configureButton: Button
    private lateinit var listButton: Button
    private lateinit var stopButton: Button
    private lateinit var shortcutsLabel: Label
    private lateinit var activationsLabel: Label
    private lateinit var messageLabel: Label

    fun build(): Widget {
        val box = Box(Orientation.VERTICAL, SPACING)
        box.halign = Align.CENTER
        box.valign = Align.CENTER

        createSessionButton = Button.withLabel("Create Session")
        createSessionButton.onClicked { viewModel.createSession() }
        box.append(createSessionButton)

        bindButton = Button.withLabel("Bind Shortcuts ($PREFERRED_TRIGGER)")
        bindButton.onClicked { viewModel.bindShortcuts() }
        box.append(bindButton)

        listButton = Button.withLabel("List Shortcuts")
        listButton.onClicked { viewModel.listShortcuts() }
        box.append(listButton)

        configureButton = Button.withLabel("Configure Shortcuts")
        configureButton.onClicked { viewModel.configureShortcuts() }
        configureButton.visible = false
        box.append(configureButton)

        stopButton = Button.withLabel("Stop Session")
        stopButton.onClicked { viewModel.stopSession() }
        box.append(stopButton)

        shortcutsLabel = Label("")
        shortcutsLabel.wrap = true
        shortcutsLabel.maxWidthChars = LABEL_MAX_WIDTH_CHARS
        shortcutsLabel.visible = false
        box.append(shortcutsLabel)

        activationsLabel = Label("")
        activationsLabel.wrap = true
        activationsLabel.maxWidthChars = LABEL_MAX_WIDTH_CHARS
        activationsLabel.visible = false
        box.append(activationsLabel)

        messageLabel = Label("")
        messageLabel.wrap = true
        messageLabel.maxWidthChars = LABEL_MAX_WIDTH_CHARS
        messageLabel.visible = false
        box.append(messageLabel)

        updateUI(viewModel.state)
        viewModel.connect(SIGNAL_STATE_CHANGED, object : GlobalShortcutsViewModel.StateChanged {
            override fun apply() {
                updateUI(viewModel.state)
            }
        })

        return box
    }

    private fun updateUI(state: GlobalShortcutsState) {
        updateCreateSessionButton(state)
        updateSessionButtons(state)
        updateShortcutsLabel(state)
        updateActivationsLabel(state)
        updateMessageLabel(state)
    }

    private fun updateCreateSessionButton(state: GlobalShortcutsState) {
        createSessionButton.sensitive = !state.isLoading && !state.isSessionActive
        createSessionButton.label = when {
            state.isLoading -> "Creating..."
            state.isSessionActive -> "Session Active"
            else -> "Create Session"
        }
    }

    private fun updateSessionButtons(state: GlobalShortcutsState) {
        bindButton.sensitive = state.isSessionActive
        configureButton.visible = state.isConfigureSupported
        configureButton.sensitive = state.isSessionActive
        listButton.sensitive = state.isSessionActive
        stopButton.sensitive = state.isSessionActive
    }

    private fun updateShortcutsLabel(state: GlobalShortcutsState) {
        when {
            state.shortcuts == null -> shortcutsLabel.visible = false
            state.shortcuts.isEmpty() -> {
                shortcutsLabel.label = "No shortcuts registered."
                shortcutsLabel.visible = true
            }
            else -> {
                val text = state.shortcuts.joinToString("\n") { shortcut ->
                    "${shortcut.id}: ${shortcut.triggerDescription ?: "No trigger"}"
                }
                shortcutsLabel.label = text
                shortcutsLabel.visible = true
            }
        }
    }

    private fun updateActivationsLabel(state: GlobalShortcutsState) {
        if (state.activations.isNotEmpty()) {
            val text = state.activations.joinToString("\n") { activation ->
                val action = if (activation.activated) "Activated" else "Deactivated"
                "$action: ${activation.shortcutId} (t=${activation.timestamp})"
            }
            activationsLabel.label = text
            activationsLabel.visible = true
        } else {
            activationsLabel.visible = false
        }
    }

    private fun updateMessageLabel(state: GlobalShortcutsState) {
        if (state.message != null) {
            messageLabel.label = state.message
            messageLabel.visible = true
        } else {
            messageLabel.visible = false
        }
    }
}
