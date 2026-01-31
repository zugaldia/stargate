package com.zugaldia.stargate.sdk.globalshortcuts

/**
 * Represents a bound shortcut as returned by the portal.
 *
 * @property id Unique identifier for the shortcut.
 * @property description Human-readable description of the shortcut action.
 * @property triggerDescription Human-readable description of the assigned trigger (e.g., "Ctrl+C").
 */
data class BoundShortcut(
    val id: String,
    val description: String?,
    val triggerDescription: String?
)
