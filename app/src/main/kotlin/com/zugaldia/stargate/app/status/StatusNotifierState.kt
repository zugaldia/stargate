package com.zugaldia.stargate.app.status

data class StatusNotifierState(
    val isLoading: Boolean = false,
    val isConnected: Boolean = false,
    val registeredServiceName: String? = null,
    val isStatusNotifierHostRegistered: Boolean? = null,
    val protocolVersion: Int? = null,
    val registeredStatusNotifierItems: List<String> = emptyList(),
    val message: String? = null,
    val error: String? = null
)
