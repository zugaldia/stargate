package com.zugaldia.stargate.sdk.remotedesktop

/**
 * Option key for device types bitmask.
 * Used in RemoteDesktop.SelectDevices.
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
 * Result key for devices bitmask returned in Start response.
 */
const val RESULT_DEVICES = "devices"

/**
 * Result key for clipboard enabled flag returned in Start response.
 * Since version 2.
 */
const val RESULT_CLIPBOARD_ENABLED = "clipboard_enabled"

/**
 * Result key for restore token returned in Start response.
 * Since version 2.
 */
const val RESULT_RESTORE_TOKEN = "restore_token"

/**
 * Option key for the finish flag in NotifyPointerAxis.
 * If true, indicates the last axis event in a series (e.g., fingers lifted from touchpad).
 */
const val OPTION_FINISH = "finish"
