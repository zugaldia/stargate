package com.zugaldia.stargate.sdk.remotedesktop

import org.freedesktop.dbus.types.UInt32

/**
 * Represents how a remote desktop session should persist.
 * Added in version 2 of the RemoteDesktop interface.
 */
enum class PersistMode(val value: UInt32) {
    /**
     * Do not persist (default).
     */
    DO_NOT_PERSIST(UInt32(0)),

    /**
     * Permissions persist as long as the application is running.
     */
    WHILE_APPLICATION_RUNNING(UInt32(1)),

    /**
     * Permissions persist until explicitly revoked.
     */
    UNTIL_REVOKED(UInt32(2))
}
