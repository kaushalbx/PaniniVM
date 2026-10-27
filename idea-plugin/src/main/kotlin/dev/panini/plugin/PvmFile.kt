package dev.panini.plugin

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider
import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager
import com.intellij.psi.util.PsiModificationTracker
import dev.panini.vyakaranam.ast.ProgramDocument
import dev.panini.vyakaranam.ast.Pada
import dev.panini.vyakaranam.ast.SourceLocatedNode
import dev.panini.vyakaranam.ast.VyakaranamNode
import dev.panini.vyakaranam.parser.PaniniParser

class PvmFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, PvmLanguage.INSTANCE) {
    override fun getFileType(): FileType = PvmFileType.INSTANCE
    override fun toString(): String = "PaniniVM File"

    /**
     * Canonical typed AST for semantic IDE features. IntelliJ PSI remains an
     * editable source tree; it must not grow a second set of grammatical models.
     */
    fun canonicalDocument(): ProgramDocument? = CachedValuesManager.getCachedValue(this) {
        CachedValueProvider.Result.create(
            runCatching { PaniniParser().parseDocument(text) }.getOrNull(),
            PsiModificationTracker.MODIFICATION_COUNT,
        )
    }

    /** Resolve an editor/PSI offset to the canonical typed top-level node. */
    fun canonicalItemAt(offset: Int): VyakaranamNode? {
        val document = canonicalDocument() ?: return null
        val index = document.itemSpans.indexOfFirst { offset >= it.start && offset < it.endExclusive }
        return document.items.getOrNull(index)
    }

    /** Resolve an offset to the smallest source-located canonical grammatical pada. */
    fun canonicalPadaAt(offset: Int): Pada? = canonicalItemAt(offset)
        ?.canonicalLocatedPadas()
        ?.filter { pada ->
            val span = (pada as SourceLocatedNode).sourceSpan ?: return@filter false
            offset >= span.start && offset < span.endExclusive
        }
        ?.minByOrNull { pada ->
            val span = (pada as SourceLocatedNode).sourceSpan!!
            span.endExclusive - span.start
        }
}
