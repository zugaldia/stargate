package com.zugaldia.stargate.sdk.status

/**
 * D-Bus bus name for the StatusNotifierWatcher.
 */
const val STATUS_NOTIFIER_BUS_NAME = "org.kde.StatusNotifierWatcher"

/**
 * D-Bus object path for the StatusNotifierWatcher.
 */
const val STATUS_NOTIFIER_OBJECT_PATH = "/StatusNotifierWatcher"

/**
 * D-Bus object path for the StatusNotifierItem exported by this process.
 */
const val STATUS_NOTIFIER_ITEM_OBJECT_PATH = "/StatusNotifierItem"

/**
 * Prefix for the well-known bus name claimed by a StatusNotifierItem.
 * Full name: "$STATUS_NOTIFIER_ITEM_SERVICE_PREFIX-{pid}-1"
 */
const val STATUS_NOTIFIER_ITEM_SERVICE_PREFIX = "org.kde.StatusNotifierItem"

/**
 * D-Bus object path for the DBusMenu exported alongside a StatusNotifierItem.
 */
const val DBUS_MENU_OBJECT_PATH = "/StatusNotifierItem/menu"

/**
 * com.canonical.dbusmenu protocol version implemented by this library.
 */
const val DBUS_MENU_VERSION = 3L
