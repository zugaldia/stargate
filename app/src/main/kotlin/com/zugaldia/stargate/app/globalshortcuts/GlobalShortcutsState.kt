package com.zugaldia.stargate.app.globalshortcuts

data class GlobalShortcutsState(
    val isLoading: Boolean = false,
    val isSessionActive: Boolean = false,
    val error: String? = null
)
