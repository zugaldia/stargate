package com.zugaldia.stargate.app.remotedesktop

import com.zugaldia.stargate.sdk.remotedesktop.DeviceType

data class RemoteDesktopState(
    val isLoading: Boolean = false,
    val isSessionActive: Boolean = false,
    val error: String? = null,
    val countdownSeconds: Int? = null,
    val isTyping: Boolean = false,
    val devices: Set<DeviceType> = emptySet(),
    val clipboardEnabled: Boolean? = null,
    val restoreToken: String? = null
)
