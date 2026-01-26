package com.zugaldia.stargate.sdk.settings

import com.zugaldia.stargate.sdk.BUS_NAME
import com.zugaldia.stargate.sdk.OBJECT_PATH
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import org.freedesktop.dbus.connections.impl.DBusConnection
import org.freedesktop.dbus.types.UInt32
import org.freedesktop.portal.Settings

/**
 * Namespace for appearance-related settings.
 */
private const val NAMESPACE_APPEARANCE = "org.freedesktop.appearance"

/**
 * Keys for appearance settings.
 */
private const val KEY_COLOR_SCHEME = "color-scheme"
private const val KEY_ACCENT_COLOR = "accent-color"
private const val KEY_CONTRAST = "contrast"
private const val KEY_REDUCED_MOTION = "reduced-motion"

/**
 * Wrapper around the org.freedesktop.portal.Settings D-Bus interface.
 * Provides convenient methods to read appearance settings with proper types
 * and subscribe to setting changes.
 */
class SettingsPortal(private val connection: DBusConnection) {

    private val settings: Settings =
        connection.getRemoteObject(BUS_NAME, OBJECT_PATH, Settings::class.java)

    /**
     * Returns the interface version.
     */
    val version: Int
        get() = settings.getversion().toInt()

    /**
     * Reads the user's preferred color scheme.
     */
    @Suppress("TooGenericExceptionCaught")
    fun getColorScheme(): Result<ColorScheme> {
        try {
            val variant = settings.ReadOne(NAMESPACE_APPEARANCE, KEY_COLOR_SCHEME)
            val value = variant.value as UInt32
            return Result.success(ColorScheme.fromValue(value))
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    /**
     * Reads the user's preferred accent color.
     */
    @Suppress("UNCHECKED_CAST", "TooGenericExceptionCaught")
    fun getAccentColor(): Result<AccentColor> {
        try {
            val variant = settings.ReadOne(NAMESPACE_APPEARANCE, KEY_ACCENT_COLOR)
            val values = variant.value as List<Double>
            return Result.success(AccentColor(values[0], values[1], values[2]))
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    /**
     * Reads the user's preferred contrast setting.
     */
    @Suppress("TooGenericExceptionCaught")
    fun getContrast(): Result<Contrast> {
        try {
            val variant = settings.ReadOne(NAMESPACE_APPEARANCE, KEY_CONTRAST)
            val value = variant.value as UInt32
            return Result.success(Contrast.fromValue(value))
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    /**
     * Reads the user's reduced motion preference.
     */
    @Suppress("TooGenericExceptionCaught")
    fun getReducedMotion(): Result<ReducedMotion> {
        try {
            val variant = settings.ReadOne(NAMESPACE_APPEARANCE, KEY_REDUCED_MOTION)
            val value = variant.value as UInt32
            return Result.success(ReducedMotion.fromValue(value))
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    /**
     * A flow that emits when the color scheme changes.
     */
    val colorSchemeChanges: Flow<ColorScheme> = settingFlow(KEY_COLOR_SCHEME) { signal ->
        ColorScheme.fromValue(signal.value.value as UInt32)
    }

    /**
     * A flow that emits when the accent color changes.
     */
    @Suppress("UNCHECKED_CAST")
    val accentColorChanges: Flow<AccentColor> = settingFlow(KEY_ACCENT_COLOR) { signal ->
        val values = signal.value.value as List<Double>
        AccentColor(values[0], values[1], values[2])
    }

    /**
     * A flow that emits when the contrast setting changes.
     */
    val contrastChanges: Flow<Contrast> = settingFlow(KEY_CONTRAST) { signal ->
        Contrast.fromValue(signal.value.value as UInt32)
    }

    /**
     * A flow that emits when the reduced motion preference changes.
     */
    val reducedMotionChanges: Flow<ReducedMotion> = settingFlow(KEY_REDUCED_MOTION) { signal ->
        ReducedMotion.fromValue(signal.value.value as UInt32)
    }

    @Suppress("TooGenericExceptionCaught")
    private fun <T> settingFlow(key: String, transform: (Settings.SettingChanged) -> T): Flow<T> =
        callbackFlow {
            val handler: (Settings.SettingChanged) -> Unit = { signal ->
                if (signal.namespace == NAMESPACE_APPEARANCE && signal.key == key) {
                    trySend(transform(signal))
                }
            }
            val signalHandler = connection.addSigHandler(
                Settings.SettingChanged::class.java,
                settings,
                handler
            )
            awaitClose { signalHandler.close() }
        }
}
