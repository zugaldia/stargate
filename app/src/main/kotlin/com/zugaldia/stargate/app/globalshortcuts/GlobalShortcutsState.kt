package com.zugaldia.stargate.app.globalshortcuts

import com.zugaldia.stargate.sdk.globalshortcuts.BoundShortcut
import com.zugaldia.stargate.sdk.globalshortcuts.ShortcutActivation

data class GlobalShortcutsState(
    val isLoading: Boolean = false,
    val isSessionActive: Boolean = false,
    val shortcuts: List<BoundShortcut>? = null,
    val activations: List<ShortcutActivation> = emptyList(),
    val error: String? = null
)
