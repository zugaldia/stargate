package com.zugaldia.stargate.sdk.remotedesktop

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
 * Option key for the finish flag in NotifyPointerAxis.
 * If true, indicates the last axis event in a series (e.g., fingers lifted from touchpad).
 */
const val OPTION_FINISH = "finish"
