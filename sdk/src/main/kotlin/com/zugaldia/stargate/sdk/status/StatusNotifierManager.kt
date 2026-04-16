package com.zugaldia.stargate.sdk.status

import org.freedesktop.dbus.connections.impl.DBusConnection
import org.freedesktop.dbus.connections.impl.DBusConnectionBuilder
import org.kde.StatusNotifierWatcher

/**
 * Wrapper around the org.kde.StatusNotifierWatcher D-Bus interface.
 * Provides access to the KDE StatusNotifier protocol for system tray integration.
 */
class StatusNotifierManager(private val connection: DBusConnection) : AutoCloseable {
    private val watcher: StatusNotifierWatcher =
        connection.getRemoteObject(
            STATUS_NOTIFIER_BUS_NAME,
            STATUS_NOTIFIER_OBJECT_PATH,
            StatusNotifierWatcher::class.java
        )

    /**
     * Returns true if a StatusNotifierHost is currently registered with the watcher.
     */
    fun isStatusNotifierHostRegistered(): Boolean = watcher.isIsStatusNotifierHostRegistered

    /**
     * Returns the protocol version supported by the StatusNotifierWatcher.
     */
    fun getProtocolVersion(): Int = watcher.protocolVersion

    /**
     * Returns the list of registered StatusNotifierItem service names.
     */
    fun getRegisteredStatusNotifierItems(): List<String> = watcher.registeredStatusNotifierItems

    /**
     * Exports a StatusNotifierItem on the session bus and registers it with the watcher.
     * Returns the well-known service name that was claimed (e.g. "org.kde.StatusNotifierItem-1234-1").
     */
    fun registerItem(
        item: StargateStatusNotifierItem,
        identifier: String = STATUS_NOTIFIER_ITEM_SERVICE_PREFIX
    ): String {
        val pid = ProcessHandle.current().pid()
        val serviceName = "$identifier-$pid-1"
        connection.exportObject(STATUS_NOTIFIER_ITEM_OBJECT_PATH, item)
        item.menu?.let { connection.exportObject(DBUS_MENU_OBJECT_PATH, it) }
        connection.requestBusName(serviceName)
        watcher.RegisterStatusNotifierItem(serviceName)
        return serviceName
    }

    override fun close() {
        connection.close()
    }

    companion object {
        /**
         * Creates a new StatusNotifierManager connected to the session bus.
         */
        fun connect(): StatusNotifierManager =
            StatusNotifierManager(DBusConnectionBuilder.forSessionBus().build())
    }
}
