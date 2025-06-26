package io.mohammedalaamorsi.nyt.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import kotlin.collections.toTypedArray

sealed class UiText {
    data class DynamicString(val text: String) : UiText()
    data class StringResource(val resId: Int, val args: List<Any> = emptyList()) : UiText()
    data object Empty : UiText()

    @Composable
    fun asString(): String {
        return when (this) {
            is DynamicString -> text
            is StringResource -> stringResource(resId, *args.toTypedArray())
            Empty -> ""
        }
    }
}
