package dev.panini.plugin

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.ContributedReferenceHost
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNameIdentifierOwner
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.search.LocalSearchScope
import com.intellij.psi.search.SearchScope
import com.intellij.psi.impl.source.resolve.reference.ReferenceProvidersRegistry
import com.intellij.util.IncorrectOperationException
import dev.panini.vyakaranam.ast.Prakriya
import dev.panini.vyakaranam.ast.PrakriyaVisibility
import dev.panini.vyakaranam.parser.PaniniParser

class PvmPsiElement(node: ASTNode) : ASTWrapperPsiElement(node), ContributedReferenceHost, PsiNameIdentifierOwner {
    override fun getReferences(): Array<PsiReference> =
        ReferenceProvidersRegistry.getReferencesFromProviders(this)

    override fun getName(): String? = getNameIdentifier()
        ?.let { identifier -> generateSequence(identifier.firstChild) { it.nextSibling }.firstOrNull { it.text.isNotBlank() } }
        ?.text

    override fun setName(name: String): PsiElement {
        if (node.elementType != PvmTokenTypes.PRAKRIYA_HEADER &&
            node.elementType != PvmTokenTypes.PRAKRIYA_NAME
        ) {
            throw IncorrectOperationException("Only PaniniVM prakriyā declarations can be renamed")
        }
        if (!isPvmLexicalRoot(name)) {
            throw IncorrectOperationException(
                "Rename only the lexical root; PaniniVM preserves derivational affixes and case endings",
            )
        }
        val identifier = getNameIdentifier() ?: throw IncorrectOperationException("Missing prakriyā name")
        val root = PsiTreeUtil.collectElements(identifier) {
            it.firstChild == null && it.node.elementType in PvmTokenTypes.SEMANTIC_SYMBOLS
        }.firstOrNull() ?: throw IncorrectOperationException("Missing prakriyā lexical root")
        val file = containingFile as? PvmFile ?: throw IncorrectOperationException("Missing PaniniVM file")
        val rootStart = root.textRange.startOffset
        val rootEnd = root.textRange.endOffset
        val source = file.text.replaceRange(rootStart, rootEnd, name)
        if (runCatching { PaniniParser().parseDocument(source) }.isFailure) {
            throw IncorrectOperationException("The renamed prakriyā would not be grammatically valid")
        }

        val document = file.viewProvider.document
            ?: throw IncorrectOperationException("PaniniVM document is not editable")
        document.replaceString(rootStart, rootEnd, name)
        PsiDocumentManager.getInstance(project).commitDocument(document)
        val renamedLeaf = file.findElementAt(rootStart)
            ?: throw IncorrectOperationException("Renamed prakriyā could not be resolved")
        return generateSequence(renamedLeaf) { it.parent }
            .filterIsInstance<PvmPsiElement>()
            .firstOrNull { it.node.elementType == node.elementType }
            ?: renamedLeaf
    }

    override fun getNameIdentifier(): PsiElement? {
        if (node.elementType == PvmTokenTypes.PRAKRIYA_NAME) return this
        if (node.elementType != PvmTokenTypes.PRAKRIYA_HEADER) return null
        return children.firstOrNull { it.node.elementType == PvmTokenTypes.PRAKRIYA_NAME }
    }

    override fun getUseScope(): SearchScope {
        if (node.elementType == PvmTokenTypes.PRAKRIYA_HEADER ||
            node.elementType == PvmTokenTypes.PRAKRIYA_NAME
        ) {
            val file = containingFile as? PvmFile
            val declaration = file?.canonicalItemAt(textOffset) as? Prakriya
            if (declaration?.modifiers?.visibility == PrakriyaVisibility.INTERNAL) {
                return LocalSearchScope(requireNotNull(file))
            }
            if (file != null) return GlobalSearchScope.projectScope(project)
        }
        return super.getUseScope()
    }

}

internal fun isPvmLexicalRoot(name: String): Boolean = name.isNotBlank() && name.all { character ->
    character.isLetter() || Character.getType(character) in PVM_COMBINING_MARK_TYPES
}

private val PVM_COMBINING_MARK_TYPES = setOf(
    Character.NON_SPACING_MARK.toInt(),
    Character.COMBINING_SPACING_MARK.toInt(),
    Character.ENCLOSING_MARK.toInt(),
)
