package com.zugaldia.stargate.app.remotedesktop

import com.zugaldia.stargate.app.SIGNAL_STATE_CHANGED
import com.zugaldia.stargate.app.textToKeySym
import com.zugaldia.stargate.sdk.DesktopPortal
import com.zugaldia.stargate.sdk.remotedesktop.DeviceType
import com.zugaldia.stargate.sdk.remotedesktop.InputState
import com.zugaldia.stargate.sdk.remotedesktop.PersistMode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.apache.logging.log4j.LogManager
import org.gnome.glib.GLib
import org.gnome.gobject.GObject
import org.javagi.gobject.annotations.Signal

class RemoteDesktopViewModel(private val portal: DesktopPortal) : GObject() {
    private val logger = LogManager.getLogger()

    private val job: Job = SupervisorJob()
    private val scope = CoroutineScope(job + Dispatchers.Default)

    private var _state: RemoteDesktopState = RemoteDesktopState()
    val state: RemoteDesktopState
        get() = _state

    @Signal
    interface StateChanged {
        fun apply()
    }

    private fun updateState(value: RemoteDesktopState) {
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
            val restoreToken = _state.restoreToken
            logger.info(
                "Starting remote desktop session with keyboard device type, restoreToken={}",
                if (restoreToken != null) "present" else "null"
            )
            val result = portal.remoteDesktop.startSession(
                types = setOf(DeviceType.KEYBOARD),
                restoreToken = restoreToken,
                persistMode = PersistMode.UNTIL_REVOKED
            )

            result.fold(
                onSuccess = { response ->
                    logger.info(
                        "Session started successfully: devices={}, clipboardEnabled={}, restoreToken={}",
                        response.devices,
                        response.clipboardEnabled,
                        if (response.restoreToken != null) "present" else "null"
                    )
                    updateState(
                        _state.copy(
                            isLoading = false,
                            isSessionActive = true,
                            devices = response.devices,
                            clipboardEnabled = response.clipboardEnabled,
                            restoreToken = response.restoreToken
                        )
                    )
                },
                onFailure = { error ->
                    logger.error("Failed to start session", error)
                    updateState(_state.copy(isLoading = false, error = error.message))
                }
            )
        }
    }

    fun typeTextWithCountdown(text: String) {
        if (!_state.isSessionActive || _state.countdownSeconds != null || _state.isTyping) {
            logger.warn("Cannot type: session not active or already counting down/typing")
            return
        }

        scope.launch {
            @Suppress("TooGenericExceptionCaught")
            try {
                // Countdown from 5 to 1
                for (seconds in COUNTDOWN_SECONDS downTo 1) {
                    updateState(_state.copy(countdownSeconds = seconds))
                    delay(COUNTDOWN_DELAY_MS)
                }
                updateState(_state.copy(countdownSeconds = null, isTyping = true))

                // Type the text with a delay between keystrokes
                logger.info("Typing ${text.length} characters")
                for (keySym in textToKeySym(text)) {
                    portal.remoteDesktop.notifyKeyboardKeySym(keySym, InputState.PRESSED)
                    portal.remoteDesktop.notifyKeyboardKeySym(keySym, InputState.RELEASED)
                    delay(TYPING_DELAY_MS)
                }
            } catch (e: kotlinx.coroutines.CancellationException) {
                throw e
            } catch (e: Exception) {
                logger.error("Failed to type text", e)
                updateState(_state.copy(error = e.message ?: "Unknown error while typing"))
            } finally {
                updateState(_state.copy(isTyping = false, countdownSeconds = null))
            }
        }
    }

    fun stopSession() {
        if (!_state.isSessionActive) {
            logger.warn("No active session to stop")
            return
        }

        logger.info("Stopping remote desktop session, preserving restoreToken")
        portal.remoteDesktop.clearSession()

        // Keep the restore token so it can be used to restore the session later
        updateState(RemoteDesktopState(restoreToken = _state.restoreToken))
    }

    suspend fun closeAndJoin() {
        logger.info("Closing RemoteDesktopViewModel, cancelling coroutine scope and awaiting completion")
        portal.remoteDesktop.clearSession()
        job.cancelAndJoin()
    }

    companion object {
        private const val COUNTDOWN_SECONDS = 5
        private const val COUNTDOWN_DELAY_MS = 1000L
        private const val TYPING_DELAY_MS = 50L
    }
}
