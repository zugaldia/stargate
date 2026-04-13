package com.zugaldia.stargate.sdk.status

import org.freedesktop.dbus.DBusPath
import org.kde.PropertyAttentionIconPixmapStruct
import org.kde.PropertyIconPixmapStruct
import org.kde.PropertyOverlayIconPixmapStruct
import org.kde.PropertyToolTipStruct
import org.kde.StatusNotifierItem
import org.slf4j.LoggerFactory

/**
 * Base class for StatusNotifierItem implementations. Handles the XDG activation token
 * lifecycle and dispatches activate/secondary-activate callbacks. Subclasses provide
 * application-specific identity and icon properties.
 *
 * @param menu Optional context menu exported via the com.canonical.dbusmenu protocol.
 *   When provided, [getMenu] returns [DBUS_MENU_OBJECT_PATH] and the caller is responsible
 *   for exporting the [StargateMenu] object at that path (see [StatusNotifierManager.registerItem]).
 * @param onActivate Called on the D-Bus thread when the host activates the item (e.g. double-click).
 *   Receives the XDG activation token (if any) provided by the host before the activate call.
 *   Implementations must marshal any UI work to the GTK main thread themselves.
 * @param onSecondaryActivate Called on the D-Bus thread when the host secondary-activates the item (e.g. middle-click).
 *   Receives the XDG activation token (if any) provided by the host before the activate call.
 *   Implementations must marshal any UI work to the GTK main thread themselves.
 */
@Suppress("TooManyFunctions")
abstract class StargateStatusNotifierItem(
    val menu: StargateMenu? = null,
    private val onActivate: (token: String?) -> Unit,
    private val onSecondaryActivate: (token: String?) -> Unit,
) : StatusNotifierItem {
    private val logger = LoggerFactory.getLogger(javaClass)

    init {
        menu?.tokenConsumer = ::consumeActivationToken
    }

    @Volatile
    private var pendingActivationToken: String? = null

    private fun consumeActivationToken(): String? {
        val token = pendingActivationToken
        pendingActivationToken = null
        return token
    }

    override fun getCategory(): String = "ApplicationStatus"

    abstract override fun getId(): String

    abstract override fun getTitle(): String

    override fun getStatus(): String = "Active"

    override fun getWindowId(): Int = 0

    override fun getIconThemePath(): String = ""

    override fun getMenu(): DBusPath = if (menu != null) DBusPath(DBUS_MENU_OBJECT_PATH) else DBusPath("/")

    override fun isItemIsMenu(): Boolean = false

    // https://specifications.freedesktop.org/icon-naming/latest/
    override fun getIconName(): String = "dialog-information"

    override fun getIconPixmap(): List<PropertyIconPixmapStruct> = emptyList()

    override fun getOverlayIconName(): String = ""

    override fun getOverlayIconPixmap(): List<PropertyOverlayIconPixmapStruct> = emptyList()

    override fun getAttentionIconName(): String = ""

    override fun getAttentionIconPixmap(): List<PropertyAttentionIconPixmapStruct> = emptyList()

    override fun getAttentionMovieName(): String = ""

    abstract fun getToolTipInfo(): Pair<String, String>

    override fun getToolTip(): PropertyToolTipStruct {
        val info = getToolTipInfo()
        return PropertyToolTipStruct("", emptyList(), info.first, info.second)
    }

    override fun ProvideXdgActivationToken(token: String) {
        logger.info("ProvideXdgActivationToken: $token")
        pendingActivationToken = token
    }

    override fun ContextMenu(x: Int, y: Int) {
        logger.info("ContextMenu: x=$x, y=$y")
    }

    override fun Activate(x: Int, y: Int) {
        logger.info("Activate: x=$x, y=$y (token=$pendingActivationToken)")
        onActivate(consumeActivationToken())
    }

    override fun SecondaryActivate(x: Int, y: Int) {
        logger.info("SecondaryActivate: x=$x, y=$y (token=$pendingActivationToken)")
        onSecondaryActivate(consumeActivationToken())
    }

    override fun Scroll(delta: Int, orientation: String) {
        logger.info("Scroll: delta=$delta, orientation=$orientation")
    }

    override fun getObjectPath(): String = STATUS_NOTIFIER_ITEM_OBJECT_PATH
}
