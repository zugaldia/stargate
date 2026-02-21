package com.zugaldia.stargate.app.remotedesktop

import com.zugaldia.stargate.app.DEFAULT_TEXT
import com.zugaldia.stargate.app.ERROR_LABEL_MAX_WIDTH_CHARS
import com.zugaldia.stargate.app.SIGNAL_STATE_CHANGED
import com.zugaldia.stargate.app.SPACING
import com.zugaldia.stargate.app.TEXT_VIEW_HEIGHT
import com.zugaldia.stargate.app.TEXT_VIEW_WIDTH
import org.gnome.gtk.Align
import org.gnome.gtk.Box
import org.gnome.gtk.Button
import org.gnome.gtk.Label
import org.gnome.gtk.Orientation
import org.gnome.gtk.ScrolledWindow
import org.gnome.gtk.TextIter
import org.gnome.gtk.TextView
import org.gnome.gtk.Widget
import org.gnome.gtk.WrapMode

class RemoteDesktopScreen(private val viewModel: RemoteDesktopViewModel) {
    private lateinit var startButton: Button
    private lateinit var sessionInfoLabel: Label
    private lateinit var textView: TextView
    private lateinit var typeButton: Button
    private lateinit var stopButton: Button
    private lateinit var errorLabel: Label

    fun build(): Widget {
        val box = Box(Orientation.VERTICAL, SPACING)
        box.halign = Align.CENTER
        box.valign = Align.CENTER

        startButton = Button.withLabel("Start Session")
        startButton.onClicked { viewModel.startSession() }
        box.append(startButton)

        sessionInfoLabel = Label("")
        sessionInfoLabel.visible = false
        box.append(sessionInfoLabel)

        textView = TextView()
        textView.buffer.setText(DEFAULT_TEXT, -1)
        textView.wrapMode = WrapMode.WORD_CHAR
        val scrolledWindow = ScrolledWindow()
        scrolledWindow.child = textView
        scrolledWindow.setSizeRequest(TEXT_VIEW_WIDTH, TEXT_VIEW_HEIGHT)
        box.append(scrolledWindow)

        typeButton = Button.withLabel("Type Text")
        typeButton.onClicked {
            val buffer = textView.buffer
            val startIter = TextIter()
            val endIter = TextIter()
            buffer.getStartIter(startIter)
            buffer.getEndIter(endIter)
            val text = buffer.getText(startIter, endIter, false)
            viewModel.typeTextWithCountdown(text)
        }

        box.append(typeButton)

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
        viewModel.connect(SIGNAL_STATE_CHANGED, object : RemoteDesktopViewModel.StateChanged {
            override fun apply() {
                updateUI(viewModel.state)
            }
        })

        return box
    }

    private fun updateUI(state: RemoteDesktopState) {
        updateStartButton(state)
        updateSessionInfo(state)
        updateTypingControls(state)
        updateStopButton(state)
        updateErrorLabel(state)
    }

    private fun updateStartButton(state: RemoteDesktopState) {
        startButton.sensitive = !state.isLoading && !state.isSessionActive
        startButton.label = when {
            state.isLoading -> "Starting..."
            state.isSessionActive -> "Session Active"
            state.restoreToken != null -> "Restore Session"
            else -> "Start Session"
        }
    }

    private fun updateSessionInfo(state: RemoteDesktopState) {
        if (state.isSessionActive) {
            val devices = state.devices.joinToString(", ") { it.name }
            val clipboard = when (state.clipboardEnabled) {
                true -> "Yes"
                false -> "No"
                null -> "N/A"
            }
            val restoreToken = if (state.restoreToken != null) "Available" else "N/A"
            sessionInfoLabel.label = "Devices: $devices\nClipboard: $clipboard\nRestore Token: $restoreToken"
            sessionInfoLabel.visible = true
        } else {
            sessionInfoLabel.visible = false
        }
    }

    private fun updateTypingControls(state: RemoteDesktopState) {
        val canType = state.isSessionActive && state.countdownSeconds == null && !state.isTyping
        textView.sensitive = canType
        typeButton.sensitive = canType
        typeButton.label = when {
            state.countdownSeconds != null -> "Typing in ${state.countdownSeconds}..."
            state.isTyping -> "Typing..."
            else -> "Type Text"
        }
    }

    private fun updateStopButton(state: RemoteDesktopState) {
        stopButton.sensitive = state.isSessionActive && state.countdownSeconds == null && !state.isTyping
    }

    private fun updateErrorLabel(state: RemoteDesktopState) {
        if (state.error != null) {
            errorLabel.label = state.error
            errorLabel.visible = true
        } else {
            errorLabel.visible = false
        }
    }
}
