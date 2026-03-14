package com.zugaldia.stargate.app.notification

data class NotificationState(
    val isSending: Boolean = false,
    val lastResult: String? = null,
    val error: String? = null
)
