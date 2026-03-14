package com.zugaldia.stargate.sdk.openuri

import com.zugaldia.stargate.sdk.BUS_NAME
import com.zugaldia.stargate.sdk.OBJECT_PATH
import com.zugaldia.stargate.sdk.OPTION_HANDLE_TOKEN
import com.zugaldia.stargate.sdk.generateToken
import com.zugaldia.stargate.sdk.request.awaitPortalResponse
import org.freedesktop.dbus.connections.impl.DBusConnection
import org.freedesktop.dbus.types.Variant
import org.freedesktop.portal.OpenURI

/**
 * Wrapper around the org.freedesktop.portal.OpenURI D-Bus interface.
 * Provides methods to open URIs in the user's preferred application.
 */
class OpenUriPortal(private val connection: DBusConnection) {

    private val openUri: OpenURI =
        connection.getRemoteObject(BUS_NAME, OBJECT_PATH, OpenURI::class.java)

    /**
     * Returns the interface version.
     */
    val version: Int
        get() = openUri.getversion().toInt()

    /**
     * Opens a URI (e.g. https://example.com) in the user's preferred application.
     *
     * Note: file:// URIs are not supported by this method; use OpenFile instead.
     *
     * @param uri The URI to open.
     * @param parentWindow An identifier for the application window (typically empty string).
     * @param ask Whether to prompt the user to choose an application (requires portal v3+).
     */
    suspend fun openUri(
        uri: String,
        parentWindow: String = "",
        ask: Boolean = false
    ): Result<Unit> {
        val options = buildMap<String, Variant<*>> {
            put(OPTION_HANDLE_TOKEN, Variant(generateToken()))
            if (ask) {
                put(OPTION_ASK, Variant(true))
            }
        }

        return awaitPortalResponse(
            connection = connection,
            methodName = "OpenURI",
            call = { openUri.OpenURI(parentWindow, uri, options) },
            parseSuccess = { }
        )
    }

    /**
     * Checks whether a given URI scheme (e.g. "https") is supported.
     * Requires portal version 5 or later.
     *
     * @param scheme The URI scheme to check.
     */
    @Suppress("TooGenericExceptionCaught")
    fun isSchemeSupported(scheme: String): Result<Boolean> {
        try {
            val supported = openUri.SchemeSupported(scheme, emptyMap())
            return Result.success(supported)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}
