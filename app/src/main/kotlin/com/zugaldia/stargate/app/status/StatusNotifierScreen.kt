package com.zugaldia.stargate.app.status

import com.zugaldia.stargate.app.LABEL_MAX_WIDTH_CHARS
import com.zugaldia.stargate.app.SIGNAL_STATE_CHANGED
import com.zugaldia.stargate.app.SPACING
import org.gnome.gtk.Align
import org.gnome.gtk.Box
import org.gnome.gtk.Button
import org.gnome.gtk.Label
import org.gnome.gtk.Orientation
import org.gnome.gtk.Widget

class StatusNotifierScreen(private val viewModel: StatusNotifierViewModel) {
    private lateinit var connectButton: Button
    private lateinit var statusLabel: Label
    private lateinit var hostRegisteredLabel: Label
    private lateinit var protocolVersionLabel: Label
    private lateinit var registeredItemsLabel: Label

    fun build(): Widget {
        val box = Box(Orientation.VERTICAL, SPACING)
        box.hexpand = true
        box.vexpand = true
        box.halign = Align.CENTER
        box.valign = Align.CENTER

        connectButton = Button.withLabel("Connect to Status Notifier Watcher")
        connectButton.onClicked {
            viewModel.connect()
        }

        statusLabel = Label("")
        statusLabel.maxWidthChars = LABEL_MAX_WIDTH_CHARS
        statusLabel.wrap = true

        hostRegisteredLabel = Label("")
        protocolVersionLabel = Label("")
        registeredItemsLabel = Label("")
        registeredItemsLabel.maxWidthChars = LABEL_MAX_WIDTH_CHARS
        registeredItemsLabel.wrap = true

        box.append(connectButton)
        box.append(statusLabel)
        box.append(hostRegisteredLabel)
        box.append(protocolVersionLabel)
        box.append(registeredItemsLabel)

        viewModel.connect(SIGNAL_STATE_CHANGED, object : StatusNotifierViewModel.StateChanged {
            override fun apply() {
                updateUI(viewModel.state)
            }
        })

        return box
    }

    private fun updateUI(state: StatusNotifierState) {
        connectButton.sensitive = !state.isLoading
        connectButton.label = if (state.isLoading) "Connecting..." else "Connect to Status Notifier Watcher"
        statusLabel.label = state.error ?: state.message ?: ""

        val connected = state.isConnected
        hostRegisteredLabel.visible = connected
        protocolVersionLabel.visible = connected
        registeredItemsLabel.visible = connected

        if (connected) {
            hostRegisteredLabel.label = "Host Registered: ${state.isStatusNotifierHostRegistered}"
            protocolVersionLabel.label = "Protocol Version: ${state.protocolVersion}"
            registeredItemsLabel.label = "Registered Items: ${
                if (state.registeredStatusNotifierItems.isEmpty()) "none"
                else state.registeredStatusNotifierItems.joinToString(", ")
            }"
        }
    }
}
