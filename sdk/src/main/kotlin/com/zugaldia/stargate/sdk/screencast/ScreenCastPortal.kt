package com.zugaldia.stargate.sdk.screencast

import com.zugaldia.stargate.sdk.BUS_NAME
import com.zugaldia.stargate.sdk.EMPTY_OPTIONS
import com.zugaldia.stargate.sdk.OBJECT_PATH
import com.zugaldia.stargate.sdk.OPTION_HANDLE_TOKEN
import com.zugaldia.stargate.sdk.OPTION_PERSIST_MODE
import com.zugaldia.stargate.sdk.OPTION_RESTORE_TOKEN
import com.zugaldia.stargate.sdk.OPTION_TYPES
import com.zugaldia.stargate.sdk.PersistMode
import com.zugaldia.stargate.sdk.RESULT_RESTORE_TOKEN
import com.zugaldia.stargate.sdk.generateToken
import com.zugaldia.stargate.sdk.request.awaitPortalResponse
import com.zugaldia.stargate.sdk.session.CreateSessionResponse
import com.zugaldia.stargate.sdk.session.PortalSession
import com.zugaldia.stargate.sdk.session.SessionClosedEvent
import kotlinx.coroutines.flow.Flow
import org.freedesktop.dbus.DBusPath
import org.freedesktop.dbus.FileDescriptor
import org.freedesktop.dbus.connections.impl.DBusConnection
import org.freedesktop.dbus.types.UInt32
import org.freedesktop.dbus.types.Variant
import org.freedesktop.portal.ScreenCast

/**
 * Wrapper around the org.freedesktop.portal.ScreenCast D-Bus interface.
 * Provides convenient methods to capture screen content via PipeWire streams.
 *
 * This portal maintains an active session internally. After calling [startSession],
 * the PipeWire remote can be opened with [openPipeWireRemote].
 */
class ScreenCastPortal(private val connection: DBusConnection) {

    private val screenCast: ScreenCast =
        connection.getRemoteObject(BUS_NAME, OBJECT_PATH, ScreenCast::class.java)

    private val session = PortalSession(connection)

    /**
     * The currently active session handle, set automatically by [startSession].
     */
    val activeSession: DBusPath?
        get() = session.active

    /**
     * Clears the active session.
     */
    fun clearSession() {
        session.clear()
    }

    /**
     * Returns a Flow that emits when the active session is closed.
     *
     * @return Flow of session closed events.
     */
    fun observeSessionClosed(): Flow<SessionClosedEvent> = session.observeClosed()

    /**
     * Returns the interface version.
     */
    val version: Int
        get() = screenCast.getVersion().toInt()

    /**
     * Returns the available source types as a set of [SourceType] values.
     */
    val availableSourceTypes: Set<SourceType>
        get() = SourceType.fromBitmask(screenCast.availableSourceTypes)

    /**
     * Returns the available cursor modes as a set of [CursorMode] values.
     */
    val availableCursorModes: Set<CursorMode>
        get() = CursorMode.fromBitmask(screenCast.availableCursorModes)

    /**
     * Creates a new screencast session.
     *
     * @return Result containing [CreateSessionResponse] with the session handle.
     */
    suspend fun createSession(): Result<CreateSessionResponse> = session.createSession(
        call = { options -> screenCast.CreateSession(options) }
    )

    /**
     * Select sources to capture for the given session. This must be called before starting
     * the session, and an application may only attempt to select sources once per session.
     *
     * Passing invalid input will cause the session to be closed.
     *
     * @param sessionHandle Object path for the session created via [createSession].
     * @param types Set of source types to allow the user to select. Default is MONITOR.
     * @param multiple Whether to allow selecting multiple sources.
     * @param cursorMode How the cursor should appear in the stream.
     * @param restoreToken Optional token to restore a previous session (version 4+).
     * @param persistMode How this session should persist (version 4+).
     * @return Result indicating success or failure.
     */
    suspend fun selectSources(
        sessionHandle: DBusPath,
        types: Set<SourceType>? = null,
        multiple: Boolean? = null,
        cursorMode: CursorMode? = null,
        restoreToken: String? = null,
        persistMode: PersistMode? = null
    ): Result<Unit> {
        val options = buildMap<String, Variant<*>> {
            put(OPTION_HANDLE_TOKEN, Variant(generateToken()))
            types?.let { put(OPTION_TYPES, Variant(SourceType.toBitmask(it))) }
            multiple?.let { put(OPTION_MULTIPLE, Variant(it)) }
            cursorMode?.let { put(OPTION_CURSOR_MODE, Variant(UInt32(cursorMode.value.toLong()))) }
            restoreToken?.let { put(OPTION_RESTORE_TOKEN, Variant(it)) }
            persistMode?.let { put(OPTION_PERSIST_MODE, Variant(it.value)) }
        }

        return awaitPortalResponse(
            connection = connection,
            methodName = "SelectSources",
            call = { screenCast.SelectSources(sessionHandle, options) },
            parseSuccess = { }
        )
    }

    /**
     * Start the screencast session. This will typically present a dialog for the user
     * to confirm the selection configured via [selectSources].
     *
     * A screencast session may only be started after having selected sources, and
     * an application can only attempt to start a session once.
     *
     * @param sessionHandle Object path for the session created via [createSession].
     * @param parentWindow Identifier for the application window (see Window Identifiers).
     *                     Use empty string if no parent window.
     * @return Result containing [StartResponse] with the selected PipeWire streams.
     */
    suspend fun start(
        sessionHandle: DBusPath,
        parentWindow: String = ""
    ): Result<StartResponse> {
        val options = mapOf(OPTION_HANDLE_TOKEN to Variant(generateToken()))

        return awaitPortalResponse(
            connection = connection,
            methodName = "Start",
            call = { screenCast.Start(sessionHandle, parentWindow, options) },
            parseSuccess = { results ->
                StartResponse(
                    streams = parseStreamsResult(results),
                    restoreToken = results[RESULT_RESTORE_TOKEN]?.value as? String
                )
            }
        )
    }

    /**
     * Convenience function that performs the complete screencast session flow:
     * creates a session, selects sources, and starts the session.
     *
     * @param types Set of source types to allow the user to select.
     * @param multiple Whether to allow selecting multiple sources.
     * @param cursorMode How the cursor should appear in the stream.
     * @param restoreToken Optional token to restore a previous session (version 4+).
     * @param persistMode How this session should persist (version 4+).
     * @param parentWindow Identifier for the application window. Use empty string if no parent window.
     * @return Result containing [StartResponse] with the selected PipeWire streams.
     */
    @Suppress("ReturnCount")
    suspend fun startSession(
        types: Set<SourceType>? = null,
        multiple: Boolean? = null,
        cursorMode: CursorMode? = null,
        restoreToken: String? = null,
        persistMode: PersistMode? = null,
        parentWindow: String = ""
    ): Result<StartResponse> {
        // Step 1: Create the session
        val sessionHandle = createSession().getOrElse { return Result.failure(it) }.sessionHandle
        session.set(sessionHandle)

        // Step 2: Select sources
        selectSources(sessionHandle, types, multiple, cursorMode, restoreToken, persistMode).getOrElse {
            clearSession()
            return Result.failure(it)
        }

        // Step 3: Start the session
        return start(sessionHandle, parentWindow).onFailure {
            clearSession()
        }
    }

    /**
     * Opens a PipeWire remote for the given session, returning a file descriptor
     * that can be passed to a PipeWire client to access the stream.
     *
     * Must be called after [start] succeeds.
     *
     * @param sessionHandle Object path for the session created via [createSession].
     * @return Result containing a file descriptor for the PipeWire remote.
     */
    fun openPipeWireRemote(sessionHandle: DBusPath): Result<FileDescriptor> = runCatching {
        screenCast.OpenPipeWireRemote(sessionHandle, EMPTY_OPTIONS)
    }

    /**
     * Opens a PipeWire remote using the active session handle.
     *
     * Must be called after [startSession] succeeds.
     *
     * @return Result containing a file descriptor for the PipeWire remote.
     */
    fun openPipeWireRemote(): Result<FileDescriptor> = session.withSession { sessionHandle ->
        screenCast.OpenPipeWireRemote(sessionHandle, EMPTY_OPTIONS)
    }

    @Suppress("UNCHECKED_CAST")
    private fun parseStreamsResult(results: Map<String, Variant<*>>): List<StreamInfo> {
        val streamsRaw = results[RESULT_STREAMS]?.value as? List<*> ?: return emptyList()
        return streamsRaw.mapNotNull { item ->
            val struct = item as? Array<*> ?: return@mapNotNull null
            val nodeId = struct.getOrNull(0) as? UInt32 ?: return@mapNotNull null
            val props = struct.getOrNull(1) as? Map<String, Variant<*>> ?: emptyMap()
            StreamInfo(nodeId = nodeId, properties = props)
        }
    }
}
