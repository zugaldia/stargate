package com.zugaldia.stargate.sdk

/**
 * D-Bus bus name for the desktop portal.
 */
const val BUS_NAME = "org.freedesktop.portal.Desktop"

/**
 * D-Bus object path for the desktop portal.
 */
const val OBJECT_PATH = "/org/freedesktop/portal/desktop"

/**
 * Option key for the request handle token.
 * Used to identify the request object path.
 */
const val OPTION_HANDLE_TOKEN = "handle_token"

/**
 * Option key for the session handle token.
 * Used in portal CreateSession methods.
 */
const val OPTION_SESSION_HANDLE_TOKEN = "session_handle_token"

/**
 * Result key for the session handle returned in responses.
 */
const val RESULT_SESSION_HANDLE = "session_handle"
