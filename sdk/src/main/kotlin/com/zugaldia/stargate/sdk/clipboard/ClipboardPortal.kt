package com.zugaldia.stargate.sdk.clipboard

import com.zugaldia.stargate.sdk.BUS_NAME
import com.zugaldia.stargate.sdk.EMPTY_OPTIONS
import com.zugaldia.stargate.sdk.OBJECT_PATH
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import org.freedesktop.dbus.DBusPath
import org.freedesktop.dbus.FileDescriptor
import org.freedesktop.dbus.connections.impl.DBusConnection
import org.freedesktop.dbus.types.UInt32
import org.freedesktop.dbus.types.Variant
import org.freedesktop.portal.Clipboard

/**
 * Wrapper around the org.freedesktop.portal.Clipboard D-Bus interface.
 * Provides access to clipboard content for existing portal sessions.
 *
 * This portal does not create its own session. It integrates with sessions from other portals,
 * such as [com.zugaldia.stargate.sdk.remotedesktop.RemoteDesktopPortal] and the Input Capture portal.
 * Call [requestClipboard] before the session starts to request clipboard access, then use
 * [setSelection], [selectionWrite], [selectionWriteDone], and [selectionRead] after the session is active.
 */
class ClipboardPortal(private val connection: DBusConnection) {

    private val clipboard: Clipboard =
        connection.getRemoteObject(BUS_NAME, OBJECT_PATH, Clipboard::class.java)

    /**
     * Returns the interface version.
     */
    val version: Int
        get() = clipboard.getVersion().toInt()

    /**
     * Requests clipboard access for the given session. Must be called before the session starts.
     * The session must be started before calling any other method in this interface.
     * Note that other interfaces may place restrictions on when it is possible to interact with
     * the clipboard.
     *
     * @param sessionHandle Object path for the session created by another portal.
     * @return Result indicating success or failure.
     */
    fun requestClipboard(sessionHandle: DBusPath): Result<Unit> = runCatching {
        clipboard.RequestClipboard(sessionHandle, EMPTY_OPTIONS)
    }

    /**
     * Sets the session as the owner of the clipboard for the given MIME types.
     * The session will receive [observeSelectionTransfer] events when another client requests
     * clipboard content in one of the advertised formats.
     *
     * To transfer files, use the `application/vnd.portal.filetransfer` MIME type together with
     * the File Transfer portal.
     *
     * May only be called after clipboard access was granted by starting the session.
     *
     * @param sessionHandle Object path for the session.
     * @param mimeTypes List of MIME types the session has clipboard content for.
     * @return Result indicating success or failure.
     */
    fun setSelection(sessionHandle: DBusPath, mimeTypes: List<String>): Result<Unit> = runCatching {
        val options = mapOf(OPTION_MIME_TYPES to Variant(mimeTypes.toTypedArray(), "as"))
        clipboard.SetSelection(sessionHandle, options)
    }

    /**
     * Responds to a [observeSelectionTransfer] event by providing clipboard content via a file descriptor.
     * It is the callee that creates the file descriptor. Write the clipboard data to it, then call
     * [selectionWriteDone] once the transfer completes or fails.
     *
     * May only be called after clipboard access was granted by starting the session.
     *
     * @param sessionHandle Object path for the session.
     * @param serial The serial from the [Clipboard.SelectionTransfer] signal being answered.
     * @return Result containing the file descriptor to write clipboard content to.
     */
    fun selectionWrite(sessionHandle: DBusPath, serial: UInt32): Result<FileDescriptor> = runCatching {
        clipboard.SelectionWrite(sessionHandle, serial)
    }

    /**
     * Notifies that a clipboard transfer initiated by [selectionWrite] has completed or failed.
     * Must be called for every [observeSelectionTransfer] event, even on failure.
     *
     * May only be called after clipboard access was granted by starting the session.
     *
     * @param sessionHandle Object path for the session.
     * @param serial The serial from the [Clipboard.SelectionTransfer] signal being answered.
     * @param success Whether the clipboard data transfer completed successfully.
     * @return Result indicating success or failure.
     */
    fun selectionWriteDone(sessionHandle: DBusPath, serial: UInt32, success: Boolean): Result<Unit> = runCatching {
        clipboard.SelectionWriteDone(sessionHandle, serial, success)
    }

    /**
     * Reads clipboard content for the given MIME type via a file descriptor.
     * The creation of the file descriptor is the responsibility of the callee.
     *
     * May only be called after clipboard access was granted by starting the session.
     *
     * @param sessionHandle Object path for the session.
     * @param mimeType The MIME type of the clipboard content to read.
     * @return Result containing the file descriptor to read clipboard content from.
     */
    fun selectionRead(sessionHandle: DBusPath, mimeType: String): Result<FileDescriptor> = runCatching {
        clipboard.SelectionRead(sessionHandle, mimeType)
    }

    /**
     * Returns a Flow that emits whenever the clipboard selection changes.
     * The emitted signal's [Clipboard.SelectionOwnerChanged.getOptions] map contains
     * [OPTION_MIME_TYPES] (`as`) and [RESULT_SESSION_IS_OWNER] (`b`) keys.
     *
     * Only emits events for the given session. Clipboard access must have been granted
     * by starting the session before events are delivered.
     *
     * @param sessionHandle Object path for the session to observe.
     * @return Flow of selection owner changed signals.
     */
    fun observeSelectionOwnerChanged(sessionHandle: DBusPath): Flow<Clipboard.SelectionOwnerChanged> = callbackFlow {
        val handler = connection.addSigHandler(Clipboard.SelectionOwnerChanged::class.java) { signal ->
            if (signal.sessionHandle == sessionHandle) {
                trySend(signal)
            }
        }
        awaitClose { handler.close() }
    }

    /**
     * Returns a Flow that emits whenever another client requests clipboard content from this session.
     * Respond to each event by calling [selectionWrite] with the signal's serial, then
     * [selectionWriteDone] when the transfer is complete. If the request is not handled, call
     * [selectionWriteDone] with `success = false`.
     *
     * Only emits events for the given session. Clipboard access must have been granted
     * by starting the session before events are delivered.
     *
     * @param sessionHandle Object path for the session to observe.
     * @return Flow of selection transfer request signals.
     */
    fun observeSelectionTransfer(sessionHandle: DBusPath): Flow<Clipboard.SelectionTransfer> = callbackFlow {
        val handler = connection.addSigHandler(Clipboard.SelectionTransfer::class.java) { signal ->
            if (signal.sessionHandle == sessionHandle) {
                trySend(signal)
            }
        }
        awaitClose { handler.close() }
    }
}
