package com.zugaldia.stargate.sdk.status

import com.canonical.GetLayoutLayoutStruct
import org.freedesktop.dbus.types.Variant

/**
 * Represents an item in a DBusMenu context menu.
 */
sealed class StargateMenuItem {
    abstract val id: Int

    /**
     * A clickable menu entry with a text label.
     * [onClick] is invoked on the D-Bus thread with a `tokenSupplier` that lazily consumes
     * the pending XDG activation token. Call `tokenSupplier` from the GTK main thread
     * (e.g. inside `GLib.idleAdd`) to guarantee the token has been set by
     * a concurrent [org.kde.StatusNotifierItem.ProvideXdgActivationToken] call.
     */
    data class Action(
        override val id: Int,
        val label: String,
        val enabled: Boolean = true,
        val onClick: (tokenSupplier: () -> String?) -> Unit,
    ) : StargateMenuItem()

    /** A visual separator between groups of items. */
    data class Separator(override val id: Int) : StargateMenuItem()
}

fun StargateMenuItem.toStruct(): GetLayoutLayoutStruct =
    GetLayoutLayoutStruct(id, toProperties(), emptyList())

fun StargateMenuItem.toProperties(): Map<String, Variant<*>> = when (this) {
    is StargateMenuItem.Action -> mapOf(
        "type" to Variant("standard", "s"),
        "label" to Variant(label, "s"),
        "enabled" to Variant(enabled, "b"),
        "visible" to Variant(true, "b"),
    )
    is StargateMenuItem.Separator -> mapOf(
        "type" to Variant("separator", "s"),
        "visible" to Variant(true, "b"),
    )
}
