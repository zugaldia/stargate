package com.zugaldia.stargate.sdk.globalshortcuts

/**
 * Represents a global shortcut definition.
 *
 * @property id Unique identifier for the shortcut
 * @property description Human-readable description of the shortcut action.
 * @property preferredTrigger Preferred keyboard trigger
 */
data class Shortcut(
    val id: String,
    val description: String,
    val preferredTrigger: String // See https://specifications.freedesktop.org/shortcuts/latest/
)
