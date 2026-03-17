package com.zugaldia.stargate.sdk.notification

import com.zugaldia.stargate.sdk.BUS_NAME
import com.zugaldia.stargate.sdk.OBJECT_PATH
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import org.freedesktop.dbus.connections.impl.DBusConnection
import org.freedesktop.dbus.types.Variant
import org.freedesktop.portal.Notification

/**
 * Wrapper around the org.freedesktop.portal.Notification D-Bus interface.
 * Provides methods to send and withdraw desktop notifications, and observe action invocations.
 */
class NotificationPortal(private val connection: DBusConnection) {

    private val notification: Notification =
        connection.getRemoteObject(BUS_NAME, OBJECT_PATH, Notification::class.java)

    /**
     * Returns the interface version.
     */
    val version: Int
        get() = notification.getVersion().toInt()

    /**
     * Sends a notification with the given ID and content.
     * Reusing an existing ID updates the previous notification.
     */
    @Suppress("TooGenericExceptionCaught")
    fun addNotification(
        id: String,
        title: String,
        body: String,
        priority: NotificationPriority = NotificationPriority.NORMAL
    ): Result<Unit> {
        try {
            val data = buildMap<String, Variant<*>> {
                put("title", Variant(title))
                put("body", Variant(body))
                put("priority", Variant(priority.value))
            }
            notification.AddNotification(id, data)
            return Result.success(Unit)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    /**
     * Sends a notification with a raw vardict for full control over notification fields.
     */
    @Suppress("TooGenericExceptionCaught")
    fun addNotification(id: String, data: Map<String, Variant<*>>): Result<Unit> {
        try {
            notification.AddNotification(id, data)
            return Result.success(Unit)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    /**
     * Withdraws a previously sent notification.
     */
    @Suppress("TooGenericExceptionCaught")
    fun removeNotification(id: String): Result<Unit> {
        try {
            notification.RemoveNotification(id)
            return Result.success(Unit)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    /**
     * A flow that emits when a notification action is invoked by the user.
     */
    val actionInvocations: Flow<ActionInvocation> = callbackFlow {
        val handler: (Notification.ActionInvoked) -> Unit = { signal ->
            trySend(ActionInvocation(notificationId = signal.id, action = signal.action))
        }
        val signalHandler = connection.addSigHandler(
            Notification.ActionInvoked::class.java,
            notification,
            handler
        )
        awaitClose { signalHandler.close() }
    }
}
