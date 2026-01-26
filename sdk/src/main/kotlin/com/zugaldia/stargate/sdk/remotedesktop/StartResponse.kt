package com.zugaldia.stargate.sdk.remotedesktop

/**
 * Response from RemoteDesktop.Start containing the selected devices and session info.
 *
 * @property devices The set of devices selected by the user.
 * @property clipboardEnabled Whether clipboard sharing was enabled (version 2+).
 * @property restoreToken Token to restore this session later (version 2+).
 */
data class StartResponse(
    val devices: Set<DeviceType>,
    val clipboardEnabled: Boolean?,
    val restoreToken: String?
)
