package com.zugaldia.stargate.sdk.globalshortcuts

/**
 * Represents a shortcut activation or deactivation event.
 *
 * @property shortcutId The ID of the shortcut that was activated/deactivated.
 * @property timestamp Timestamp of the event in microseconds.
 * @property activated True if the shortcut was activated, false if deactivated.
 */
data class ShortcutActivation(
    val shortcutId: String,
    val timestamp: Long,
    val activated: Boolean
)
