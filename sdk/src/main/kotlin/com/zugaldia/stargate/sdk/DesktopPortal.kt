package com.zugaldia.stargate.sdk

import com.zugaldia.stargate.sdk.globalshortcuts.GlobalShortcutsPortal
import com.zugaldia.stargate.sdk.remotedesktop.RemoteDesktopPortal
import com.zugaldia.stargate.sdk.settings.SettingsPortal
import org.freedesktop.dbus.connections.impl.DBusConnection
import org.freedesktop.dbus.connections.impl.DBusConnectionBuilder

/**
 * Main entry point for accessing XDG Desktop Portal interfaces.
 * Manages the D-Bus connection and provides access to individual portals.
 */
class DesktopPortal(private val connection: DBusConnection) : AutoCloseable {

    /**
     * Access to the Settings portal for reading desktop appearance preferences.
     */
    val settings: SettingsPortal by lazy { SettingsPortal(connection) }

    /**
     * Access to the RemoteDesktop portal for remote desktop functionality.
     */
    val remoteDesktop: RemoteDesktopPortal by lazy { RemoteDesktopPortal(connection) }

    /**
     * Access to the GlobalShortcuts portal for registering global keyboard shortcuts.
     */
    val globalShortcuts: GlobalShortcutsPortal by lazy { GlobalShortcutsPortal(connection) }

    /**
     * Closes the underlying D-Bus connection.
     */
    override fun close() {
        connection.close()
    }

    companion object {
        /**
         * Creates a new DesktopPortal connected to the session bus.
         */
        fun connect(): DesktopPortal =
            DesktopPortal(DBusConnectionBuilder.forSessionBus().build())
    }
}
