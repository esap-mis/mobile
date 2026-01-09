package javavlsu.kb.esap.esapmobile.presentation.ui.main

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class UrlVisualTransformation : VisualTransformation {

    private val prefix = "http://"

    override fun filter(text: AnnotatedString): TransformedText {
        val newText = AnnotatedString(
            text = prefix + text.text,
            spanStyles = listOf(
                AnnotatedString.Range(SpanStyle(color = Color.Gray), 0, prefix.length),
                AnnotatedString.Range(
                    SpanStyle(color = Color.Gray),
                    prefix.length + text.length,
                    prefix.length + text.length
                )
            )
        )

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                return offset + prefix.length
            }

            override fun transformedToOriginal(offset: Int): Int {
                return when {
                    offset < prefix.length -> 0
                    offset > prefix.length + text.length -> text.length
                    else -> offset - prefix.length
                }
            }
        }

        return TransformedText(newText, offsetMapping)
    }
}