package io.github.staakk.progresstracker.ui.common

import androidx.compose.ui.text.AnnotatedString
import org.junit.Assert.*
import org.junit.Test


class SuffixTransformationTest {

    private val text = "test"
    private val suffix = " suffix"

    private val tested = SuffixTransformation(AnnotatedString(suffix))

    @Test
    fun `should return text with suffix`() {
        val result = tested.filter(AnnotatedString(text))

        assertEquals(text + suffix, result.text.toString())
    }

    @Test
    fun `should map original offset to transformed`() {
        val result = tested.filter(AnnotatedString(text))
        val offsetMapping = result.offsetMapping
        listOf(
            0 to 0,
            text.length to text.length,
            text.length + suffix.length to text.length
        ).forEach { (original, expected) ->
            assertEquals(expected, offsetMapping.originalToTransformed(original))
        }
    }

    @Test
    fun `should map transformed offset to original`() {
        val result = tested.filter(AnnotatedString(text))
        val offsetMapping = result.offsetMapping
        listOf(
            0 to 0,
            text.length to text.length,
            text.length + suffix.length to text.length
        ).forEach { (original, expected) ->
            assertEquals(expected, offsetMapping.transformedToOriginal(original))
        }
    }
}