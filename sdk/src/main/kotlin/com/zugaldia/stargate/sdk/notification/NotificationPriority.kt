package com.zugaldia.stargate.sdk.notification

/**
 * Priority level for a notification.
 */
enum class NotificationPriority(val value: String) {
    LOW("low"),
    NORMAL("normal"),
    HIGH("high"),
    URGENT("urgent");

    companion object {
        fun fromValue(value: String): NotificationPriority =
            entries.find { it.value == value } ?: NORMAL
    }
}
