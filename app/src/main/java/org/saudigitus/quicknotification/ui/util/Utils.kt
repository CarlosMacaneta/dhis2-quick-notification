package org.saudigitus.quicknotification.ui.util

object Utils {
    fun capitalizeText(text: String): String {
        return text.lowercase()
            .replaceFirstChar {
                it.uppercase()
            }
    }
}