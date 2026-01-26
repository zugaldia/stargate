package com.zugaldia.stargate.app

import com.zugaldia.stargate.app.remotedesktop.RemoteDesktopScreen
import com.zugaldia.stargate.app.remotedesktop.RemoteDesktopViewModel
import com.zugaldia.stargate.app.settings.SettingsScreen
import com.zugaldia.stargate.app.settings.SettingsViewModel
import com.zugaldia.stargate.sdk.DesktopPortal
import kotlinx.coroutines.runBlocking
import org.gnome.gio.ApplicationFlags
import org.gnome.gtk.Application
import org.gnome.gtk.ApplicationWindow
import org.gnome.gtk.Box
import org.gnome.gtk.Orientation
import org.gnome.gtk.Stack
import org.gnome.gtk.StackSidebar

private const val APPLICATION_ID = "com.zugaldia.stargate.App"
private const val APPLICATION_NAME = "Stargate"
private const val DEFAULT_WINDOW_WIDTH = 800
private const val DEFAULT_WINDOW_HEIGHT = 600

fun main(args: Array<String>) {
    val portal = DesktopPortal.connect()
    val settingsViewModel = SettingsViewModel(portal)
    val remoteDesktopViewModel = RemoteDesktopViewModel(portal)

    val app = Application(APPLICATION_ID, ApplicationFlags.DEFAULT_FLAGS)
    app.onActivate { activate(app, settingsViewModel, remoteDesktopViewModel) }
    app.onShutdown {
        runBlocking {
            settingsViewModel.closeAndJoin()
            remoteDesktopViewModel.closeAndJoin()
        }
        portal.close()
    }
    app.run(args)
}

private fun activate(
    app: Application,
    settingsViewModel: SettingsViewModel,
    remoteDesktopViewModel: RemoteDesktopViewModel
) {
    val window = ApplicationWindow(app)
    window.title = APPLICATION_NAME
    window.setDefaultSize(DEFAULT_WINDOW_WIDTH, DEFAULT_WINDOW_HEIGHT)

    val stack = Stack()

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
