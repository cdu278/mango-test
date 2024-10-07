package cdu278.mangotest.ui.auth.phone

import android.telephony.PhoneNumberUtils
import android.text.Selection
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import com.google.i18n.phonenumbers.AsYouTypeFormatter

class PhoneVisualTransformation(
    private val formatter: AsYouTypeFormatter,
) : VisualTransformation {

    override fun filter(text: AnnotatedString): TransformedText {
        val transformation = reformat(text, Selection.getSelectionEnd(text))
        return TransformedText(
            AnnotatedString(transformation.formatted ?: ""),
            object : OffsetMapping {
                override fun originalToTransformed(offset: Int): Int {
                    return try {
                        transformation.originalToTransformed[offset]
                    } catch (ex: Exception) {
                        transformation.transformedToOriginal.lastIndex
                    }
                }

                override fun transformedToOriginal(offset: Int): Int {
                    return transformation.transformedToOriginal[offset]
                }
            })
    }


    private fun reformat(chars: CharSequence, cursor: Int): Transformation {
        formatter.clear()

        val curIndex = cursor - 1
        var formatted: String? = null
        var lastNonSeparator = 0.toChar()
        var hasCursor = false

        chars.forEachIndexed { index, char ->
            if (PhoneNumberUtils.isNonSeparator(char)) {
                if (lastNonSeparator.code != 0) {
                    formatted = getFormattedNumber(lastNonSeparator, hasCursor)
                    hasCursor = false
                }
                lastNonSeparator = char
            }
            if (index == curIndex) {
                hasCursor = true
            }
        }

        if (lastNonSeparator.code != 0) {
            formatted = getFormattedNumber(lastNonSeparator, hasCursor)
        }
        val originalToTransformed = mutableListOf<Int>()
        val transformedToOriginal = mutableListOf<Int>()
        var specialCharsCount = 0
        formatted?.forEachIndexed { index, char ->
            if (!PhoneNumberUtils.isNonSeparator(char)) {
                specialCharsCount++
            } else {
                originalToTransformed.add(index)
            }
            transformedToOriginal.add(index - specialCharsCount)
        }
        originalToTransformed.add(originalToTransformed.maxOrNull()?.plus(1) ?: 0)
        transformedToOriginal.add(transformedToOriginal.maxOrNull()?.plus(1) ?: 0)

        return Transformation(formatted, originalToTransformed, transformedToOriginal)
    }

    private fun getFormattedNumber(lastNonSeparator: Char, hasCursor: Boolean): String? {
        return if (hasCursor) {
            formatter.inputDigitAndRememberPosition(lastNonSeparator)
        } else {
            formatter.inputDigit(lastNonSeparator)
        }
    }

    private data class Transformation(
        val formatted: String?,
        val originalToTransformed: List<Int>,
        val transformedToOriginal: List<Int>
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as PhoneVisualTransformation
        return formatter === other.formatter
    }

    override fun hashCode(): Int {
        return formatter.hashCode()
    }
}