package io.github.staakk.progresstracker.ui.common

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

/**
 * Add suffix to the text. Only the original text remains editable.
 */
class SuffixTransformation(private val suffix: AnnotatedString) : VisualTransformation {

    override fun filter(text: AnnotatedString): TransformedText {
        return TransformedText(
            text + suffix,
            SuffixOffsetMapping(text.length)
        )
    }

    class SuffixOffsetMapping(private val textLength: Int) : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int =
            if (offset >= textLength) textLength else offset

        override fun transformedToOriginal(offset: Int): Int =
            if (offset >= textLength) textLength else offset

    }
}

