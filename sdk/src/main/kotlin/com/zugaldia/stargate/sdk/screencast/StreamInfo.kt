package com.zugaldia.stargate.sdk.screencast

import org.freedesktop.dbus.types.UInt32
import org.freedesktop.dbus.types.Variant

/**
 * Represents a PipeWire stream returned by ScreenCast.Start.
 *
 * @property nodeId The PipeWire node identifier for this stream.
 * @property properties Additional stream properties (e.g., position, size, source_type).
 */
data class StreamInfo(
    val nodeId: UInt32,
    val properties: Map<String, Variant<*>>
)
