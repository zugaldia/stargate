package com.zugaldia.stargate.sdk.remotedesktop

import com.zugaldia.stargate.sdk.BUS_NAME
import com.zugaldia.stargate.sdk.OBJECT_PATH
import com.zugaldia.stargate.sdk.OPTION_HANDLE_TOKEN
import com.zugaldia.stargate.sdk.generateToken
import com.zugaldia.stargate.sdk.request.RequestResponse
import kotlinx.coroutines.suspendCancellableCoroutine
import org.apache.logging.log4j.LogManager
import org.freedesktop.dbus.DBusPath
import org.freedesktop.dbus.FileDescriptor
import org.freedesktop.dbus.connections.impl.DBusConnection
import org.freedesktop.dbus.types.UInt32
import org.freedesktop.dbus.types.Variant
import org.freedesktop.portal.RemoteDesktop
import org.freedesktop.portal.Request
import java.util.concurrent.atomic.AtomicReference
import kotlin.coroutines.resume

private val EMPTY_OPTIONS = emptyMap<String, Variant<*>>()

/**
 * Wrapper around the org.freedesktop.portal.RemoteDesktop D-Bus interface.
 * Provides convenient methods to access remote desktop properties and create sessions.
 *
 * This portal maintains an active session internally. After calling [startSession],
 * all input methods (pointer, keyboard, touch) will use the active session automatically.
 */
@Suppress("TooManyFunctions")
class RemoteDesktopPortal(private val connection: DBusConnection) {

    private val logger = LogManager.getLogger(RemoteDesktopPortal::class.java)

    private val remoteDesktop: RemoteDesktop =
        connection.getRemoteObject(BUS_NAME, OBJECT_PATH, RemoteDesktop::class.java)

    /**
     * The currently active session handle, set automatically by [startSession].
     */
    var activeSession: DBusPath? = null
        private set

    /**
     * Clears the active session.
     */
    fun clearSession() {
        activeSession = null
    }

    private inline fun <T> withSession(block: (DBusPath) -> T): Result<T> {
        val session = activeSession
            ?: return Result.failure(IllegalStateException("No active session. Call startSession() first."))
        return runCatching { block(session) }
    }

    /**
     * Returns the interface version.
     */
    val version: Int
        get() = remoteDesktop.getversion().toInt()

    /**
     * Returns the available device types as a bitmask.
     */
    val availableDeviceTypes: Set<DeviceType>
        get() = DeviceType.fromBitmask(remoteDesktop.availableDeviceTypes)

    /**
     * Creates a new remote desktop session.
     *
     * @return Result containing [CreateSessionResponse] with the session handle.
     */
    suspend fun createSession(): Result<CreateSessionResponse> =
        suspendCancellableCoroutine { continuation ->
            val requestPathRef = AtomicReference<String?>(null)
            var handler: AutoCloseable? = null
            handler = connection.addSigHandler(Request.Response::class.java) { signal ->
                val path = requestPathRef.get()
                if (path != null && signal.path == path) {
                    try {
                        when (val response = RequestResponse.fromValue(signal.response)) {
                            RequestResponse.SUCCESS -> {
                                val sessionHandlePath = signal.results[RESULT_SESSION_HANDLE]?.value as? String
                                val result = CreateSessionResponse(sessionHandle = DBusPath(sessionHandlePath))
                                continuation.resume(Result.success(result))
                            }
                            else -> {
                                val error = "CreateSession failed ($response)"
                                continuation.resume(Result.failure(IllegalStateException(error)))
                            }
                        }
                    } catch (@Suppress("TooGenericExceptionCaught") e: Exception) {
                        continuation.resume(Result.failure(e))
                    } finally {
                        handler?.close()
                    }
                }
            }

            val handleToken = generateToken()
            val sessionHandleToken = generateToken()
            logger.debug("createSession: handleToken={}, sessionHandleToken={}", handleToken, sessionHandleToken)
            val options = mapOf(
                OPTION_HANDLE_TOKEN to Variant(handleToken),
                OPTION_SESSION_HANDLE_TOKEN to Variant(sessionHandleToken)
            )

            val requestHandle = remoteDesktop.CreateSession(options)
            requestPathRef.set(requestHandle.path)
            continuation.invokeOnCancellation { handler?.close() }
        }

    /**
     * Select input devices to remote control.
     *
     * @param sessionHandle Object path for the session created via [createSession].
     * @param types Set of device types to request remote control of. Default is all available types.
     * @param restoreToken Optional token to restore a previous session (version 2+).
     * @param persistMode How this session should persist (version 2+).
     * @return Result indicating success or failure.
     */
    suspend fun selectDevices(
        sessionHandle: DBusPath,
        types: Set<DeviceType>? = null,
        restoreToken: String? = null,
        persistMode: PersistMode? = null
    ): Result<Unit> =
        suspendCancellableCoroutine { continuation ->
            val requestPathRef = AtomicReference<String?>(null)
            var handler: AutoCloseable? = null
            handler = connection.addSigHandler(Request.Response::class.java) { signal ->
                val path = requestPathRef.get()
                if (path != null && signal.path == path) {
                    try {
                        when (val response = RequestResponse.fromValue(signal.response)) {
                            RequestResponse.SUCCESS -> {
                                continuation.resume(Result.success(Unit))
                            }
                            else -> {
                                val error = "SelectDevices request failed ($response)"
                                continuation.resume(Result.failure(IllegalStateException(error)))
                            }
                        }
                    } catch (@Suppress("TooGenericExceptionCaught") e: Exception) {
                        continuation.resume(Result.failure(e))
                    } finally {
                        handler?.close()
                    }
                }
            }

            val options = buildMap<String, Variant<*>> {
                put(OPTION_HANDLE_TOKEN, Variant(generateToken()))
                types?.let { put(OPTION_TYPES, Variant(DeviceType.toBitmask(it))) }
                restoreToken?.let { put(OPTION_RESTORE_TOKEN, Variant(it)) }
                persistMode?.let { put(OPTION_PERSIST_MODE, Variant(it.value)) }
            }

            val requestHandle = remoteDesktop.SelectDevices(sessionHandle, options)
            requestPathRef.set(requestHandle.path)
            continuation.invokeOnCancellation { handler?.close() }
        }

    /**
     * Start the remote desktop session. This will typically result in the portal
     * presenting a dialog letting the user select what to share, including devices
     * and optionally screen content if screencast sources were selected.
     *
     * @param sessionHandle Object path for the session created via [createSession].
     * @param parentWindow Identifier for the application window (see Window Identifiers).
     *                     Use empty string if no parent window.
     * @return Result containing [StartResponse] with selected devices and session info.
     */
    suspend fun start(
        sessionHandle: DBusPath,
        parentWindow: String = ""
    ): Result<StartResponse> =
        suspendCancellableCoroutine { continuation ->
            val requestPathRef = AtomicReference<String?>(null)
            var handler: AutoCloseable? = null
            handler = connection.addSigHandler(Request.Response::class.java) { signal ->
                val path = requestPathRef.get()
                if (path != null && signal.path == path) {
                    try {
                        when (val response = RequestResponse.fromValue(signal.response)) {
                            RequestResponse.SUCCESS -> {
                                val devicesBitmask = signal.results[RESULT_DEVICES]?.value as? UInt32
                                val result = StartResponse(
                                    devices = devicesBitmask?.let { DeviceType.fromBitmask(it) } ?: emptySet(),
                                    clipboardEnabled = signal.results[RESULT_CLIPBOARD_ENABLED]?.value as? Boolean,
                                    restoreToken = signal.results[RESULT_RESTORE_TOKEN]?.value as? String
                                )
                                continuation.resume(Result.success(result))
                            }
                            else -> {
                                val error = "Start failed ($response)"
                                continuation.resume(Result.failure(IllegalStateException(error)))
                            }
                        }
                    } catch (@Suppress("TooGenericExceptionCaught") e: Exception) {
                        continuation.resume(Result.failure(e))
                    } finally {
                        handler?.close()
                    }
                }
            }

            val options = mapOf(OPTION_HANDLE_TOKEN to Variant(generateToken()))
            val requestHandle = remoteDesktop.Start(sessionHandle, parentWindow, options)
            requestPathRef.set(requestHandle.path)
            continuation.invokeOnCancellation { handler?.close() }
        }

    /**
     * Convenience function that performs the complete remote desktop session flow:
     * creates a session, selects devices, and starts the session.
     *
     * This combines [createSession], [selectDevices], and [start] into a single call.
     *
     * @param types Set of device types to request remote control of. Default is all available types.
     * @param restoreToken Optional token to restore a previous session (version 2+).
     * @param persistMode How this session should persist (version 2+).
     * @param parentWindow Identifier for the application window. Use empty string if no parent window.
     * @return Result containing [StartResponse] with selected devices and session info.
     */
    @Suppress("ReturnCount")
    suspend fun startSession(
        types: Set<DeviceType>? = null,
        restoreToken: String? = null,
        persistMode: PersistMode? = null,
        parentWindow: String = ""
    ): Result<StartResponse> {
        // Step 1: Create the session
        val sessionHandle = createSession().getOrElse { return Result.failure(it) }.sessionHandle
        activeSession = sessionHandle

        // Step 2: Select devices
        selectDevices(sessionHandle, types, restoreToken, persistMode).getOrElse {
            clearSession()
            return Result.failure(it)
        }

        // Step 3: Start the session
        return start(sessionHandle, parentWindow).onFailure {
            clearSession()
        }
    }

    // ========== Pointer Input Methods ==========

    /**
     * Notify about a relative pointer motion event.
     *
     * @param dx Relative movement on the x-axis.
     * @param dy Relative movement on the y-axis.
     * @return Result indicating success or failure.
     */
    fun notifyPointerMotion(dx: Double, dy: Double): Result<Unit> = withSession { session ->
        remoteDesktop.NotifyPointerMotion(session, EMPTY_OPTIONS, dx, dy)
    }

    /**
     * Notify about an absolute pointer motion event.
     *
     * @param stream The PipeWire stream node the coordinate is relative to.
     * @param x Pointer motion x coordinate.
     * @param y Pointer motion y coordinate.
     * @return Result indicating success or failure.
     */
    fun notifyPointerMotionAbsolute(stream: UInt32, x: Double, y: Double): Result<Unit> = withSession { session ->
        remoteDesktop.NotifyPointerMotionAbsolute(session, EMPTY_OPTIONS, stream, x, y)
    }

    /**
     * Notify about a pointer button event.
     *
     * @param button The pointer button (Linux evdev button codes).
     * @param state The button state.
     * @return Result indicating success or failure.
     */
    fun notifyPointerButton(button: Int, state: InputState): Result<Unit> = withSession { session ->
        remoteDesktop.NotifyPointerButton(session, EMPTY_OPTIONS, button, state.value)
    }

    /**
     * Notify about a pointer axis event (smooth scrolling, e.g., from touchpad).
     *
     * @param dx Relative axis movement on the x-axis.
     * @param dy Relative axis movement on the y-axis.
     * @param finish If true, this is the last axis event in a series (e.g., fingers lifted from touchpad).
     * @return Result indicating success or failure.
     */
    fun notifyPointerAxis(dx: Double, dy: Double, finish: Boolean = false): Result<Unit> = withSession { session ->
        val options = if (finish) { mapOf(OPTION_FINISH to Variant(finish)) } else { EMPTY_OPTIONS }
        remoteDesktop.NotifyPointerAxis(session, options, dx, dy)
    }

    /**
     * Notify about a discrete pointer axis event (e.g., mouse wheel click).
     *
     * @param axis The axis direction.
     * @param steps The number of steps scrolled.
     * @return Result indicating success or failure.
     */
    fun notifyPointerAxisDiscrete(axis: AxisDirection, steps: Int): Result<Unit> = withSession { session ->
        remoteDesktop.NotifyPointerAxisDiscrete(session, EMPTY_OPTIONS, axis.value, steps)
    }

    // ========== Keyboard Input Methods ==========

    /**
     * Notify about a keyboard keycode event.
     *
     * @param keycode The keyboard keycode (XKB keycode).
     * @param state The key state.
     * @return Result indicating success or failure.
     */
    fun notifyKeyboardKeycode(keycode: Int, state: InputState): Result<Unit> = withSession { session ->
        remoteDesktop.NotifyKeyboardKeycode(session, EMPTY_OPTIONS, keycode, state.value)
    }

    /**
     * Notify about a keyboard keysym event.
     *
     * @param keySym The keyboard keysym (XKB keysym).
     * @param state The key state.
     * @return Result indicating success or failure.
     */
    fun notifyKeyboardKeySym(keySym: Int, state: InputState): Result<Unit> = withSession { session ->
        remoteDesktop.NotifyKeyboardKeysym(session, EMPTY_OPTIONS, keySym, state.value)
    }

    // ========== Touch Input Methods ==========

    /**
     * Notify about a touch-down event.
     *
     * @param stream The PipeWire stream node the coordinate is relative to.
     * @param slot The touch slot where the touchpoint appeared.
     * @param x Touch down x coordinate.
     * @param y Touch down y coordinate.
     * @return Result indicating success or failure.
     */
    fun notifyTouchDown(stream: UInt32, slot: UInt32, x: Double, y: Double): Result<Unit> = withSession { session ->
        remoteDesktop.NotifyTouchDown(session, EMPTY_OPTIONS, stream, slot, x, y)
    }

    /**
     * Notify about a touch motion event.
     *
     * @param stream The PipeWire stream node the coordinate is relative to.
     * @param slot The touch slot where the touchpoint moved.
     * @param x Touch motion x coordinate.
     * @param y Touch motion y coordinate.
     * @return Result indicating success or failure.
     */
    fun notifyTouchMotion(stream: UInt32, slot: UInt32, x: Double, y: Double): Result<Unit> = withSession { session ->
        remoteDesktop.NotifyTouchMotion(session, EMPTY_OPTIONS, stream, slot, x, y)
    }

    /**
     * Notify about a touch-up event.
     *
     * @param slot The touch slot where the touchpoint was removed.
     * @return Result indicating success or failure.
     */
    fun notifyTouchUp(slot: UInt32): Result<Unit> = withSession { session ->
        remoteDesktop.NotifyTouchUp(session, EMPTY_OPTIONS, slot)
    }

    // ========== EIS Connection ==========

    /**
     * Connect to an EIS (Event Input Stream) implementation.
     *
     * This method requires libei integration and must be called after [start].
     *
     * @return Result containing a file descriptor for the EIS connection.
     */
    fun connectToEIS(): Result<FileDescriptor> = withSession { session ->
        remoteDesktop.ConnectToEIS(session, EMPTY_OPTIONS)
    }

}
