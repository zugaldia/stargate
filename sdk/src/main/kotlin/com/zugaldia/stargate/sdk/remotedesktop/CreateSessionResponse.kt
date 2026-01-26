package com.zugaldia.stargate.sdk.remotedesktop

import org.freedesktop.dbus.DBusPath

/**
 * Response from RemoteDesktop.CreateSession containing the session handle.
 *
 * @property sessionHandle Object path for the created session.
 */
data class CreateSessionResponse(
    val sessionHandle: DBusPath
)
