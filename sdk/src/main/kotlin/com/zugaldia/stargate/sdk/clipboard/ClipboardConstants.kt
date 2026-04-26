package com.zugaldia.stargate.sdk.clipboard

/**
 * Option key for MIME types.
 * Used in SetSelection options and returned in SelectionOwnerChanged signal options.
 */
const val OPTION_MIME_TYPES = "mime_types"

/**
 * Result key indicating whether the session is the current clipboard owner.
 * Returned in SelectionOwnerChanged signal options.
 */
const val RESULT_SESSION_IS_OWNER = "session_is_owner"
