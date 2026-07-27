package dev.panini.plugin

import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiWhiteSpace
import dev.panini.aryabhatiya.AryabhatiyaDecoder
import dev.panini.aryabhatiya.AryabhatiyaMapping
import dev.panini.katapayadi.KatapayadiDecoder
import dev.panini.katapayadi.KatapayadiMapping
import dev.panini.bhutasamkhya.BhutasamkhyaDecoder
import dev.panini.bhutasamkhya.BhutasamkhyaLexicon

/**
 * PvmConvertToDecimalIntention decodes any hovered Sanskrit numeral (under Katapayadi, Bhutasamkhya, or Aryabhatiya)
 * and replaces it (including its prefix keyword if present) with its decimal value.
 */
class PvmConvertToDecimalIntention : PsiElementBaseIntentionAction(), IntentionAction {

    override fun getText(): String = "Convert Sanskrit numeral to decimal"

    override fun getFamilyName(): String = "PaniniVM Numeral Conversions"

    override fun isAvailable(project: Project, editor: Editor, element: PsiElement): Boolean {
        if (element.containingFile !is PvmFile) return false
        val text = element.text.trim()
        if (text.isEmpty() || !text.all { it in '\u0900'..'\u097F' || it == '-' }) return false

        val decoded = tryDecode(text, getPreviousPrefix(element))
        return decoded != null
    }

    override fun invoke(project: Project, editor: Editor, element: PsiElement) {
        val text = element.text.trim()
        val prefix = getPreviousPrefix(element)
        val decoded = tryDecode(text, prefix) ?: return

        val document = editor.document
        var startOffset = element.textRange.startOffset
        val endOffset = element.textRange.endOffset

        // If previous sibling was the prefix keyword, consume it as well
        val prev = getPreviousNonWhitespaceSibling(element)
        if (prev != null && prev.text == prefix) {
            startOffset = prev.textRange.startOffset
        }

        document.replaceString(startOffset, endOffset, decoded.toString())
    }

    override fun startInWriteAction(): Boolean = true

    private fun getPreviousPrefix(element: PsiElement): String? {
        val prev = getPreviousNonWhitespaceSibling(element) ?: return null
        val text = prev.text
        return if (text in setOf("कटपय", "भूतसङ्ख्या", "आर्यभटीय")) text else null
    }

    private fun getPreviousNonWhitespaceSibling(element: PsiElement): PsiElement? {
        var prev = element.prevSibling
        while (prev != null && (prev is PsiWhiteSpace || prev.text.isBlank())) {
            prev = prev.prevSibling
        }
        return prev
    }

    private fun tryDecode(text: String, prefix: String?): Long? {
        if (prefix == "भूतसङ्ख्या") {
            return runCatching { BhutasamkhyaDecoder().decode(text) }.getOrNull()
        }
        if (prefix == "आर्यभटीय") {
            return runCatching { AryabhatiyaDecoder().decode(text) }.getOrNull()
        }
        if (prefix == "कटपय") {
            return runCatching { KatapayadiDecoder().decode(text) }.getOrNull()
        }

        // Without prefix, attempt all three systems
        // 1. Bhutasamkhya
        val parts = text.split("-", " ").map { it.trim() }.filter { it.isNotEmpty() }
        if (parts.isNotEmpty() && parts.all { BhutasamkhyaLexicon.isSymbol(it) }) {
            val res = runCatching { BhutasamkhyaDecoder().decode(text) }.getOrNull()
            if (res != null) return res
        }

        // 2. Aryabhatiya
        val isValidArya = text.all { AryabhatiyaMapping.isConsonant(it) || AryabhatiyaMapping.getVowelPower(it) != null || it == '्' }
        if (isValidArya) {
            val res = runCatching { AryabhatiyaDecoder().decode(text) }.getOrNull()
            if (res != null) return res
        }

        // 3. Katapayadi
        val hasKataConsonant = text.any { KatapayadiMapping.isConsonant(it) }
        if (hasKataConsonant) {
            val res = runCatching { KatapayadiDecoder().decode(text) }.getOrNull()
            if (res != null) return res
        }

        return null
    }
}
