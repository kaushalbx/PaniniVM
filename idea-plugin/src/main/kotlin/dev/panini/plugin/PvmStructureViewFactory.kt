package dev.panini.plugin

import com.intellij.ide.structureView.StructureViewBuilder
import com.intellij.ide.structureView.StructureViewModel
import com.intellij.ide.structureView.StructureViewModelBase
import com.intellij.ide.structureView.StructureViewTreeElement
import com.intellij.ide.structureView.TreeBasedStructureViewBuilder
import com.intellij.ide.util.treeView.smartTree.TreeElement
import com.intellij.lang.PsiStructureViewFactory
import com.intellij.navigation.ItemPresentation
import com.intellij.openapi.editor.Editor
import com.intellij.pom.Navigatable
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import dev.panini.vyakaranam.ast.Prakriya
import dev.panini.vyakaranam.ast.RangeDeclaration
import dev.panini.vyakaranam.ast.Scope
import dev.panini.vyakaranam.ast.Ukti
import javax.swing.Icon

class PvmStructureViewFactory : PsiStructureViewFactory {
    override fun getStructureViewBuilder(psiFile: PsiFile): StructureViewBuilder {
        return object : TreeBasedStructureViewBuilder() {
            override fun createStructureViewModel(editor: Editor?): StructureViewModel {
                return PvmStructureViewModel(psiFile, editor)
            }
        }
    }
}

class PvmStructureViewModel(psiFile: PsiFile, editor: Editor?) :
    StructureViewModelBase(psiFile, editor, PvmStructureViewElement(psiFile)),
    StructureViewModel.ElementInfoProvider {

    override fun isAlwaysShowsPlus(element: StructureViewTreeElement): Boolean = false
    override fun isAlwaysLeaf(element: StructureViewTreeElement): Boolean =
        element !is PvmStructureViewElement || element.children.isEmpty()
}

class PvmStructureViewElement(private val element: PsiElement) : StructureViewTreeElement, ItemPresentation {
    override fun getValue(): Any = element
    override fun getPresentation(): ItemPresentation = this
    override fun getPresentableText(): String? = when {
        element is PsiFile -> element.name
        element.node.elementType in TOP_LEVEL_TYPES -> canonicalLabel() ?: compact(element.text, 100)
        element.node.elementType in PRESENTATION_CHILD_TYPES -> compact(element.text, 60)
        else -> compact(element.text, 60)
    }
    override fun getLocationString(): String? = when {
        element.node.elementType in TOP_LEVEL_TYPES -> canonicalKind() ?: "statement"
        element.node.elementType == PvmTokenTypes.PRAKRIYA_HEADER -> "header"
        element.node.elementType == PvmTokenTypes.PRAKRIYA_STATEMENT -> "body"
        element.node.elementType == PvmTokenTypes.PRAKRIYA_NAME -> "name"
        element.node.elementType == PvmTokenTypes.SCOPE_DOMAIN -> "domain"
        element.node.elementType == PvmTokenTypes.SUBANTA -> "subanta"
        element.node.elementType == PvmTokenTypes.TINGANTA -> "tinganta"
        element.node.elementType == PvmTokenTypes.GRAMMATICAL_TERM -> "pada"
        else -> null
    }
    override fun getIcon(unused: Boolean): Icon? = PvmIcons.FILE
    override fun navigate(requestFocus: Boolean) {
        (element as? Navigatable)?.navigate(requestFocus)
    }
    override fun canNavigate(): Boolean = (element as? Navigatable)?.canNavigate() == true
    override fun canNavigateToSource(): Boolean = (element as? Navigatable)?.canNavigateToSource() == true

    override fun getChildren(): Array<TreeElement> {
        if (element !is PsiFile && element.node.elementType !in CONTAINER_TYPES) return emptyArray()
        val acceptedTypes = when {
            element is PsiFile -> TOP_LEVEL_TYPES
            element.node.elementType == PvmTokenTypes.PRAKRIYA_DECLARATION -> DECLARATION_CHILD_TYPES
            element.node.elementType == PvmTokenTypes.SCOPE_DECLARATION -> setOf(PvmTokenTypes.SCOPE_DOMAIN)
            element.node.elementType == PvmTokenTypes.PRAKRIYA_HEADER -> setOf(PvmTokenTypes.PRAKRIYA_NAME)
            element.node.elementType == PvmTokenTypes.PRAKRIYA_STATEMENT -> GRAMMATICAL_NODE_TYPES
            else -> setOf(PvmTokenTypes.GRAMMATICAL_TERM)
        }
        return element.children
            .filter { it.node.elementType in acceptedTypes && it.text.isNotBlank() }
            .map { PvmStructureViewElement(it) }
            .toTypedArray()
    }

    private fun compact(text: String, limit: Int): String {
        val normalized = text.replace(Regex("\\s+"), " ").trim()
        return if (normalized.length <= limit) normalized else normalized.take(limit - 1) + "…"
    }

    private fun canonicalItem() =
        (element.containingFile as? PvmFile)?.canonicalItemAt(element.textRange.startOffset)

    private fun canonicalLabel(): String? = when (val item = canonicalItem()) {
        is Prakriya -> "प्रक्रिया ${compact(item.name, 70)}"
        is Scope -> "अधिकार ${compact(item.domain, 70)}"
        is RangeDeclaration -> "सीमा ${compact(item.sourceText, 70)}"
        is Ukti -> compact(item.sourceText, 100)
        else -> item?.sourceText?.let { compact(it, 100) }
    }

    private fun canonicalKind(): String? = when (canonicalItem()) {
        is Prakriya -> "prakriyā"
        is Scope -> "adhikāra"
        is RangeDeclaration -> "range"
        is Ukti -> "ukti"
        null -> null
        else -> "statement"
    }

    private companion object {
        val TOP_LEVEL_TYPES = setOf(
            PvmTokenTypes.PRAKRIYA_DECLARATION,
            PvmTokenTypes.SCOPE_DECLARATION,
            PvmTokenTypes.RANGE_DECLARATION,
            PvmTokenTypes.UKTI,
            PvmTokenTypes.PROGRAM_ITEM,
            PvmTokenTypes.UTTERANCE,
        )
        val DECLARATION_CHILD_TYPES = setOf(
            PvmTokenTypes.PRAKRIYA_HEADER,
            PvmTokenTypes.PRAKRIYA_STATEMENT,
        )
        val GRAMMATICAL_NODE_TYPES = setOf(PvmTokenTypes.SUBANTA, PvmTokenTypes.TINGANTA)
        val PRESENTATION_CHILD_TYPES = DECLARATION_CHILD_TYPES + GRAMMATICAL_NODE_TYPES + setOf(
            PvmTokenTypes.PRAKRIYA_NAME,
            PvmTokenTypes.SCOPE_DOMAIN,
            PvmTokenTypes.GRAMMATICAL_TERM,
        )
        val CONTAINER_TYPES = setOf(
            PvmTokenTypes.PRAKRIYA_DECLARATION,
            PvmTokenTypes.SCOPE_DECLARATION,
            PvmTokenTypes.PRAKRIYA_HEADER,
            PvmTokenTypes.PRAKRIYA_STATEMENT,
            PvmTokenTypes.UTTERANCE,
        )
    }
}
