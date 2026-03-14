package com.zugaldia.stargate.app.notification

import com.zugaldia.stargate.app.SIGNAL_STATE_CHANGED
import com.zugaldia.stargate.sdk.DesktopPortal
import com.zugaldia.stargate.sdk.notification.NotificationPriority
import java.util.UUID
import org.gnome.glib.GLib
import org.gnome.gobject.GObject
import org.javagi.gobject.annotations.Signal
import org.slf4j.LoggerFactory

private const val NOTIFICATION_TITLE = "Stargate Test Notification"

class NotificationViewModel(private val portal: DesktopPortal) : GObject() {
    private val logger = LoggerFactory.getLogger(NotificationViewModel::class.java)

    private var _state: NotificationState = NotificationState()
    val state: NotificationState
        get() = _state

    @Signal
    interface StateChanged {
        fun apply()
    }

    private fun updateState(value: NotificationState) {
        _state = value
        GLib.idleAdd(GLib.PRIORITY_DEFAULT) {
            logger.info("State updated, emitting $SIGNAL_STATE_CHANGED signal")
            emit(SIGNAL_STATE_CHANGED)
            false
        }
    }

    fun sendNotification(body: String) {
        updateState(_state.copy(isSending = true, error = null))
        val result = portal.notification.addNotification(
            id = UUID.randomUUID().toString(),
            title = NOTIFICATION_TITLE,
            body = body,
            priority = NotificationPriority.NORMAL
        )
        result.fold(
            onSuccess = {
                logger.info("Notification sent successfully")
                updateState(_state.copy(isSending = false, lastResult = "Notification sent."))
            },
            onFailure = { error ->
                logger.error("Failed to send notification", error)
                updateState(_state.copy(isSending = false, error = error.message))
            }
        )
    }
}
