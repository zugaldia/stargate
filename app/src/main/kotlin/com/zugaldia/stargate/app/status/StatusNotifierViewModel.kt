package com.zugaldia.stargate.app.status

import com.zugaldia.stargate.app.SIGNAL_STATE_CHANGED
import com.zugaldia.stargate.sdk.status.StargateMenu
import com.zugaldia.stargate.sdk.status.StargateMenuItem
import com.zugaldia.stargate.sdk.status.StatusNotifierManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.gnome.glib.GLib
import org.gnome.gobject.GObject
import org.gnome.gtk.ApplicationWindow
import org.javagi.gobject.annotations.Signal
import org.slf4j.LoggerFactory

class StatusNotifierViewModel : GObject(), AutoCloseable {
    private val logger = LoggerFactory.getLogger(StatusNotifierViewModel::class.java)

    private val job: Job = SupervisorJob()
    private val scope = CoroutineScope(job + Dispatchers.IO)

    private var manager: StatusNotifierManager? = null
    private var window: ApplicationWindow? = null

    fun setWindow(window: ApplicationWindow) {
        this.window = window
    }

    private var _state: StatusNotifierState = StatusNotifierState()
    val state: StatusNotifierState
        get() = _state

    @Signal
    interface StateChanged {
        fun apply()
    }

    private fun updateState(value: StatusNotifierState) {
        _state = value
        GLib.idleAdd(GLib.PRIORITY_DEFAULT) {
            logger.info("State updated, emitting $SIGNAL_STATE_CHANGED signal")
            emit(SIGNAL_STATE_CHANGED)
            false
        }
    }

    private fun onMenuClick(label: String, action: () -> Unit = {}): (tokenSupplier: () -> String?) -> Unit =
        { tokenSupplier ->
            GLib.idleAdd(GLib.PRIORITY_DEFAULT) {
                val token = tokenSupplier()
                logger.info("Menu: $label (token=$token)")
                token?.let { window?.setStartupId(it) }
                action()
                false
            }
        }

    private fun buildMenu(): StargateMenu = StargateMenu(
        items = listOf(
            StargateMenuItem.Action(id = 1, label = "Open", onClick = onMenuClick("Open") { window?.present() }),
            StargateMenuItem.Separator(id = 2),
            StargateMenuItem.Action(id = 3, label = "Item A", onClick = onMenuClick("Clicked A")),
            StargateMenuItem.Action(id = 4, label = "Item B", onClick = onMenuClick("Clicked B")),
            StargateMenuItem.Action(id = 5, label = "Item C", onClick = onMenuClick("Clicked C")),
            StargateMenuItem.Separator(id = 6),
            StargateMenuItem.Action(
                id = 7, label = "Quit",
                onClick = onMenuClick("Quit") { window?.application?.quit() },
            ),
        ),
        onMenuOpened = { logger.info("Menu: opened") },
        onMenuClosed = { logger.info("Menu: closed") },
    )

    @Suppress("TooGenericExceptionCaught")
    fun connect() {
        updateState(_state.copy(isLoading = true, error = null))
        scope.launch {
            try {
                manager?.close()
                val connected = StatusNotifierManager.connect()
                manager = connected
                logger.info("Connected to StatusNotifierWatcher")

                val item = StatusNotifierItemStargate(
                    menu = buildMenu(),
                    onActivate = { token ->
                        GLib.idleAdd(GLib.PRIORITY_DEFAULT) {
                            logger.info("Presenting window on activate (token=$token)")
                            token?.let { window?.setStartupId(it) }
                            window?.present()
                            false
                        }
                    },
                    onSecondaryActivate = { token ->
                        GLib.idleAdd(GLib.PRIORITY_DEFAULT) {
                            logger.info("Presenting window on secondary activate (token=$token)")
                            token?.let { window?.setStartupId(it) }
                            window?.present()
                            false
                        }
                    },
                )
                val serviceName = connected.registerItem(item, "com.zugaldia.Stargate")
                logger.info("Registered StatusNotifierItem as $serviceName")

                val hostRegistered = connected.isStatusNotifierHostRegistered()
                val protocolVersion = connected.getProtocolVersion()
                val registeredItems = connected.getRegisteredStatusNotifierItems()
                logger.info(
                    "Watcher: hostRegistered=$hostRegistered, " +
                            "protocolVersion=$protocolVersion, items=$registeredItems"
                )

                updateState(
                    _state.copy(
                        isLoading = false,
                        isConnected = true,
                        registeredServiceName = serviceName,
                        isStatusNotifierHostRegistered = hostRegistered,
                        protocolVersion = protocolVersion,
                        registeredStatusNotifierItems = registeredItems,
                        message = "Registered icon as $serviceName"
                    )
                )
            } catch (e: Exception) {
                logger.error("Failed to connect to StatusNotifierWatcher", e)
                updateState(_state.copy(isLoading = false, isConnected = false, error = e.message))
            }
        }
    }

    override fun close() {
        job.cancel()
        manager?.close()
        manager = null
    }
}
