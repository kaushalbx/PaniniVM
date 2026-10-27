package dev.panini.plugin

import com.intellij.lang.Language
import com.intellij.psi.PsiElement
import com.intellij.ui.breadcrumbs.BreadcrumbsProvider
import dev.panini.vyakaranam.ast.Prakriya
import dev.panini.vyakaranam.ast.RangeDeclaration
import dev.panini.vyakaranam.ast.Scope

/** Breadcrumb labels projected from structural PSI and the canonical AST. */
class PvmBreadcrumbsProvider : BreadcrumbsProvider {
    override fun getLanguages(): Array<Language> = arrayOf(PvmLanguage.INSTANCE)

    override fun acceptElement(element: PsiElement): Boolean =
        element.node.elementType in BREADCRUMB_TYPES && element.text.isNotBlank()

    override fun getElementInfo(element: PsiElement): String {
        val file = element.containingFile as? PvmFile
        val canonical = file?.canonicalItemAt(element.textOffset)
        return when (element.node.elementType) {
            PvmTokenTypes.PRAKRIYA_DECLARATION ->
                (canonical as? Prakriya)?.let { "प्रक्रिया ${it.nameIdentity}" }
                    ?: compact(element.text)
            PvmTokenTypes.PRAKRIYA_HEADER ->
                (canonical as? Prakriya)?.let { "शीर्षकम् ${it.nameIdentity}" }
                    ?: "शीर्षकम्"
            PvmTokenTypes.PRAKRIYA_NAME ->
                (canonical as? Prakriya)?.nameIdentity ?: compact(element.text)
            PvmTokenTypes.PRAKRIYA_STATEMENT -> "वाक्यम् ${compact(element.text)}"
            PvmTokenTypes.SCOPE_DECLARATION ->
                (canonical as? Scope)?.let { "अधिकारः ${it.domainIdentity}" }
                    ?: compact(element.text)
            PvmTokenTypes.SCOPE_DOMAIN ->
                (canonical as? Scope)?.domainIdentity ?: compact(element.text)
            PvmTokenTypes.RANGE_DECLARATION ->
                (canonical as? RangeDeclaration)?.let { "सीमा ${compact(it.sourceText)}" }
                    ?: compact(element.text)
            PvmTokenTypes.SUBANTA -> "सुबन्तम् ${compact(element.text)}"
            PvmTokenTypes.TINGANTA -> "तिङन्तम् ${compact(element.text)}"
            PvmTokenTypes.UKTI -> "उक्तिः ${compact(element.text)}"
            PvmTokenTypes.PROGRAM_ITEM -> "वाक्यम् ${compact(element.text)}"
            PvmTokenTypes.UTTERANCE -> "अपूर्णम् ${compact(element.text)}"
            PvmTokenTypes.GRAMMATICAL_TERM -> compact(element.text)
            else -> compact(element.text)
        }
    }

    override fun getElementTooltip(element: PsiElement): String = element.text.trim()

    private fun compact(text: String): String {
        val normalized = text.replace(WHITESPACE, " ").trim()
        return if (normalized.length <= MAX_LABEL_LENGTH) normalized
        else normalized.take(MAX_LABEL_LENGTH - 1) + "…"
    }

    private companion object {
        const val MAX_LABEL_LENGTH = 64
        val WHITESPACE = Regex("\\s+")
        val BREADCRUMB_TYPES = setOf(
            PvmTokenTypes.PRAKRIYA_DECLARATION,
            PvmTokenTypes.PRAKRIYA_HEADER,
            PvmTokenTypes.PRAKRIYA_NAME,
            PvmTokenTypes.PRAKRIYA_STATEMENT,
            PvmTokenTypes.SCOPE_DECLARATION,
            PvmTokenTypes.SCOPE_DOMAIN,
            PvmTokenTypes.RANGE_DECLARATION,
            PvmTokenTypes.SUBANTA,
            PvmTokenTypes.TINGANTA,
            PvmTokenTypes.UKTI,
            PvmTokenTypes.PROGRAM_ITEM,
            PvmTokenTypes.UTTERANCE,
            PvmTokenTypes.GRAMMATICAL_TERM,
        )
    }
}
