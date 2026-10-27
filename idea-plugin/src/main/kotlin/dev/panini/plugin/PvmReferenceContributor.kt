package dev.panini.plugin

import com.intellij.openapi.util.TextRange
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiPolyVariantReferenceBase
import com.intellij.psi.PsiReferenceContributor
import com.intellij.psi.PsiReferenceProvider
import com.intellij.psi.PsiReferenceRegistrar
import com.intellij.psi.ResolveResult
import com.intellij.psi.PsiElementResolveResult
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.ProcessingContext
import com.intellij.util.IncorrectOperationException
import dev.panini.vyakaranam.parser.PaniniParser

/** Native IntelliJ references backed by canonical AST identities and project documents. */
class PvmReferenceContributor : PsiReferenceContributor() {
    override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {
        registrar.registerReferenceProvider(
            PlatformPatterns.psiElement(PvmPsiElement::class.java).withLanguage(PvmLanguage.INSTANCE),
            object : PsiReferenceProvider() {
                override fun getReferencesByElement(
                    element: PsiElement,
                    context: ProcessingContext,
                ) = referencesIn(element as PvmPsiElement)
            },
        )
    }

    private fun referencesIn(host: PvmPsiElement): Array<PvmPrakriyaReference> {
        if (host.node.elementType !in REFERENCE_HOST_TYPES) return emptyArray()
        val file = host.containingFile as? PvmFile ?: return emptyArray()
        val document = file.canonicalDocument() ?: return emptyArray()
        val knownNames = PvmProjectFileIndex.knownOperationStems(file)
        return PsiTreeUtil.collectElementsOfType(host, PsiElement::class.java)
            .asSequence()
            .filter { it.firstChild == null && it.node.elementType in PvmTokenTypes.SEMANTIC_SYMBOLS }
            .mapNotNull { leaf ->
                val identity = PvmSymbolResolver.prakriyaReferenceIdentityAt(
                    document, leaf.textOffset, knownNames,
                ) ?: return@mapNotNull null
                PvmPrakriyaReference(
                    host,
                    TextRange(leaf.textOffset - host.textOffset, leaf.textRange.endOffset - host.textOffset),
                    identity,
                )
            }
            .toList()
            .toTypedArray()
    }

    private companion object {
        val REFERENCE_HOST_TYPES = setOf(
            PvmTokenTypes.PRAKRIYA_STATEMENT,
            PvmTokenTypes.SCOPE_DECLARATION,
            PvmTokenTypes.UKTI,
            PvmTokenTypes.PROGRAM_ITEM,
            PvmTokenTypes.UTTERANCE,
        )
    }
}

private class PvmPrakriyaReference(
    element: PsiElement,
    range: TextRange,
    private val identity: Pair<String, String?>,
) :
    PsiPolyVariantReferenceBase<PsiElement>(element, range, false) {

    override fun multiResolve(incompleteCode: Boolean): Array<ResolveResult> {
        val file = element.containingFile as? PvmFile ?: return ResolveResult.EMPTY_ARRAY
        return resolvePrakriyaTargets(file, identity)
            .map { target ->
                generateSequence(target) { it.parent }
                    .filterIsInstance<PvmPsiElement>()
                    .firstOrNull { it.node.elementType == PvmTokenTypes.PRAKRIYA_HEADER }
                    ?: target
            }
            .distinct()
            .map(::PsiElementResolveResult)
            .toTypedArray()
    }

    override fun resolve(): PsiElement? = multiResolve(false).singleOrNull()?.element

    override fun isReferenceTo(target: PsiElement): Boolean =
        multiResolve(false).any { result ->
            result.element?.let { element.manager.areElementsEquivalent(it, target) } == true
        }

    override fun handleElementRename(newElementName: String): PsiElement {
        if (!isPvmLexicalRoot(newElementName)) {
            throw IncorrectOperationException(
                "Rename only the lexical root; PaniniVM preserves derivational affixes and case endings",
            )
        }
        val file = element.containingFile as? PvmFile
            ?: throw IncorrectOperationException("Missing PaniniVM file")
        val start = element.textRange.startOffset + rangeInElement.startOffset
        val end = element.textRange.startOffset + rangeInElement.endOffset
        val source = file.text.replaceRange(start, end, newElementName)
        if (runCatching { PaniniParser().parseDocument(source) }.isFailure) {
            throw IncorrectOperationException("The renamed reference would not be grammatically valid")
        }
        val document = file.viewProvider.document
            ?: throw IncorrectOperationException("PaniniVM document is not editable")
        val hostType = element.node.elementType
        document.replaceString(start, end, newElementName)
        PsiDocumentManager.getInstance(file.project).commitDocument(document)
        val renamedLeaf = file.findElementAt(start)
            ?: throw IncorrectOperationException("Renamed reference could not be resolved")
        return generateSequence(renamedLeaf) { it.parent }
            .firstOrNull { it.node.elementType == hostType }
            ?: renamedLeaf
    }
}
