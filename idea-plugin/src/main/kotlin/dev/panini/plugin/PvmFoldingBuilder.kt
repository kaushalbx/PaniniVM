package dev.panini.plugin

import com.intellij.lang.ASTNode
import com.intellij.lang.folding.FoldingBuilderEx
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.editor.Document
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import dev.panini.vyakaranam.ast.Prakriya

/** Multiline folds projected from canonical prakriyā declarations. */
class PvmFoldingBuilder : FoldingBuilderEx() {
    override fun buildFoldRegions(
        root: PsiElement,
        document: Document,
        quick: Boolean,
    ): Array<FoldingDescriptor> = PsiTreeUtil.collectElementsOfType(root, PvmPsiElement::class.java)
        .asSequence()
        .filter { it.node.elementType == PvmTokenTypes.PRAKRIYA_DECLARATION }
        .filter { declaration ->
            val startLine = document.getLineNumber(declaration.textRange.startOffset)
            val endOffset = declaration.textRange.endOffset.coerceAtMost(document.textLength)
            val endLine = document.getLineNumber((endOffset - 1).coerceAtLeast(declaration.textRange.startOffset))
            endLine > startLine
        }
        .map { FoldingDescriptor(it.node, it.textRange) }
        .toList()
        .toTypedArray()

    override fun getPlaceholderText(node: ASTNode): String {
        val file = node.psi.containingFile as? PvmFile
        val prakriya = file?.canonicalItemAt(node.startOffset) as? Prakriya
        return prakriya?.let { "प्रक्रिया ${it.nameIdentity} …" } ?: "प्रक्रिया …"
    }

    override fun isCollapsedByDefault(node: ASTNode): Boolean = false
}
