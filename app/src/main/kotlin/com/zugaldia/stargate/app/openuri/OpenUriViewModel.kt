package com.zugaldia.stargate.app.openuri

import com.zugaldia.stargate.app.SIGNAL_STATE_CHANGED
import com.zugaldia.stargate.sdk.DesktopPortal
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.gnome.glib.GLib
import org.gnome.gobject.GObject
import org.javagi.gobject.annotations.Signal
import org.slf4j.LoggerFactory

class OpenUriViewModel(private val portal: DesktopPortal) : GObject() {
    private val logger = LoggerFactory.getLogger(OpenUriViewModel::class.java)
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private var _state: OpenUriState = OpenUriState()
    val state: OpenUriState
        get() = _state

    @Signal
    interface StateChanged {
        fun apply()
    }

    private fun updateState(value: OpenUriState) {
        _state = value
        GLib.idleAdd(GLib.PRIORITY_DEFAULT) {
            logger.info("State updated, emitting $SIGNAL_STATE_CHANGED signal")
            emit(SIGNAL_STATE_CHANGED)
            false
        }
    }

    fun openUri(uri: String) {
        updateState(_state.copy(isOpening = true, error = null))
        scope.launch {
            val result = portal.openUri.openUri(uri)
            result.fold(
                onSuccess = {
                    logger.info("URI opened successfully: {}", uri)
                    updateState(_state.copy(isOpening = false))
                },
                onFailure = { error ->
                    logger.error("Failed to open URI: {}", uri, error)
                    updateState(_state.copy(isOpening = false, error = error.message))
                }
            )
        }
    }

    suspend fun closeAndJoin() {
        scope.cancel()
    }
}
