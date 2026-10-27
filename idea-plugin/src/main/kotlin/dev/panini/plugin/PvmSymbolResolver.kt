package dev.panini.plugin

import dev.panini.execution.PrakriyaInvocationMatcher
import dev.panini.vyakaranam.ast.DocumentSourceSpan
import dev.panini.vyakaranam.ast.Invocation
import dev.panini.vyakaranam.ast.Pipeline
import dev.panini.vyakaranam.ast.Prakriya
import dev.panini.vyakaranam.ast.PrakriyaVisibility
import dev.panini.vyakaranam.ast.ProgramDocument
import dev.panini.vyakaranam.ast.ProgramNode
import dev.panini.vyakaranam.ast.Scope
import dev.panini.vyakaranam.ast.SourceLocatedNode
import dev.panini.vyakaranam.ast.Ukti
import dev.panini.vyakaranam.ast.depthFirst

/** Resolves source navigation directly from the canonical parser AST and its source spans. */
internal object PvmSymbolResolver {
    fun prakriyaDeclarationSpans(document: ProgramDocument): List<DocumentSourceSpan> =
        document.items.indices.mapNotNull(document.prakriyaNameSpans::get)

    fun domainDeclarationSpans(document: ProgramDocument): List<DocumentSourceSpan> =
        document.items.indices.mapNotNull(document.scopeDomainSpans::get)

    fun prakriyaReferenceSpans(document: ProgramDocument): List<DocumentSourceSpan> =
        pipelineStageSpans(document).flatMap { (stage, stageSpan) ->
            val source = document.sourceText.substring(stageSpan.start, stageSpan.endExclusive)
            locateAll(source, stageSpan, stage.operationStem)
        }.distinct()

    fun domainReferenceSpans(document: ProgramDocument): List<DocumentSourceSpan> = buildList {
        pipelineStageSpans(document).forEach { (stage, stageSpan) ->
            val domain = stage.domainStem ?: return@forEach
            val source = document.sourceText.substring(stageSpan.start, stageSpan.endExclusive)
            addAll(locateAll(source, stageSpan, domain))
        }
        document.items.forEachIndexed { index, item ->
            val declaration = item as? Prakriya ?: return@forEachIndexed
            val domain = declaration.domain ?: return@forEachIndexed
            val headerSpan = document.prakriyaHeaderSpans[index] ?: return@forEachIndexed
            val header = document.sourceText.substring(headerSpan.start, headerSpan.endExclusive)
            addAll(locateAll(header, headerSpan, domain))
        }
    }.distinct()

    fun prakriyaDeclarationOffsets(document: ProgramDocument, sourceOffset: Int): List<Int> {
        val itemIndex = document.itemSpans.indexOfFirst { sourceOffset in it.start until it.endExclusive }
        if (itemIndex < 0) return emptyList()

        val reference = pipelineReferenceAt(document, itemIndex, sourceOffset) ?: return emptyList()
        return prakriyaDeclarationOffsets(
            document, reference.operationStem, reference.domainStem, includeInternal = true,
        )
    }

    fun prakriyaReferenceIdentityAt(
        document: ProgramDocument,
        sourceOffset: Int,
        knownOperationStems: Set<String> = emptySet(),
    ): Pair<String, String?>? {
        val itemIndex = document.itemSpans.indexOfFirst { sourceOffset in it.start until it.endExclusive }
        if (itemIndex < 0) return null
        pipelineReferenceAt(document, itemIndex, sourceOffset)?.let { return it.operationStem to it.domainStem }
        if (knownOperationStems.isEmpty()) return null
        return invocationReferenceAt(document, itemIndex, sourceOffset, knownOperationStems)
    }

    fun prakriyaIdentityAt(
        document: ProgramDocument,
        sourceOffset: Int,
        knownOperationStems: Set<String>,
    ): Pair<String, String?>? {
        var activeDomain: String? = null
        document.items.forEachIndexed { index, item ->
            if (item is Scope) {
                activeDomain = item.domainIdentity
                return@forEachIndexed
            }
            val declaration = item as? Prakriya ?: return@forEachIndexed
            val nameSpan = document.prakriyaNameSpans[index] ?: return@forEachIndexed
            if (sourceOffset in nameSpan.start until nameSpan.endExclusive) {
                return declaration.nameIdentity to (declaration.domainIdentity ?: activeDomain)
            }
        }
        return prakriyaReferenceIdentityAt(document, sourceOffset, knownOperationStems)
    }

    /** Invocation identity plus the source-order argument under the caret, if any. */
    fun prakriyaCallContextAt(
        document: ProgramDocument,
        sourceOffset: Int,
        knownOperationStems: Set<String>,
    ): Pair<Pair<String, String?>, Int?>? {
        prakriyaIdentityAt(document, sourceOffset, knownOperationStems)?.let {
            return it to null
        }
        val itemIndex = document.itemSpans.indexOfFirst { sourceOffset in it.start until it.endExclusive }
        if (itemIndex < 0) return null
        val itemSpan = document.itemSpans[itemIndex]
        val itemSource = document.sourceText.substring(itemSpan.start, itemSpan.endExclusive)
        for (invocation in roots(document.items[itemIndex]).asSequence()
            .flatMap { it.depthFirst(includeLoopConditions = true) }
            .filterIsInstance<Invocation>()) {
            val shape = runCatching {
                PrakriyaInvocationMatcher.match(
                    Ukti(invocation.sourceText, body = invocation), knownOperationStems,
                )
            }.getOrNull() ?: continue
            val argumentIndex = shape.argumentPadas.indexOfFirst { pada ->
                val span = (pada as? SourceLocatedNode)?.sourceSpan ?: return@indexOfFirst false
                sourceOffset in span.start until span.endExclusive
            }
            if (argumentIndex >= 0) {
                return (shape.operationStem to shape.domainStem) to argumentIndex
            }
            for (invocationSpan in locateAll(itemSource, itemSpan, invocation.sourceText)) {
                val invocationSource = document.sourceText.substring(
                    invocationSpan.start, invocationSpan.endExclusive,
                )
                if (locateAll(invocationSource, invocationSpan, shape.operationStem).any {
                        sourceOffset in it.start until it.endExclusive
                    }) {
                    return (shape.operationStem to shape.domainStem) to null
                }
            }
        }
        return null
    }

    fun prakriyaDeclarationOffsets(
        document: ProgramDocument,
        operationStem: String,
        domainStem: String?,
        includeInternal: Boolean,
    ): List<Int> {
        var activeDomain: String? = null
        return document.items.mapIndexedNotNull { index, item ->
            if (item is Scope) {
                activeDomain = item.domainIdentity
                return@mapIndexedNotNull null
            }
            val declaration = item as? Prakriya ?: return@mapIndexedNotNull null
            val effectiveDomain = declaration.domainIdentity ?: activeDomain
            if (declaration.nameIdentity != operationStem || effectiveDomain != domainStem) {
                return@mapIndexedNotNull null
            }
            if (!includeInternal && declaration.modifiers.visibility == PrakriyaVisibility.INTERNAL) {
                return@mapIndexedNotNull null
            }
            document.prakriyaNameSpans[index]?.start ?: document.itemSpans.getOrNull(index)?.start
        }.distinct()
    }

    private fun pipelineReferenceAt(
        document: ProgramDocument,
        itemIndex: Int,
        sourceOffset: Int,
    ): dev.panini.vyakaranam.ast.PipelineStage? =
        pipelineStageSpans(document, itemIndex).firstOrNull { (stage, span) ->
            val source = document.sourceText.substring(span.start, span.endExclusive)
            locateAll(source, span, stage.operationStem).any {
                sourceOffset in it.start until it.endExclusive
            }
        }?.first

    private fun invocationReferenceAt(
        document: ProgramDocument,
        itemIndex: Int,
        sourceOffset: Int,
        knownOperationStems: Set<String>,
    ): Pair<String, String?>? {
        val itemSpan = document.itemSpans[itemIndex]
        val itemSource = document.sourceText.substring(itemSpan.start, itemSpan.endExclusive)
        for (invocation in roots(document.items[itemIndex]).asSequence()
            .flatMap { it.depthFirst(includeLoopConditions = true) }
            .filterIsInstance<Invocation>()) {
            val shape = runCatching {
                PrakriyaInvocationMatcher.match(
                    Ukti(invocation.sourceText, body = invocation), knownOperationStems,
                )
            }.getOrNull() ?: continue
            for (invocationSpan in locateAll(itemSource, itemSpan, invocation.sourceText)) {
                val invocationSource = document.sourceText.substring(
                    invocationSpan.start, invocationSpan.endExclusive,
                )
                val operationSpans = locateAll(invocationSource, invocationSpan, shape.operationStem)
                if (operationSpans.any { sourceOffset in it.start until it.endExclusive }) {
                    return shape.operationStem to shape.domainStem
                }
            }
        }
        return null
    }

    private fun pipelineStageSpans(
        document: ProgramDocument,
        onlyItemIndex: Int? = null,
    ): List<Pair<dev.panini.vyakaranam.ast.PipelineStage, DocumentSourceSpan>> {
        val itemIndexes = onlyItemIndex?.let(::listOf) ?: document.items.indices.toList()
        return itemIndexes.flatMap { itemIndex -> pipelineStageSpans(document, itemIndex) }.distinct()
    }

    private fun pipelineStageSpans(
        document: ProgramDocument,
        itemIndex: Int,
    ): List<Pair<dev.panini.vyakaranam.ast.PipelineStage, DocumentSourceSpan>> {
        val item = document.items[itemIndex]
        val itemSpan = document.itemSpans[itemIndex]
        val roots = roots(item)
        val itemSource = document.sourceText.substring(itemSpan.start, itemSpan.endExclusive)

        return buildList {
          for (pipeline in roots.asSequence().flatMap { it.depthFirst() }.filterIsInstance<Pipeline>()) {
            val pipelineRange = locate(itemSource, itemSpan, pipeline.sourceText) ?: continue
            val pipelineSource = document.sourceText.substring(pipelineRange.start, pipelineRange.endExclusive)
            for (stage in pipeline.stages) {
                locateAll(pipelineSource, pipelineRange, stage.sourceText).forEach { add(stage to it) }
            }
          }
        }
    }

    private fun roots(item: dev.panini.vyakaranam.ast.VyakaranamNode): List<ProgramNode> = when (item) {
        is Ukti -> listOf(item.body)
        is Prakriya -> item.body
        is Scope -> item.body
        else -> emptyList()
    }

    /** ANTLR context text omits editor whitespace; retain an index map back to the original source. */
    private fun locate(containerText: String, containerSpan: DocumentSourceSpan, text: String): DocumentSourceSpan? =
        locateAll(containerText, containerSpan, text).firstOrNull()

    private fun locateAll(
        containerText: String,
        containerSpan: DocumentSourceSpan,
        text: String,
    ): List<DocumentSourceSpan> {
        val sourceIndexes = containerText.indices.filterNot { containerText[it].isWhitespace() }
        val compactContainer = buildString(sourceIndexes.size) {
            sourceIndexes.forEach { append(containerText[it]) }
        }
        val compactNeedle = text.filterNot(Char::isWhitespace)
        if (compactNeedle.isEmpty()) return emptyList()

        return buildList {
            var searchFrom = 0
            while (searchFrom <= compactContainer.length - compactNeedle.length) {
                val compactStart = compactContainer.indexOf(compactNeedle, searchFrom)
                if (compactStart < 0) break
                val sourceStart = sourceIndexes[compactStart]
                val sourceEnd = sourceIndexes[compactStart + compactNeedle.lastIndex] + 1
                add(DocumentSourceSpan(containerSpan.start + sourceStart, containerSpan.start + sourceEnd))
                searchFrom = compactStart + 1
            }
        }
    }
}
