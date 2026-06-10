package com.azyabon.moviesapp.presentation.common

fun formatRuntime(runtimeMinutes: Int?): String {
    if (runtimeMinutes == null || runtimeMinutes <= 0) return ""

    val hours = runtimeMinutes / 60
    val minutes = runtimeMinutes % 60

    return when {
        hours > 0 && minutes > 0 -> "${hours}h ${minutes}m"
        hours > 0 -> "${hours}h"
        else -> "${minutes}m"
    }
}

fun formatReleaseYear(releaseDate: String): String {
    return releaseDate.take(4)
}

fun formatMoney(value: Int): String {
    return when {
        value <= 0 -> "-"
        value >= 1_000_000_000 -> "$${(value / 1_000_000_000.0).formatOneDecimal()}B"
        value >= 1_000_000 -> "$${(value / 1_000_000.0).formatOneDecimal()}M"
        value >= 1_000 -> "$${(value / 1_000.0).formatOneDecimal()}K"
        else -> "$$value"
    }
}

private fun Double.formatOneDecimal(): String {
    return if (this % 1.0 == 0.0) {
        toInt().toString()
    } else {
        String.format("%.1f", this)
    }
}