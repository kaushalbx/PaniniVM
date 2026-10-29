package dev.panini.plugin

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider
import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager
import com.intellij.openapi.util.ModificationTracker
import com.intellij.openapi.util.Key
import com.intellij.psi.util.CachedValue
import dev.panini.vyakaranam.ast.ProgramDocument
import dev.panini.vyakaranam.ast.Pada
import dev.panini.vyakaranam.ast.Invocation
import dev.panini.vyakaranam.ast.SourceLocatedNode
import dev.panini.vyakaranam.ast.VyakaranamNode
import dev.panini.vyakaranam.ast.grammaticalInvocations
import dev.panini.vyakaranam.ast.relationSpan
import dev.panini.vyakaranam.parser.PaniniParser
import dev.panini.analysis.UktiAnalysis
import dev.panini.analysis.KarakaRelation
import dev.panini.analysis.KriyaFrame
import dev.panini.execution.PvmScript
import dev.panini.execution.PvmScriptStatement
import dev.panini.execution.binding.VyakaranamExecutionAdapter

class PvmFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, PvmLanguage.INSTANCE) {
    private val sourceModificationTracker = ModificationTracker { this.viewProvider.modificationStamp }

    override fun getFileType(): FileType = PvmFileType.INSTANCE
    override fun toString(): String = "PaniniVM File"

    /**
     * Canonical typed AST for semantic IDE features. IntelliJ PSI remains an
     * editable source tree; it must not grow a second set of grammatical models.
     */
    fun canonicalDocument(): ProgramDocument? = CachedValuesManager.getCachedValue(this) {
        CachedValueProvider.Result.create(
            runCatching { PaniniParser().parseDocument(text) }.getOrNull(),
            sourceModificationTracker,
        )
    }

    /** Shared canonical sentence analysis for annotations, docs, and navigation. */
    fun canonicalGrammarAnalyses(): List<UktiAnalysis> = CachedValuesManager.getManager(project).getCachedValue(
        this,
        GRAMMAR_ANALYSES_KEY,
        {
            val analyses = canonicalDocument()?.let { document ->
                PvmScript.fromDocument(document).flatMap { statement ->
                    when (statement) {
                        is PvmScriptStatement.Sentence -> listOf(statement)
                        is PvmScriptStatement.PrakriyaDefinition -> statement.body
                        else -> emptyList()
                    }
                }.mapNotNull { sentence ->
                    sentence.ukti?.let {
                        runCatching { VyakaranamExecutionAdapter.analyzeGrammar(it) }.getOrNull()
                    }
                }
            }.orEmpty()
            CachedValueProvider.Result.create(analyses, sourceModificationTracker)
        },
        false,
    )

    /** Resolve an editor offset to its analyzed kriyā frame and optional kāraka relation. */
    fun canonicalSemanticFrameAt(offset: Int): Pair<KriyaFrame, KarakaRelation?>? {
        for (analysis in canonicalGrammarAnalyses()) {
            for (frame in analysis.frames) {
                frame.relations.firstOrNull { relation ->
                    (relation.participant.pada as? SourceLocatedNode)?.sourceSpan?.let {
                        offset >= it.start && offset < it.endExclusive
                    } == true
                }?.let { return frame to it }
                val kriyaSpan = (frame.kriya?.analysis?.pada as? SourceLocatedNode)?.sourceSpan
                if (kriyaSpan != null && offset >= kriyaSpan.start && offset < kriyaSpan.endExclusive) {
                    return frame to null
                }
            }
        }
        return null
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

    /** Resolve an editor offset to the smallest canonical grammatical invocation. */
    fun canonicalInvocationAt(offset: Int): Invocation? = canonicalItemAt(offset)
        ?.canonicalProgramNodes()
        ?.flatMap { it.grammaticalInvocations() }
        ?.filter { invocation ->
            val span = invocation.relationSpan() ?: return@filter false
            offset >= span.start && offset < span.endExclusive
        }
        ?.minByOrNull { invocation ->
            val span = invocation.relationSpan()!!
            span.endExclusive - span.start
        }

    private companion object {
        val GRAMMAR_ANALYSES_KEY = Key.create<CachedValue<List<UktiAnalysis>>>(
            "dev.panini.plugin.canonicalGrammarAnalyses",
        )
    }
}
