package com.zugaldia.stargate.sdk.globalshortcuts

import com.zugaldia.stargate.sdk.BUS_NAME
import com.zugaldia.stargate.sdk.OBJECT_PATH
import com.zugaldia.stargate.sdk.OPTION_HANDLE_TOKEN
import com.zugaldia.stargate.sdk.generateToken
import com.zugaldia.stargate.sdk.request.awaitPortalResponse
import com.zugaldia.stargate.sdk.session.CreateSessionResponse
import com.zugaldia.stargate.sdk.session.PortalSession
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import org.apache.logging.log4j.LogManager
import org.freedesktop.dbus.DBusPath
import org.freedesktop.dbus.connections.impl.DBusConnection
import org.freedesktop.dbus.types.Variant
import org.freedesktop.portal.BindShortcutsShortcutsStruct
import org.freedesktop.portal.GlobalShortcuts

/**
 * Wrapper around the org.freedesktop.portal.GlobalShortcuts D-Bus interface.
 * Provides convenient methods to register and listen for global keyboard shortcuts.
 *
 * This portal maintains an active session internally. After calling [startSession],
 * shortcuts can be bound using [bindShortcuts] and activation events can be observed
 * via [activations].
 */
@Suppress("TooManyFunctions")
class GlobalShortcutsPortal(private val connection: DBusConnection) {

    private val logger = LogManager.getLogger(GlobalShortcutsPortal::class.java)

    private val globalShortcuts: GlobalShortcuts =
        connection.getRemoteObject(BUS_NAME, OBJECT_PATH, GlobalShortcuts::class.java)

    private val session = PortalSession()

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
     * Returns the interface version.
     */
    val version: Int
        get() = globalShortcuts.getversion().toInt()

    /**
     * Creates a new global shortcuts session.
     *
     * @return Result containing [CreateSessionResponse] with the session handle.
     */
    suspend fun createSession(): Result<CreateSessionResponse> = session.createSession(
        connection = connection,
        call = { options -> globalShortcuts.CreateSession(options) }
    )

    /**
     * Bind shortcuts to the session. This allows the application to receive
     * activation events when the user triggers the shortcuts.
     *
     * @param sessionHandle Object path for the session created via [createSession].
     * @param shortcuts List of shortcuts to bind.
     * @param parentWindow Identifier for the application window. Use empty string if no parent window.
     * @return Result containing the list of bound shortcuts with their assigned triggers.
     */
    suspend fun bindShortcuts(
        sessionHandle: DBusPath,
        shortcuts: List<Shortcut>,
        parentWindow: String = ""
    ): Result<List<BoundShortcut>> {
        val options = mapOf(OPTION_HANDLE_TOKEN to Variant(generateToken()))
        val shortcutsStructs = shortcuts.map { shortcut ->
            val props = buildMap<String, Variant<*>> {
                put(SHORTCUT_DESCRIPTION, Variant(shortcut.description))
                put(SHORTCUT_PREFERRED_TRIGGER, Variant(shortcut.preferredTrigger))
            }
            BindShortcutsShortcutsStruct(shortcut.id, props)
        }

        return awaitPortalResponse(
            connection = connection,
            methodName = "BindShortcuts",
            call = { globalShortcuts.BindShortcuts(sessionHandle, shortcutsStructs, parentWindow, options) },
            parseSuccess = { results -> parseShortcutsResult(results) }
        )
    }

    /**
     * List all shortcuts bound to the session.
     *
     * @param sessionHandle Object path for the session created via [createSession].
     * @return Result containing the list of bound shortcuts.
     */
    suspend fun listShortcuts(
        sessionHandle: DBusPath
    ): Result<List<BoundShortcut>> {
        val options = mapOf(OPTION_HANDLE_TOKEN to Variant(generateToken()))

        return awaitPortalResponse(
            connection = connection,
            methodName = "ListShortcuts",
            call = { globalShortcuts.ListShortcuts(sessionHandle, options) },
            parseSuccess = { results -> parseShortcutsResult(results) }
        )
    }

    /**
     * Opens the system dialog for configuring shortcuts.
     *
     * @param sessionHandle Object path for the session created via [createSession].
     * @param parentWindow Identifier for the application window. Use empty string if no parent window.
     * @return Result indicating success or failure.
     */
    fun configureShortcuts(
        sessionHandle: DBusPath,
        parentWindow: String = ""
    ): Result<Unit> = runCatching {
        val options = mapOf(
            OPTION_HANDLE_TOKEN to Variant(generateToken()),
            OPTION_ACTIVATION_TOKEN to Variant(generateToken())
        )
        globalShortcuts.ConfigureShortcuts(sessionHandle, parentWindow, options)
    }

    /**
     * Convenience function that creates a session and binds shortcuts in one call.
     *
     * @param shortcuts List of shortcuts to bind.
     * @param parentWindow Identifier for the application window. Use empty string if no parent window.
     * @return Result containing the list of bound shortcuts with their assigned triggers.
     */
    @Suppress("ReturnCount")
    suspend fun startSession(
        shortcuts: List<Shortcut>,
        parentWindow: String = ""
    ): Result<List<BoundShortcut>> {
        // Step 1: Create the session
        val sessionHandle = createSession().getOrElse { return Result.failure(it) }.sessionHandle
        session.set(sessionHandle)

        // Step 2: Bind shortcuts
        return bindShortcuts(sessionHandle, shortcuts, parentWindow).onFailure {
            clearSession()
        }
    }

    /**
     * Returns a Flow of shortcut activation events for the active session.
     *
     * This flow emits [ShortcutActivation] events when shortcuts are activated or deactivated.
     * The flow will only emit events for the current [activeSession].
     *
     * @return Flow of shortcut activation events.
     */
    fun activations(): Flow<ShortcutActivation> = callbackFlow {
        val session = activeSession
        if (session == null) {
            close(IllegalStateException("No active session."))
            return@callbackFlow
        }

        val activatedHandler = connection.addSigHandler(GlobalShortcuts.Activated::class.java) { signal ->
            if (signal.sessionHandle == session) {
                val activation = ShortcutActivation(
                    shortcutId = signal.shortcutId,
                    timestamp = signal.timestamp.toLong(),
                    activated = true
                )
                trySend(activation)
            }
        }

        val deactivatedHandler = connection.addSigHandler(GlobalShortcuts.Deactivated::class.java) { signal ->
            if (signal.sessionHandle == session) {
                val activation = ShortcutActivation(
                    shortcutId = signal.shortcutId,
                    timestamp = signal.timestamp.toLong(),
                    activated = false
                )
                trySend(activation)
            }
        }

        awaitClose {
            activatedHandler.close()
            deactivatedHandler.close()
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun parseShortcutsResult(results: Map<String, Variant<*>>): List<BoundShortcut> {
        val shortcutsRaw = results[RESULT_SHORTCUTS]?.value as? List<*> ?: return emptyList()
        return shortcutsRaw.mapNotNull { item ->
            val struct = item as? List<*> ?: return@mapNotNull null
            val id = struct.getOrNull(0) as? String ?: return@mapNotNull null
            val props = struct.getOrNull(1) as? Map<String, Variant<*>> ?: emptyMap()
            BoundShortcut(
                id = id,
                description = props[SHORTCUT_DESCRIPTION]?.value as? String,
                triggerDescription = props[SHORTCUT_TRIGGER_DESCRIPTION]?.value as? String
            )
        }
    }
}
