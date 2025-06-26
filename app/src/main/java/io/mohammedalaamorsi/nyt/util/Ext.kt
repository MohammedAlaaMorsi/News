package io.mohammedalaamorsi.nyt.util

fun String.formatDetailDate(): String {
    return try {
        val inputFormat = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
        val outputFormat =
            java.text.SimpleDateFormat("MMMM dd, yyyy", java.util.Locale.getDefault())
        val date = inputFormat.parse(this)
        outputFormat.format(date ?: return this)
    } catch (e: Exception) {
        this
    }
}
