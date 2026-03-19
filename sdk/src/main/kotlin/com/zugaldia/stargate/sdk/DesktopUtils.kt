package com.zugaldia.stargate.sdk

import java.util.UUID

/** Prefix used for all portal request tokens. */
const val TOKEN_PREFIX = "stargate"

/**
 * Generates a unique token for portal request handles.
 * The token is used as the last element of a handle path: /org/freedesktop/portal/desktop/request/SENDER/TOKEN
 * Reference: https://flatpak.github.io/xdg-desktop-portal/docs/doc-org.freedesktop.portal.Request.html
 */
fun generateToken(): String {
    val uuid = UUID.randomUUID().toString().replace("-", "")
    return "${TOKEN_PREFIX}_$uuid"
}

/**
 * Whether the application is running inside a Snap package.
 * https://snapcraft.io/docs/reference/development/environment-variables/
 */
fun isSnap(): Boolean = !System.getenv("SNAP_NAME").isNullOrEmpty()

/**
 * Whether the application is running inside a Flatpak sandbox.
 * https://docs.flatpak.org/en/latest/flatpak-command-reference.html
 */
fun isFlatpak(): Boolean = !System.getenv("FLATPAK_ID").isNullOrEmpty()

/**
 * Whether the application is running inside any sandbox (Snap or Flatpak).
 * This is a somewhat naive detection. Is there a more reliable method?
 */
fun isSandboxed(): Boolean = isSnap() || isFlatpak()
