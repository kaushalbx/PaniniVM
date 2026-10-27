package dev.panini.plugin

import com.intellij.lang.cacheBuilder.DefaultWordsScanner
import com.intellij.lang.cacheBuilder.WordsScanner
import com.intellij.lang.findUsages.FindUsagesProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.TokenSet

/** Find-usages metadata sourced from canonical prakriyā identities. */
class PvmFindUsagesProvider : FindUsagesProvider {
    override fun getWordsScanner(): WordsScanner = DefaultWordsScanner(
        PvmLexerAdapter(),
        TokenSet.create(*PvmTokenTypes.SEMANTIC_SYMBOLS.toTypedArray()),
        PvmParserDefinition.COMMENTS,
        TokenSet.EMPTY,
    )

    override fun canFindUsagesFor(psiElement: PsiElement): Boolean =
        identityAt(psiElement) != null

    override fun getHelpId(psiElement: PsiElement): String? = null

    override fun getType(element: PsiElement): String {
        val file = element.containingFile as? PvmFile ?: return "PaniniVM symbol"
        val document = file.canonicalDocument() ?: return "PaniniVM symbol"
        return if (document.prakriyaNameSpans.values.any {
                element.textOffset in it.start until it.endExclusive
            }) {
            "prakriyā declaration"
        } else {
            "prakriyā reference"
        }
    }

    override fun getDescriptiveName(element: PsiElement): String =
        identityAt(element)?.let(::renderIdentity) ?: element.text

    override fun getNodeText(element: PsiElement, useFullName: Boolean): String =
        if (useFullName) getDescriptiveName(element) else element.text

    private fun identityAt(element: PsiElement): Pair<String, String?>? {
        val file = element.containingFile as? PvmFile ?: return null
        val document = file.canonicalDocument() ?: return null
        val knownNames = PvmProjectFileIndex.knownOperationStems(file)
        return PvmSymbolResolver.prakriyaIdentityAt(document, element.textOffset, knownNames)
    }

    private fun renderIdentity(identity: Pair<String, String?>): String =
        identity.second?.let { "$it / ${identity.first}" } ?: identity.first
}
