package com.zugaldia.stargate.sdk

import org.freedesktop.dbus.types.Variant

/**
 * D-Bus bus name for the desktop portal.
 */
const val BUS_NAME = "org.freedesktop.portal.Desktop"

/**
 * D-Bus object path for the desktop portal.
 */
const val OBJECT_PATH = "/org/freedesktop/portal/desktop"

/**
 * Option key for the request handle token.
 * Used to identify the request object path.
 */
const val OPTION_HANDLE_TOKEN = "handle_token"

/**
 * Option key for the session handle token.
 * Used in portal CreateSession methods.
 */
const val OPTION_SESSION_HANDLE_TOKEN = "session_handle_token"

/**
 * Result key for the session handle returned in responses.
 */
const val RESULT_SESSION_HANDLE = "session_handle"

/**
 * Option key for types bitmask (device types, source types, etc.).
 * Used in SelectDevices, SelectSources, and similar portal methods.
 */
const val OPTION_TYPES = "types"

/**
 * Option key for restore token.
 * Used to restore a previous session.
 */
const val OPTION_RESTORE_TOKEN = "restore_token"

/**
 * Option key for persist mode.
 * Controls how long permissions persist.
 */
const val OPTION_PERSIST_MODE = "persist_mode"

/**
 * Result key for restore token returned in Start response.
 */
const val RESULT_RESTORE_TOKEN = "restore_token"

/**
 * Empty D-Bus options map, reusable across portals.
 */
val EMPTY_OPTIONS = emptyMap<String, Variant<*>>()
