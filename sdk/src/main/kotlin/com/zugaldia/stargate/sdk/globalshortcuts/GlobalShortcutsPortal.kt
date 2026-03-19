package com.zugaldia.stargate.sdk.globalshortcuts

import com.zugaldia.stargate.sdk.BUS_NAME
import com.zugaldia.stargate.sdk.OBJECT_PATH
import com.zugaldia.stargate.sdk.OPTION_HANDLE_TOKEN
import com.zugaldia.stargate.sdk.generateToken
import com.zugaldia.stargate.sdk.request.awaitPortalResponse
import com.zugaldia.stargate.sdk.session.CreateSessionResponse
import com.zugaldia.stargate.sdk.session.PortalSession
import com.zugaldia.stargate.sdk.session.SessionClosedEvent
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import org.slf4j.LoggerFactory
import org.freedesktop.dbus.DBusPath
import org.freedesktop.dbus.connections.impl.DBusConnection
import org.freedesktop.dbus.types.Variant
import org.freedesktop.portal.BindShortcutsShortcutsStruct
import org.freedesktop.portal.GlobalShortcuts

/**
 * Wrapper around the org.freedesktop.portal.GlobalShortcuts D-Bus interface.
 * Provides convenient methods to register and listen for global keyboard shortcuts.
 *
 * This portal maintains an active session internally. After calling [createSession],
 * shortcuts can be bound using [bindShortcuts] and activation events can be observed
 * via [activations].
 */
class GlobalShortcutsPortal(private val connection: DBusConnection) {

    private val logger = LoggerFactory.getLogger(GlobalShortcutsPortal::class.java)

    private val globalShortcuts: GlobalShortcuts =
        connection.getRemoteObject(BUS_NAME, OBJECT_PATH, GlobalShortcuts::class.java)

    private val session = PortalSession(connection)

    /**
     * The currently active session handle, set automatically by [createSession].
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
     * This flow emits [SessionClosedEvent] with details about why the session was closed.
     * The flow will only emit events for the current [activeSession].
     *
     * @return Flow of session closed events.
     * @throws IllegalStateException if no active session exists when the flow is collected.
     */
    fun observeSessionClosed(): Flow<SessionClosedEvent> = session.observeClosed()

    /**
     * Returns the interface version.
     */
    val version: Int
        get() = globalShortcuts.getVersion().toInt()

    /**
     * Creates a new global shortcuts session and sets it as the active session.
     *
     * @return Result containing [CreateSessionResponse] with the session handle.
     */
    suspend fun createSession(): Result<CreateSessionResponse> = session.createSession(
        call = { options -> globalShortcuts.CreateSession(options) }
    ).onSuccess { response ->
        session.set(response.sessionHandle)
    }

    /**
     * Bind shortcuts to the active session. This allows the application to receive
     * activation events when the user triggers the shortcuts.
     *
     * The first time a shortcut is bound, the portal presents a system dialog for the user
     * to review and confirm. Subsequent calls with the same shortcut ID bind automatically
     * without user interaction, as the portal remembers the user's prior approval.
     *
     * Shortcuts must be bound in every new session — [listShortcuts] will return an empty
     * list until this method is called, even if shortcuts were bound in a previous session.
     *
     * Uses the session handle from [createSession] automatically.
     *
     * @param shortcuts List of shortcuts to bind.
     * @param parentWindow Identifier for the application window. Use empty string if no parent window.
     * @return Result containing the list of bound shortcuts with their assigned triggers.
     */
    suspend fun bindShortcuts(
        shortcuts: List<Shortcut>,
        parentWindow: String = ""
    ): Result<List<BoundShortcut>> {
        val sessionHandle = activeSession
            ?: return Result.failure(IllegalStateException("No active session."))
        return bindShortcuts(sessionHandle, shortcuts, parentWindow)
    }

    /**
     * Bind shortcuts to the session. This allows the application to receive
     * activation events when the user triggers the shortcuts.
     *
     * The first time a shortcut is bound, the portal presents a system dialog for the user
     * to review and confirm. Subsequent calls with the same shortcut ID bind automatically
     * without user interaction, as the portal remembers the user's prior approval.
     *
     * Shortcuts must be bound in every new session — [listShortcuts] will return an empty
     * list until this method is called, even if shortcuts were bound in a previous session.
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
     * List all shortcuts bound to the active session.
     *
     * Uses the session handle from [createSession] automatically.
     *
     * @return Result containing the list of bound shortcuts.
     */
    suspend fun listShortcuts(): Result<List<BoundShortcut>> {
        val sessionHandle = activeSession
            ?: return Result.failure(IllegalStateException("No active session."))
        return listShortcuts(sessionHandle)
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
     * Opens the system dialog for configuring shortcuts using the active session.
     *
     * Uses the session handle from [createSession] automatically.
     *
     * @param parentWindow Identifier for the application window. Use empty string if no parent window.
     * @return Result indicating success or failure.
     */
    fun configureShortcuts(
        parentWindow: String = ""
    ): Result<Unit> {
        val sessionHandle = activeSession
            ?: return Result.failure(IllegalStateException("No active session."))
        return configureShortcuts(sessionHandle, parentWindow)
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
            // D-Bus structs arrive as Object[] arrays from the signal response
            val struct = (item as? Array<*>)?.toList() ?: return@mapNotNull null
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
