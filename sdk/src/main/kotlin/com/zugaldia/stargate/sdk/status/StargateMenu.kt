package com.zugaldia.stargate.sdk.status

import com.canonical.Dbusmenu
import com.canonical.GetGroupPropertiesPropertiesStruct
import com.canonical.GetLayoutLayoutStruct
import com.canonical.GetLayoutTuple
import org.freedesktop.dbus.types.UInt32
import org.freedesktop.dbus.types.Variant
import org.slf4j.LoggerFactory

/**
 * Server-side implementation of the com.canonical.dbusmenu interface.
 * Export this object at [DBUS_MENU_OBJECT_PATH] alongside the StatusNotifierItem,
 * and return [DBUS_MENU_OBJECT_PATH] from [getMenu] so the tray host can find it.
 */
class StargateMenu(
    private val items: List<StargateMenuItem>,
    private val onMenuOpened: (() -> Unit)? = null,
    private val onMenuClosed: (() -> Unit)? = null,
) : Dbusmenu {

    private val logger = LoggerFactory.getLogger(javaClass)

    // Wired by StargateStatusNotifierItem to consume the pending XDG activation token.
    internal var tokenConsumer: () -> String? = { null }

    override fun getVersion(): UInt32 = UInt32(DBUS_MENU_VERSION)

    override fun getStatus(): String = "normal"

    override fun GetLayout(
        parentId: Int,
        recursionDepth: Int,
        propertyNames: List<String>
    ): GetLayoutTuple<UInt32, GetLayoutLayoutStruct> {
        val children = items.map { item -> Variant(item.toStruct(), "(ia{sv}av)") as Variant<*> }
        val root = GetLayoutLayoutStruct(
            0,
            mapOf("children-display" to Variant("submenu", "s")),
            children,
        )

        return GetLayoutTuple(UInt32(1), root)
    }

    override fun GetGroupProperties(
        ids: List<Int>,
        propertyNames: List<String>
    ): List<GetGroupPropertiesPropertiesStruct> = items
        .filter { it.id in ids }
        .map { GetGroupPropertiesPropertiesStruct(it.id, it.toProperties()) }

    override fun GetProperty(id: Int, name: String): Variant<*> =
        items.find { it.id == id }?.toProperties()?.get(name) ?: Variant("", "s")

    override fun Event(id: Int, eventId: String, data: Variant<*>, timestamp: UInt32) {
        logger.info("Event: id=$id, eventId=$eventId")
        when (eventId) {
            "opened" -> onMenuOpened?.invoke()
            "closed" -> onMenuClosed?.invoke()
            "clicked" -> {
                items.filterIsInstance<StargateMenuItem.Action>().find { it.id == id }?.onClick?.invoke(tokenConsumer)
            }
        }
    }

    override fun AboutToShow(id: Int): Boolean = false

    override fun getObjectPath(): String = DBUS_MENU_OBJECT_PATH
}
