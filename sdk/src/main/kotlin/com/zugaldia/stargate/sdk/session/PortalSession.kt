package com.zugaldia.stargate.sdk.session

import com.zugaldia.stargate.sdk.OPTION_HANDLE_TOKEN
import com.zugaldia.stargate.sdk.OPTION_SESSION_HANDLE_TOKEN
import com.zugaldia.stargate.sdk.RESULT_SESSION_HANDLE
import com.zugaldia.stargate.sdk.generateToken
import com.zugaldia.stargate.sdk.request.awaitPortalResponse
import org.apache.logging.log4j.LogManager
import org.freedesktop.dbus.DBusPath
import org.freedesktop.dbus.connections.impl.DBusConnection
import org.freedesktop.dbus.types.Variant

/**
 * Manages session state for portals that require an active session.
 */
class PortalSession {
    private val logger = LogManager.getLogger(PortalSession::class.java)

    /**
     * The currently active session handle.
     */
    var active: DBusPath? = null
        private set

    /**
     * Sets the active session handle.
     *
     * @param path The session handle path returned from CreateSession.
     */
    fun set(path: DBusPath) {
        active = path
    }

    /**
     * Clears the active session.
     */
    fun clear() {
        active = null
    }

    /**
     * Executes a block with the active session, returning a [Result].
     *
     * @param block The operation to perform with the session handle.
     * @return [Result.success] with the block's return value, or [Result.failure]
     *         if no session is active or the block throws an exception.
     */
    inline fun <T> withSession(block: (DBusPath) -> T): Result<T> {
        val session = active ?: return Result.failure(IllegalStateException("No active session."))
        return runCatching { block(session) }
    }

    /**
     * Creates a new portal session using the standard CreateSession D-Bus method.
     *
     * @param connection The D-Bus connection.
     * @param logger Logger for debugging.
     * @param call Lambda that calls CreateSession on the specific portal interface.
     * @return Result containing [CreateSessionResponse] with the session handle.
     */
    suspend fun createSession(
        connection: DBusConnection,
        call: (Map<String, Variant<*>>) -> DBusPath
    ): Result<CreateSessionResponse> {
        val handleToken = generateToken()
        val sessionHandleToken = generateToken()
        logger.info("CreateSession (handleToken={}, sessionHandleToken={})", handleToken, sessionHandleToken)
        val options = mapOf(
            OPTION_HANDLE_TOKEN to Variant(handleToken),
            OPTION_SESSION_HANDLE_TOKEN to Variant(sessionHandleToken)
        )

        return awaitPortalResponse(
            connection = connection,
            methodName = "CreateSession",
            call = { call(options) },
            parseSuccess = { results ->
                val sessionHandlePath = results[RESULT_SESSION_HANDLE]?.value as? String
                CreateSessionResponse(sessionHandle = DBusPath(sessionHandlePath))
            }
        )
    }
}
