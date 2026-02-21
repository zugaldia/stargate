package com.zugaldia.stargate.app.settings

import com.zugaldia.stargate.sdk.settings.AccentColor
import com.zugaldia.stargate.sdk.settings.ColorScheme
import com.zugaldia.stargate.sdk.settings.Contrast
import com.zugaldia.stargate.sdk.settings.ReducedMotion

data class SettingsState(
    val version: Int? = null,
    val colorScheme: ColorScheme? = null,
    val accentColor: AccentColor? = null,
    val contrast: Contrast? = null,
    val reducedMotion: ReducedMotion? = null
)
