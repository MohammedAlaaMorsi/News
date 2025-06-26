package io.mohammedalaamorsi.nyt.util

import com.google.common.truth.Truth.assertThat
import io.mohammedalaamorsi.nyt.R
import org.junit.Test

class UiTextTest {

    @Test
    fun `DynamicString should return correct text`() {
        // Given
        val testText = "This is a dynamic string"
        val uiText = UiText.DynamicString(testText)

        // Then
        assertThat(uiText.text).isEqualTo(testText)
    }

    @Test
    fun `StringResource should contain correct resource id and args`() {
        // Given
        val resId = R.string.app_name
        val args = listOf("arg1", "arg2", 123)
        val uiText = UiText.StringResource(resId, args)

        // Then
        assertThat(uiText.resId).isEqualTo(resId)
        assertThat(uiText.args).isEqualTo(args)
    }

    @Test
    fun `StringResource should have empty args by default`() {
        // Given
        val resId = R.string.app_name
        val uiText = UiText.StringResource(resId)

        // Then
        assertThat(uiText.resId).isEqualTo(resId)
        assertThat(uiText.args).isEmpty()
    }

    @Test
    fun `Empty should be singleton object`() {
        // Given
        val empty1 = UiText.Empty
        val empty2 = UiText.Empty

        // Then
        assertThat(empty1).isSameInstanceAs(empty2)
    }

    @Test
    fun `StringResource equality should work correctly`() {
        // Given
        val resId = R.string.app_name
        val args = listOf("test")
        val uiText1 = UiText.StringResource(resId, args)
        val uiText2 = UiText.StringResource(resId, args)
        val uiText3 = UiText.StringResource(resId, listOf("different"))

        // Then
        assertThat(uiText1).isEqualTo(uiText2)
        assertThat(uiText1).isNotEqualTo(uiText3)
    }

    @Test
    fun `DynamicString equality should work correctly`() {
        // Given
        val text = "Test text"
        val uiText1 = UiText.DynamicString(text)
        val uiText2 = UiText.DynamicString(text)
        val uiText3 = UiText.DynamicString("Different text")

        // Then
        assertThat(uiText1).isEqualTo(uiText2)
        assertThat(uiText1).isNotEqualTo(uiText3)
    }

    @Test
    fun `UiText types should be distinct`() {
        // Given
        val dynamicString = UiText.DynamicString("test")
        val stringResource = UiText.StringResource(R.string.app_name)
        val empty = UiText.Empty

        // Then
        assertThat(dynamicString).isNotEqualTo(stringResource)
        assertThat(dynamicString).isNotEqualTo(empty)
        assertThat(stringResource).isNotEqualTo(empty)
    }
}
