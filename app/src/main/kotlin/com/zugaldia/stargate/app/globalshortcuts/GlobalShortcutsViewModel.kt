package com.zugaldia.stargate.app.globalshortcuts

import com.zugaldia.stargate.app.SIGNAL_STATE_CHANGED
import com.zugaldia.stargate.sdk.DesktopPortal
import com.zugaldia.stargate.sdk.globalshortcuts.Shortcut
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import org.apache.logging.log4j.LogManager
import org.gnome.glib.GLib
import org.gnome.gobject.GObject
import org.javagi.gobject.annotations.Signal

class GlobalShortcutsViewModel(private val portal: DesktopPortal) : GObject() {
    private val logger = LogManager.getLogger()

    private val job: Job = SupervisorJob()
    private val scope = CoroutineScope(job + Dispatchers.Default)

    private var _state: GlobalShortcutsState = GlobalShortcutsState()
    val state: GlobalShortcutsState
        get() = _state

    @Signal
    interface StateChanged {
        fun apply()
    }

    private fun updateState(value: GlobalShortcutsState) {
        _state = value
        GLib.idleAdd(GLib.PRIORITY_DEFAULT) {
            logger.info("State updated, emitting $SIGNAL_STATE_CHANGED signal")
            emit(SIGNAL_STATE_CHANGED)
            false
        }
    }

    fun startSession() {
        if (_state.isLoading || _state.isSessionActive) {
            logger.warn("Session already loading or active, ignoring start request")
            return
        }

        updateState(_state.copy(isLoading = true, error = null))

        scope.launch {
            val shortcuts = listOf(
                Shortcut(
                    id = "stargate-test",
                    description = "Test shortcut for Stargate",
                    preferredTrigger = "CTRL+SHIFT+z"
                )
            )
            logger.info("Starting global shortcuts session with {} shortcut(s)", shortcuts.size)
            val result = portal.globalShortcuts.startSession(shortcuts)

            result.fold(
                onSuccess = { boundShortcuts ->
                    logger.info("Session created and shortcuts bound: count={}", boundShortcuts.size)
                    boundShortcuts.forEach { shortcut ->
                        logger.info(
                            "  Bound shortcut: id={}, trigger={}",
                            shortcut.id,
                            shortcut.triggerDescription
                        )
                    }
                    updateState(
                        _state.copy(
                            isLoading = false,
                            isSessionActive = true
                        )
                    )
                },
                onFailure = { error ->
                    logger.error("Failed to create session", error)
                    updateState(_state.copy(isLoading = false, error = error.message))
                }
            )
        }
    }

    fun stopSession() {
        if (!_state.isSessionActive) {
            logger.warn("No active session to stop")
            return
        }

        logger.info("Stopping global shortcuts session")
        portal.globalShortcuts.clearSession()
        updateState(GlobalShortcutsState())
    }

    fun listShortcuts() {
        if (!_state.isSessionActive) {
            logger.warn("No active session, cannot list shortcuts")
            return
        }

        scope.launch {
            val sessionHandle = portal.globalShortcuts.activeSession
            if (sessionHandle == null) {
                logger.error("No active session handle available")
                updateState(_state.copy(error = "No active session handle"))
                return@launch
            }

            logger.info("Listing shortcuts for active session")
            val result = portal.globalShortcuts.listShortcuts(sessionHandle)

            result.fold(
                onSuccess = { shortcuts ->
                    logger.info("Shortcuts listed successfully: count={}", shortcuts.size)
                    shortcuts.forEach { shortcut ->
                        logger.info(
                            "  Shortcut: id={}, description={}, trigger={}",
                            shortcut.id,
                            shortcut.description,
                            shortcut.triggerDescription
                        )
                    }
                },
                onFailure = { error ->
                    logger.error("Failed to list shortcuts", error)
                    updateState(_state.copy(error = error.message))
                }
            )
        }
    }

    suspend fun closeAndJoin() {
        logger.info("Closing GlobalShortcutsViewModel, cancelling coroutine scope and awaiting completion")
        portal.globalShortcuts.clearSession()
        job.cancelAndJoin()
    }
}
