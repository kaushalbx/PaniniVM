package dev.panini.plugin

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.search.LocalSearchScope
import com.intellij.psi.search.SearchScope
import com.intellij.psi.search.searches.ReferencesSearch
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.Processor
import com.intellij.util.QueryExecutor

/** Finds positional parameter uses whose source spelling cannot be found by a word index. */
class PvmPrakriyaParameterReferencesSearch :
    QueryExecutor<PsiReference, ReferencesSearch.SearchParameters> {

    override fun execute(
        parameters: ReferencesSearch.SearchParameters,
        consumer: Processor<in PsiReference>,
    ): Boolean {
        val target = parameterTarget(parameters.elementToSearch) ?: return true
        val targetFile = target.containingFile as? PvmFile ?: return true
        if (PvmPrakriyaCallSemantics.declaredParameterAt(targetFile, target.textOffset) == null) return true

        for ((candidate, _) in PvmProjectFileIndex.documents(targetFile)) {
            if (!parameters.effectiveSearchScope.contains(candidate)) continue
            for (argument in PsiTreeUtil.findChildrenOfType(candidate, PvmInvocationArgumentPsi::class.java)) {
                for (reference in argument.references) {
                    if (reference.isReferenceTo(target) && !consumer.process(reference)) return false
                }
            }
        }
        return true
    }

    private fun parameterTarget(element: PsiElement): PsiElement? = when (element) {
        is PvmPrakriyaParameterPsi -> element
        else -> PsiTreeUtil.getParentOfType(element, PvmPrakriyaParameterPsi::class.java, false)
            ?: (element as? PvmSubantaPsi)
            ?: PsiTreeUtil.getParentOfType(element, PvmSubantaPsi::class.java, false)
    }

    private fun SearchScope.contains(file: PvmFile): Boolean = when (this) {
        is GlobalSearchScope -> file.virtualFile?.let(::contains) == true
        is LocalSearchScope -> scope.any { it.containingFile == file || it == file }
        else -> true
    }
}
