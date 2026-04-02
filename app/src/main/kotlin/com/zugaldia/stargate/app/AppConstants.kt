package com.zugaldia.stargate.app

const val SPACING = 12
const val SIGNAL_STATE_CHANGED = "state-changed"

const val TEXT_VIEW_WIDTH = 300
const val TEXT_VIEW_HEIGHT = 120
const val LABEL_MAX_WIDTH_CHARS = 50

const val PREFERRED_TRIGGER = "CTRL+SHIFT+z"

const val DEFAULT_TEXT = "Select a language above to load sample text, or type your own here.\n" +
    "Press \"Type Text\" to send it as keystrokes to the focused application."

val SAMPLE_LANGUAGES = listOf(
    "Custom" to null,
    "English" to "sample_en.txt",
    "French" to "sample_fr.txt",
    "Spanish" to "sample_es.txt",
    "Russian" to "sample_ru.txt",
    "Arabic" to "sample_ar.txt",
    "Chinese" to "sample_zh.txt"
)
