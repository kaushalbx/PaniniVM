package dev.panini.plugin

import com.intellij.lang.ASTNode
import com.intellij.lang.PsiBuilder
import com.intellij.lang.PsiParser
import com.intellij.openapi.progress.ProgressManager
import com.intellij.psi.tree.IElementType
import dev.panini.vyakaranam.ast.DocumentSourceSpan
import dev.panini.vyakaranam.ast.Prakriya
import dev.panini.vyakaranam.ast.ProgramDocument
import dev.panini.vyakaranam.ast.RangeDeclaration
import dev.panini.vyakaranam.ast.Scope
import dev.panini.vyakaranam.ast.SourceLocatedNode
import dev.panini.vyakaranam.ast.TingantaPada
import dev.panini.vyakaranam.ast.Ukti
import dev.panini.vyakaranam.ast.VyakaranamNode
import dev.panini.vyakaranam.parser.PaniniParser

class PvmPsiParser : PsiParser {
    override fun parse(root: IElementType, builder: PsiBuilder): ASTNode {
        val rootMarker = builder.mark()
        val source = builder.originalText.toString()
        val document = parseCanonical(source)
        if (document != null && document.items.size == document.itemSpans.size) {
            parseCanonicalDocument(builder, document)
            while (!builder.eof()) builder.advanceLexer()
        } else {
            longestCanonicalPrefix(source)?.let { parseCanonicalDocument(builder, it) }
            while (!builder.eof()) parseUtterance(builder)
        }
        rootMarker.done(root)
        return builder.treeBuilt
    }

    private fun parseCanonicalDocument(builder: PsiBuilder, document: ProgramDocument) {
        document.items.zip(document.itemSpans).forEachIndexed { index, (item, span) ->
            consumeUntil(builder, span.start)
            if (builder.eof()) return@forEachIndexed

            val itemMarker = builder.mark()
            val children = canonicalChildren(document, index, item, span)
            children.forEach { child ->
                consumeUntil(builder, child.span.start)
                if (!builder.eof() && builder.currentOffset < child.span.endExclusive) {
                    parseRegion(builder, child)
                }
            }
            consumeUntil(builder, span.endExclusive)
            itemMarker.done(elementType(item))
        }
    }

    private fun parseCanonical(source: String): ProgramDocument? = runCatching {
        PaniniParser().parseDocument(source)
    }.getOrNull()?.takeIf { it.items.size == it.itemSpans.size }

    /**
     * Keep completed top-level forms structural while the final form is being
     * edited. Candidate boundaries come from the language's own terminators;
     * the returned tree is still produced exclusively by the canonical parser.
     */
    private fun longestCanonicalPrefix(source: String): ProgramDocument? {
        val boundaries = source.indices
            .filter { source[it] == '।' || source[it] == '॥' || source[it] == '.' }
            .asReversed()
            .take(MAX_RECOVERY_BOUNDARIES)
        for (index in boundaries) {
            ProgressManager.checkCanceled()
            val prefix = source.substring(0, index + 1)
            val document = parseCanonical(prefix) ?: continue
            if (document.items.isNotEmpty()) return document
        }
        return null
    }

    private fun canonicalChildren(
        document: ProgramDocument,
        itemIndex: Int,
        item: VyakaranamNode,
        itemSpan: DocumentSourceSpan,
    ): List<CanonicalRegion> = buildList {
        document.prakriyaHeaderSpans[itemIndex]?.let {
            val name = document.prakriyaNameSpans[itemIndex]
            add(CanonicalRegion(
                it,
                PvmTokenTypes.PRAKRIYA_HEADER,
                listOfNotNull(name?.let { span -> CanonicalRegion(span, PvmTokenTypes.PRAKRIYA_NAME) }),
            ))
        }
        document.prakriyaBodySpans[itemIndex].orEmpty().forEach {
            add(CanonicalRegion(it, PvmTokenTypes.PRAKRIYA_STATEMENT, padaRegions(item, it)))
        }
        document.scopeDomainSpans[itemIndex]?.let {
            add(CanonicalRegion(it, PvmTokenTypes.SCOPE_DOMAIN))
        }
        if (document.prakriyaBodySpans[itemIndex].isNullOrEmpty()) {
            addAll(padaRegions(item, itemSpan))
        }
    }.filter {
        it.span.start >= itemSpan.start && it.span.endExclusive <= itemSpan.endExclusive
    }.sortedBy { it.span.start }

    private fun parseRegion(builder: PsiBuilder, region: CanonicalRegion) {
        val marker = builder.mark()
        region.children.forEach { child ->
            consumeUntil(builder, child.span.start)
            if (!builder.eof() && builder.currentOffset < child.span.endExclusive) parseRegion(builder, child)
        }
        consumeUntil(builder, region.span.endExclusive)
        marker.done(region.type)
    }

    private fun padaRegions(item: VyakaranamNode, within: DocumentSourceSpan): List<CanonicalRegion> =
        item.canonicalLocatedPadas()
            .mapNotNull { pada ->
                val span = (pada as SourceLocatedNode).sourceSpan ?: return@mapNotNull null
                if (span.start < within.start || span.endExclusive > within.endExclusive) return@mapNotNull null
                CanonicalRegion(
                    span,
                    if (pada is TingantaPada) PvmTokenTypes.TINGANTA else PvmTokenTypes.SUBANTA,
                )
            }
            .distinctBy { it.span }
            .sortedBy { it.span.start }

    private fun consumeUntil(builder: PsiBuilder, endOffset: Int) {
        while (!builder.eof() && builder.currentOffset < endOffset) builder.advanceLexer()
    }

    private fun elementType(item: VyakaranamNode): IElementType = when (item) {
        is Prakriya -> PvmTokenTypes.PRAKRIYA_DECLARATION
        is Scope -> PvmTokenTypes.SCOPE_DECLARATION
        is RangeDeclaration -> PvmTokenTypes.RANGE_DECLARATION
        is Ukti -> PvmTokenTypes.UKTI
        else -> PvmTokenTypes.PROGRAM_ITEM
    }

    private data class CanonicalRegion(
        val span: DocumentSourceSpan,
        val type: IElementType,
        val children: List<CanonicalRegion> = emptyList(),
    )

    private fun parseUtterance(builder: PsiBuilder) {
        val utterance = builder.mark()
        while (!builder.eof()) {
            parseTerm(builder)
            if (builder.eof()) break

            val separator = builder.tokenText
            builder.advanceLexer()
            if (separator == "।" || separator == "॥" || separator == ".") break
        }
        utterance.done(PvmTokenTypes.UTTERANCE)
    }

    private fun parseTerm(builder: PsiBuilder) {
        val term = builder.mark()
        var consumed = false
        while (!builder.eof()) {
            val text = builder.tokenText
            if (text == "+" || text == "।" || text == "॥" || text == ".") break
            builder.advanceLexer()
            consumed = true
        }
        if (consumed) term.done(PvmTokenTypes.GRAMMATICAL_TERM) else term.drop()
    }

    private companion object {
        /** Bounds reparsing work for severely malformed large editor buffers. */
        const val MAX_RECOVERY_BOUNDARIES = 64
    }
}
