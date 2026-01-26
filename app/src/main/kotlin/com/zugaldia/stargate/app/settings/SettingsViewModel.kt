package com.zugaldia.stargate.app.settings

import com.zugaldia.stargate.app.SIGNAL_STATE_CHANGED
import com.zugaldia.stargate.sdk.DesktopPortal
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

class SettingsViewModel(private val portal: DesktopPortal) : GObject() {
    private val logger = LogManager.getLogger()

    private val job: Job = SupervisorJob()
    private val scope = CoroutineScope(job + Dispatchers.Default)

    private var _state: SettingsState = SettingsState()
    val state: SettingsState
        get() = _state

    @Signal
    interface StateChanged {
        fun apply()
    }

    private fun updateState(value: SettingsState) {
        _state = value
        GLib.idleAdd(GLib.PRIORITY_DEFAULT) {
            logger.info("State updated, emitting $SIGNAL_STATE_CHANGED signal")
            emit(SIGNAL_STATE_CHANGED)
            false
        }
    }

    init {
        logger.info("Initializing SettingsViewModel")
        loadSettings()
        subscribeToChanges()
    }

    private fun loadSettings() {
        val newState = SettingsState(
            version = portal.settings.version,
            colorScheme = portal.settings.getColorScheme().getOrNull(),
            accentColor = portal.settings.getAccentColor().getOrNull(),
            contrast = portal.settings.getContrast().getOrNull(),
            reducedMotion = portal.settings.getReducedMotion().getOrNull()
        )

        logger.info("Initial settings loaded: $newState")
        updateState(newState)
    }

    private fun subscribeToChanges() {
        scope.launch {
            portal.settings.colorSchemeChanges.collect { colorScheme ->
                logger.info("Color scheme changed: $colorScheme")
                updateState(_state.copy(colorScheme = colorScheme))
            }
        }
        scope.launch {
            portal.settings.accentColorChanges.collect { accentColor ->
                logger.info("Accent color changed: $accentColor")
                updateState(_state.copy(accentColor = accentColor))
            }
        }
        scope.launch {
            portal.settings.contrastChanges.collect { contrast ->
                logger.info("Contrast changed: $contrast")
                updateState(_state.copy(contrast = contrast))
            }
        }
        scope.launch {
            portal.settings.reducedMotionChanges.collect { reducedMotion ->
                logger.info("Reduced motion changed: $reducedMotion")
                updateState(_state.copy(reducedMotion = reducedMotion))
            }
        }
    }

    suspend fun closeAndJoin() {
        logger.info("Closing SettingsViewModel, cancelling coroutine scope and awaiting completion")
        job.cancelAndJoin()
    }
}
