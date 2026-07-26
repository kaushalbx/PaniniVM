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
    override fun isAlwaysLeaf(element: StructureViewTreeElement): Boolean = element !is PvmStructureViewElement || element.value !is PvmFile
}

class PvmStructureViewElement(private val element: PsiElement) : StructureViewTreeElement, ItemPresentation {
    override fun getValue(): Any = element
    override fun getPresentation(): ItemPresentation = this
    override fun getPresentableText(): String? = if (element is PsiFile) element.name else element.text
    override fun getLocationString(): String? = null
    override fun getIcon(unused: Boolean): Icon? = PvmIcons.FILE
    override fun navigate(requestFocus: Boolean) {
        (element as? Navigatable)?.navigate(requestFocus)
    }
    override fun canNavigate(): Boolean = (element as? Navigatable)?.canNavigate() == true
    override fun canNavigateToSource(): Boolean = (element as? Navigatable)?.canNavigateToSource() == true

    override fun getChildren(): Array<TreeElement> {
        if (element !is PsiFile) return emptyArray()
        return element.children
            .filter { it.text.isNotBlank() }
            .map { PvmStructureViewElement(it) }
            .toTypedArray()
    }
}
