package com.zugaldia.stargate.app.notification

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

class NotificationScreen(private val viewModel: NotificationViewModel) {
    private lateinit var sendButton: Button
    private lateinit var statusLabel: Label
    private lateinit var textView: TextView

    fun build(): Widget {
        val box = Box(Orientation.VERTICAL, SPACING)
        box.hexpand = true
        box.vexpand = true
        box.halign = Align.CENTER
        box.valign = Align.CENTER

        textView = TextView()
        textView.wrapMode = WrapMode.WORD_CHAR
        textView.buffer.setText("Hello from Stargate.", -1)

        val scrolledWindow = ScrolledWindow()
        scrolledWindow.child = textView
        scrolledWindow.minContentWidth = TEXT_VIEW_WIDTH
        scrolledWindow.minContentHeight = TEXT_VIEW_HEIGHT

        sendButton = Button.withLabel("Send Notification")
        sendButton.onClicked {
            val buffer = textView.buffer
            val startIter = TextIter()
            val endIter = TextIter()
            buffer.getStartIter(startIter)
            buffer.getEndIter(endIter)
            val body = buffer.getText(startIter, endIter, false)
            viewModel.sendNotification(body)
        }

        statusLabel = Label("")
        statusLabel.maxWidthChars = ERROR_LABEL_MAX_WIDTH_CHARS
        statusLabel.wrap = true

        box.append(scrolledWindow)
        box.append(sendButton)
        box.append(statusLabel)

        viewModel.connect(SIGNAL_STATE_CHANGED, object : NotificationViewModel.StateChanged {
            override fun apply() {
                updateUI(viewModel.state)
            }
        })

        return box
    }

    private fun updateUI(state: NotificationState) {
        sendButton.sensitive = !state.isSending
        sendButton.label = if (state.isSending) "Sending..." else "Send Notification"
        statusLabel.label = state.error ?: state.lastResult ?: ""
    }
}
