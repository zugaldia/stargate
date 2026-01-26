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
