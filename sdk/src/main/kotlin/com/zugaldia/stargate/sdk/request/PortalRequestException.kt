package com.zugaldia.stargate.sdk.request

/**
 * Exception thrown when a portal request ends with a non-success response.
 *
 * @param response The portal response indicating how the interaction ended.
 * @param methodName The name of the portal method that was called.
 */
class PortalRequestException(
    val response: RequestResponse,
    val methodName: String
) : Exception("$methodName failed ($response)")
