package com.zugaldia.stargate.sdk.notification

/**
 * Data class representing a notification action invocation.
 */
data class ActionInvocation(
    val notificationId: String,
    val action: String
)
