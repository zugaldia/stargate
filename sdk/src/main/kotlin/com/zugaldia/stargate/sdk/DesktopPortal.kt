package com.zugaldia.stargate.sdk

import com.zugaldia.stargate.sdk.settings.SettingsPortal
import org.freedesktop.dbus.connections.impl.DBusConnection
import org.freedesktop.dbus.connections.impl.DBusConnectionBuilder
import org.freedesktop.dbus.interfaces.DBusInterface
import org.freedesktop.portal.Settings

/**
 * D-Bus bus name for the desktop portal.
 */
private const val BUS_NAME = "org.freedesktop.portal.Desktop"

/**
 * D-Bus object path for the desktop portal.
 */
private const val OBJECT_PATH = "/org/freedesktop/portal/desktop"

/**
 * Main entry point for accessing XDG Desktop Portal interfaces.
 * Manages the D-Bus connection and provides access to individual portals.
 */
class DesktopPortal(private val connection: DBusConnection) : AutoCloseable {

    /**
     * Access to the Settings portal for reading desktop appearance preferences.
     */
    val settings: SettingsPortal by lazy { SettingsPortal(getRemoteObject()) }

    /**
     * Gets a remote D-Bus object for the specified interface type.
     */
    private inline fun <reified T : DBusInterface> getRemoteObject(): T =
        connection.getRemoteObject(BUS_NAME, OBJECT_PATH, T::class.java)

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
