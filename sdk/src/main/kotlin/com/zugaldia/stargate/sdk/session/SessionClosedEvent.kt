package com.zugaldia.stargate.sdk.session

import org.freedesktop.dbus.DBusPath
import org.freedesktop.dbus.types.Variant

/**
 * Event emitted when a portal session is closed.
 *
 * @property sessionHandle The handle of the session that was closed.
 * @property details Additional details about why the session was closed.
 */
data class SessionClosedEvent(
    val sessionHandle: DBusPath,
    val details: Map<String, Variant<*>>
)
