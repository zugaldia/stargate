package com.zugaldia.stargate.sdk.registry

import com.zugaldia.stargate.sdk.BUS_NAME
import com.zugaldia.stargate.sdk.isSandboxed
import org.freedesktop.dbus.connections.impl.DBusConnection
import org.freedesktop.dbus.types.Variant
import org.freedesktop.host.portal.Registry
import org.slf4j.LoggerFactory

/**
 * Wrapper around the org.freedesktop.host.portal.Registry D-Bus interface.
 * Lets unsandboxed applications register their D-Bus connections and associate
 * them with an application ID used in portals.
 */
class RegistryPortal(private val connection: DBusConnection) {

    private val logger = LoggerFactory.getLogger(RegistryPortal::class.java)

    private val registry: Registry =
        connection.getRemoteObject(BUS_NAME, REGISTRY_OBJECT_PATH, Registry::class.java)

    /**
     * Returns the interface version.
     */
    val version: Int
        get() = registry.version.toInt()

    /**
     * Register the D-Bus peer and associate it with an application ID.
     * The application ID must match the basename of a .desktop file.
     *
     * Registration can only be done once and must happen before any portal method call.
     * Sandboxed applications do not need to call this; registration happens automatically.
     *
     * @param appId The application identifier (e.g. "com.example.MyApp").
     * @param options Optional vardict with further information.
     */
    @Suppress("TooGenericExceptionCaught")
    fun register(appId: String, options: Map<String, Variant<*>> = emptyMap()): Result<Unit> {
        if (isSandboxed()) {
            logger.warn(
                "Registering in a sandboxed environment is unnecessary; " +
                    "sandboxed apps are registered automatically."
            )
        }
        try {
            registry.Register(appId, options)
            return Result.success(Unit)
        } catch (e: Exception) {
            // Older desktop environments may not have support for the registry yet.
            return Result.failure(e)
        }
    }
}
