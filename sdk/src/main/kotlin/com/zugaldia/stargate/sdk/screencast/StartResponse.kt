package com.zugaldia.stargate.sdk.screencast

/**
 * Response from ScreenCast.Start containing the selected streams.
 *
 * @property streams The list of PipeWire streams selected by the user.
 * @property restoreToken Token to restore this session later (version 4+).
 */
data class StartResponse(
    val streams: List<StreamInfo>,
    val restoreToken: String?
)
