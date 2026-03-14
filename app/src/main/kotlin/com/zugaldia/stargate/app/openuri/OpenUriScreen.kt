package com.zugaldia.stargate.app.openuri

import com.zugaldia.stargate.app.ERROR_LABEL_MAX_WIDTH_CHARS
import com.zugaldia.stargate.app.SIGNAL_STATE_CHANGED
import com.zugaldia.stargate.app.SPACING
import org.gnome.gtk.Align
import org.gnome.gtk.Box
import org.gnome.gtk.Button
import org.gnome.gtk.Entry
import org.gnome.gtk.Label
import org.gnome.gtk.Orientation
import org.gnome.gtk.Widget

private const val DEFAULT_URI = "https://github.com/zugaldia/stargate"
private const val ENTRY_WIDTH_CHARS = 40

class OpenUriScreen(private val viewModel: OpenUriViewModel) {
    private lateinit var uriEntry: Entry
    private lateinit var openButton: Button
    private lateinit var errorLabel: Label

    fun build(): Widget {
        val box = Box(Orientation.VERTICAL, SPACING)
        box.hexpand = true
        box.vexpand = true
        box.halign = Align.CENTER
        box.valign = Align.CENTER

        uriEntry = Entry()
        uriEntry.text = DEFAULT_URI
        uriEntry.widthChars = ENTRY_WIDTH_CHARS

        openButton = Button.withLabel("Open")
        openButton.onClicked {
            val uri = uriEntry.text
            if (uri.isNotBlank()) {
                viewModel.openUri(uri)
            }
        }

        errorLabel = Label("")
        errorLabel.maxWidthChars = ERROR_LABEL_MAX_WIDTH_CHARS
        errorLabel.wrap = true

        box.append(uriEntry)
        box.append(openButton)
        box.append(errorLabel)

        viewModel.connect(SIGNAL_STATE_CHANGED, object : OpenUriViewModel.StateChanged {
            override fun apply() {
                updateUI(viewModel.state)
            }
        })

        return box
    }

    private fun updateUI(state: OpenUriState) {
        openButton.sensitive = !state.isOpening
        openButton.label = if (state.isOpening) "Opening..." else "Open"
        errorLabel.label = state.error ?: ""
    }
}
