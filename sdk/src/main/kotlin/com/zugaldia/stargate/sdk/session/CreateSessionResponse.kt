package com.zugaldia.stargate.sdk.session

import org.freedesktop.dbus.DBusPath

/**
 * Response from CreateSession containing the session handle.
 *
 * @property sessionHandle Object path for the created session.
 */
data class CreateSessionResponse(
    val sessionHandle: DBusPath
)
