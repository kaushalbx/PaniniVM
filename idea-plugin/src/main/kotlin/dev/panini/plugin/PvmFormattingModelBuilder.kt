package dev.panini.plugin

import com.intellij.formatting.Block
import com.intellij.formatting.ChildAttributes
import com.intellij.formatting.FormattingContext
import com.intellij.formatting.FormattingModel
import com.intellij.formatting.FormattingModelBuilder
import com.intellij.formatting.FormattingModelProvider
import com.intellij.formatting.Indent
import com.intellij.formatting.Spacing
import com.intellij.lang.ASTNode
import com.intellij.psi.TokenType
import com.intellij.psi.formatter.common.AbstractBlock

/** Conservative formatter that changes separators, never grammatical terms. */
class PvmFormattingModelBuilder : FormattingModelBuilder {
    override fun createModel(formattingContext: FormattingContext): FormattingModel {
        val element = formattingContext.psiElement
        val root = PvmFormattingBlock(element.node, null)
        return FormattingModelProvider.createFormattingModelForPsiFile(
            element.containingFile,
            root,
            formattingContext.codeStyleSettings,
        )
    }
}

private class PvmFormattingBlock(
    node: ASTNode,
    private val inheritedIndent: Indent?,
) : AbstractBlock(node, null, null) {
    override fun buildChildren(): List<Block> = node.getChildren(null)
        .asSequence()
        .filter { it.elementType != TokenType.WHITE_SPACE && it.textLength > 0 }
        .map { child -> PvmFormattingBlock(child, childIndent(child)) }
        .toList()

    override fun getIndent(): Indent? = inheritedIndent

    override fun getChildAttributes(newChildIndex: Int): ChildAttributes =
        ChildAttributes(Indent.getNoneIndent(), null)

    override fun isLeaf(): Boolean = node.firstChildNode == null

    override fun getSpacing(child1: Block?, child2: Block): Spacing? {
        val left = (child1 as? PvmFormattingBlock)?.node ?: return null
        val right = (child2 as? PvmFormattingBlock)?.node ?: return null
        if (node.elementType == PvmParserDefinition.FILE &&
            left.elementType in TOP_LEVEL_TYPES && right.elementType in TOP_LEVEL_TYPES
        ) {
            return Spacing.createSpacing(0, 0, 1, true, 1)
        }
        if (left.text.trimEnd().lastOrNull()?.toString() in TERMINATORS) {
            return Spacing.createSpacing(0, 0, 1, true, 1)
        }
        if (left.text == "+" || right.text == "+") {
            return Spacing.createSpacing(1, 1, 0, false, 0)
        }
        if (right.text.trimStart().firstOrNull()?.toString() in TERMINATORS) {
            return Spacing.createSpacing(1, 1, 0, false, 0)
        }
        return null
    }

    private fun childIndent(child: ASTNode): Indent = when (child.elementType) {
        PvmTokenTypes.PRAKRIYA_STATEMENT -> Indent.getNormalIndent()
        else -> Indent.getNoneIndent()
    }

    private companion object {
        val TERMINATORS = setOf("।", "॥", ".")
        val TOP_LEVEL_TYPES = setOf(
            PvmTokenTypes.PRAKRIYA_DECLARATION,
            PvmTokenTypes.SCOPE_DECLARATION,
            PvmTokenTypes.RANGE_DECLARATION,
            PvmTokenTypes.UKTI,
            PvmTokenTypes.PROGRAM_ITEM,
            PvmTokenTypes.UTTERANCE,
        )
    }
}
