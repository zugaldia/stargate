package com.zugaldia.stargate.app

import com.zugaldia.stargate.app.globalshortcuts.GlobalShortcutsScreen
import com.zugaldia.stargate.app.globalshortcuts.GlobalShortcutsViewModel
import com.zugaldia.stargate.app.notification.NotificationScreen
import com.zugaldia.stargate.app.notification.NotificationViewModel
import com.zugaldia.stargate.app.openuri.OpenUriScreen
import com.zugaldia.stargate.app.openuri.OpenUriViewModel
import com.zugaldia.stargate.app.remotedesktop.RemoteDesktopScreen
import com.zugaldia.stargate.app.remotedesktop.RemoteDesktopViewModel
import com.zugaldia.stargate.app.settings.SettingsScreen
import com.zugaldia.stargate.app.settings.SettingsViewModel
import com.zugaldia.stargate.sdk.DesktopPortal
import com.zugaldia.stargate.sdk.isSandboxed
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import org.gnome.gio.ApplicationFlags
import org.gnome.gtk.Application
import org.gnome.gtk.ApplicationWindow
import org.gnome.gtk.Box
import org.gnome.gtk.Orientation
import org.gnome.gtk.Stack
import org.gnome.gtk.StackSidebar

private val logger = LoggerFactory.getLogger("com.zugaldia.stargate.app.App")

private const val APPLICATION_ID = "com.zugaldia.Stargate"
private const val APPLICATION_NAME = "Stargate"
private const val DEFAULT_WINDOW_WIDTH = 800
private const val DEFAULT_WINDOW_HEIGHT = 600

fun main(args: Array<String>) {
    logger.info("Starting $APPLICATION_ID, and connecting to the Desktop Portal via D-Bus")
    val portal = DesktopPortal.connect()

    // Unsandboxed apps must register with the portal so D-Bus calls are
    // associated with the correct application ID. Sandboxed apps (Flatpak/Snap)
    // get this automatically. Without registration, portals may not work
    // or work but with degraded functionality.
    if (!isSandboxed()) {
        portal.registry.register(APPLICATION_ID).onSuccess {
            logger.info("Registered application ID: {}", APPLICATION_ID)
        }.onFailure {
            logger.error("Failed to register application ID: {}", APPLICATION_ID, it)
        }
    }

    val globalShortcutsViewModel = GlobalShortcutsViewModel(portal)
    val notificationViewModel = NotificationViewModel(portal)
    val openUriViewModel = OpenUriViewModel(portal)
    val remoteDesktopViewModel = RemoteDesktopViewModel(portal)
    val settingsViewModel = SettingsViewModel(portal)

    val app = Application(APPLICATION_ID, ApplicationFlags.DEFAULT_FLAGS)

    app.onActivate {
        logger.info("Application activated")
        activate(
            app, globalShortcutsViewModel, notificationViewModel, openUriViewModel,
            remoteDesktopViewModel, settingsViewModel
        )
    }

    app.onShutdown {
        logger.info("Application shutting down")
        runBlocking {
            globalShortcutsViewModel.closeAndJoin()
            openUriViewModel.closeAndJoin()
            remoteDesktopViewModel.closeAndJoin()
            settingsViewModel.closeAndJoin()
        }
        portal.close()
    }

    logger.info("Starting application: {}", APPLICATION_ID)
    app.run(args)
}

private fun activate(
    app: Application,
    globalShortcutsViewModel: GlobalShortcutsViewModel,
    notificationViewModel: NotificationViewModel,
    openUriViewModel: OpenUriViewModel,
    remoteDesktopViewModel: RemoteDesktopViewModel,
    settingsViewModel: SettingsViewModel
) {
    val window = ApplicationWindow(app)
    window.title = APPLICATION_NAME
    window.setDefaultSize(DEFAULT_WINDOW_WIDTH, DEFAULT_WINDOW_HEIGHT)

    val stack = Stack()

    val globalShortcutsScreen = GlobalShortcutsScreen(globalShortcutsViewModel)
    stack.addTitled(globalShortcutsScreen.build(), "global-shortcuts", "Global Shortcuts")

    val notificationScreen = NotificationScreen(notificationViewModel)
    stack.addTitled(notificationScreen.build(), "notification", "Notification")

    val openUriScreen = OpenUriScreen(openUriViewModel)
    stack.addTitled(openUriScreen.build(), "open-uri", "Open URI")

    val remoteDesktopScreen = RemoteDesktopScreen(remoteDesktopViewModel)
    stack.addTitled(remoteDesktopScreen.build(), "remote-desktop", "Remote Desktop")

    val settingsScreen = SettingsScreen(settingsViewModel)
    stack.addTitled(settingsScreen.build(), "settings", "Settings")

    val sidebar = StackSidebar()
    sidebar.stack = stack

    val mainBox = Box(Orientation.HORIZONTAL, 0)
    mainBox.append(sidebar)
    mainBox.append(stack)

    window.child = mainBox
    window.present()
}
