package com.zugaldia.stargate.app.globalshortcuts

import com.zugaldia.stargate.app.PREFERRED_TRIGGER
import com.zugaldia.stargate.app.SIGNAL_STATE_CHANGED
import com.zugaldia.stargate.sdk.DesktopPortal
import com.zugaldia.stargate.sdk.globalshortcuts.Shortcut
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import org.gnome.glib.GLib
import org.gnome.gobject.GObject
import org.javagi.gobject.annotations.Signal
import org.slf4j.LoggerFactory

class GlobalShortcutsViewModel(private val portal: DesktopPortal) : GObject() {
    private val logger = LoggerFactory.getLogger(GlobalShortcutsViewModel::class.java)

    private val job: Job = SupervisorJob()
    private val scope = CoroutineScope(job + Dispatchers.Default)

    private var sessionClosedObserver: Job? = null
    private var activationsObserver: Job? = null

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

    fun createSession() {
        if (_state.isLoading || _state.isSessionActive) {
            logger.warn("Session already loading or active, ignoring start request")
            return
        }

        updateState(_state.copy(isLoading = true, error = null))

        scope.launch {
            logger.info("Creating global shortcuts session")
            val result = portal.globalShortcuts.createSession()

            result.fold(
                onSuccess = { response ->
                    logger.info("Session created: handle={}", response.sessionHandle)
                    updateState(_state.copy(isLoading = false, isSessionActive = true))
                    startObservingSessionClosed()
                    startObservingActivations()
                },
                onFailure = { error ->
                    logger.error("Failed to create session", error)
                    updateState(_state.copy(isLoading = false, error = error.message))
                }
            )
        }
    }

    // This will typically cause the portal to present a system dialog allowing
    // the user to review and configure the requested shortcuts.
    fun bindShortcuts() {
        scope.launch {
            logger.info("Binding {} shortcut(s)", PREFERRED_TRIGGER)
            val result = portal.globalShortcuts.bindShortcuts(
                listOf(
                    Shortcut(
                        id = "stargate-test",
                        description = "Test shortcut for Stargate",
                        preferredTrigger = PREFERRED_TRIGGER
                    )
                )
            )

            result.fold(
                onSuccess = { boundShortcuts ->
                    logger.info("Shortcuts bound: count={}", boundShortcuts.size)
                    boundShortcuts.forEach { shortcut ->
                        logger.info(
                            "Bound shortcut: id={}, trigger={}",
                            shortcut.id,
                            shortcut.triggerDescription
                        )
                    }
                    updateState(_state.copy(shortcuts = boundShortcuts))
                },
                onFailure = { error ->
                    logger.error("Failed to bind shortcuts", error)
                    updateState(_state.copy(error = error.message))
                }
            )
        }
    }

    fun listShortcuts() {
        scope.launch {
            logger.info("Listing shortcuts for active session")
            val result = portal.globalShortcuts.listShortcuts()

            result.fold(
                onSuccess = { shortcuts ->
                    logger.info("Shortcuts listed successfully: count={}", shortcuts.size)
                    shortcuts.forEach { shortcut ->
                        logger.info(
                            "Shortcut: id={}, description={}, trigger={}",
                            shortcut.id,
                            shortcut.description,
                            shortcut.triggerDescription
                        )
                    }
                    updateState(_state.copy(shortcuts = shortcuts))
                },
                onFailure = { error ->
                    logger.error("Failed to list shortcuts", error)
                    updateState(_state.copy(error = error.message))
                }
            )
        }
    }

    // Requests the portal to show a system configuration UI for all shortcuts
    // in the current session.
    fun configureShortcuts() {
        scope.launch {
            logger.info("Opening shortcut configuration dialog")
            portal.globalShortcuts.configureShortcuts().onFailure { error ->
                logger.error("Failed to configure shortcuts", error)
                updateState(_state.copy(error = error.message))
            }
        }
    }

    private fun startObservingSessionClosed() {
        sessionClosedObserver?.cancel()
        sessionClosedObserver = scope.launch {
            @Suppress("TooGenericExceptionCaught")
            try {
                portal.globalShortcuts.observeSessionClosed().collect { event ->
                    logger.warn("Session was closed: sessionHandle={}, details={}", event.sessionHandle, event.details)
                    updateState(GlobalShortcutsState(error = "Session was closed by the system"))
                }
            } catch (e: Exception) {
                logger.error("Error observing session closed events", e)
            }
        }
    }

    private fun startObservingActivations() {
        activationsObserver?.cancel()
        activationsObserver = scope.launch {
            @Suppress("TooGenericExceptionCaught")
            try {
                portal.globalShortcuts.activations().collect { activation ->
                    val action = if (activation.activated) "Activated" else "Deactivated"
                    logger.info(
                        "Shortcut {}: id={}, timestamp={}",
                        action,
                        activation.shortcutId,
                        activation.timestamp
                    )
                    updateState(_state.copy(activations = _state.activations + activation))
                }
            } catch (e: Exception) {
                logger.error("Error observing shortcut activations", e)
            }
        }
    }

    fun stopSession() {
        if (!_state.isSessionActive) {
            logger.warn("No active session to stop")
            return
        }

        logger.info("Stopping global shortcuts session")
        activationsObserver?.cancel()
        activationsObserver = null
        sessionClosedObserver?.cancel()
        sessionClosedObserver = null
        portal.globalShortcuts.clearSession()
        updateState(GlobalShortcutsState())
    }

    suspend fun closeAndJoin() {
        logger.info("Closing GlobalShortcutsViewModel, cancelling coroutine scope and awaiting completion")
        activationsObserver?.cancel()
        activationsObserver = null
        sessionClosedObserver?.cancel()
        sessionClosedObserver = null
        portal.globalShortcuts.clearSession()
        job.cancelAndJoin()
    }
}
