package com.zugaldia.stargate.sdk.request

import kotlinx.coroutines.suspendCancellableCoroutine
import org.freedesktop.dbus.DBusPath
import org.freedesktop.dbus.connections.impl.DBusConnection
import org.freedesktop.dbus.types.Variant
import org.freedesktop.portal.Request
import java.util.concurrent.atomic.AtomicReference
import kotlin.coroutines.resume

/**
 * Awaits a portal response signal after making a D-Bus request.
 *
 * This utility encapsulates the common pattern of:
 * 1. Setting up a signal handler for [Request.Response]
 * 2. Making the D-Bus call
 * 3. Waiting for the response signal
 * 4. Parsing the result or returning a failure
 *
 * @param T The type of the successful result.
 * @param connection The D-Bus connection to use for signal handling.
 * @param methodName The name of the portal method being called (for error messages).
 * @param call A lambda that makes the D-Bus call and returns the request handle path.
 * @param parseSuccess A lambda that parses the signal results on success.
 * @return [Result.success] with the parsed result, or [Result.failure] on error or cancellation.
 */
suspend fun <T> awaitPortalResponse(
    connection: DBusConnection,
    methodName: String,
    call: () -> DBusPath,
    parseSuccess: (Map<String, Variant<*>>) -> T
): Result<T> = suspendCancellableCoroutine { continuation ->
    val requestPathRef = AtomicReference<String?>(null)
    var handler: AutoCloseable? = null

    handler = connection.addSigHandler(Request.Response::class.java) { signal ->
        val path = requestPathRef.get()
        if (path != null && signal.path == path) {
            try {
                when (val response = RequestResponse.fromValue(signal.response)) {
                    RequestResponse.SUCCESS -> {
                        val result = parseSuccess(signal.results)
                        continuation.resume(Result.success(result))
                    }
                    else -> {
                        val error = "$methodName failed ($response)"
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

    try {
        val requestHandle = call()
        requestPathRef.set(requestHandle.path)
        continuation.invokeOnCancellation { handler?.close() }
    } catch (@Suppress("TooGenericExceptionCaught") e: Exception) {
        handler?.close()
        continuation.resume(Result.failure(e))
    }
}
