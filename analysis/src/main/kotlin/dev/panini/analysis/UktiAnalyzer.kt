package dev.panini.analysis

import dev.panini.vyakaranam.ast.Conditional
import dev.panini.vyakaranam.ast.Invocation
import dev.panini.vyakaranam.ast.Pipeline
import dev.panini.vyakaranam.ast.ProgramNode
import dev.panini.vyakaranam.ast.ProgramNodeVisitor
import dev.panini.vyakaranam.ast.Procedure
import dev.panini.vyakaranam.ast.Quotation
import dev.panini.vyakaranam.ast.Repeat
import dev.panini.vyakaranam.ast.Scope
import dev.panini.vyakaranam.ast.Sequence
import dev.panini.vyakaranam.ast.Ukti
import dev.panini.vyakaranam.ast.invocations
import dev.panini.vyakaranam.ast.accept

data class UktiAnalysis(
    val ukti: Ukti,
    val frames: List<KriyaFrame>,
    val links: List<KriyaLink>,
)

/**
 * Performs the second, utterance-level stage of analysis.
 *
 * A [VakyaAnalyzer] first assigns the padas of each finite clause to that
 * clause's kriya frame. This analyzer then relates those completed frames
 * according to the construction retained by the parser.
 */
class UktiAnalyzer(
    private val analyzeVakya: (dev.panini.vyakaranam.ast.Vakya, KriyaId) -> KriyaFrame,
) {
    constructor(vakyaAnalyzer: VakyaAnalyzer) : this(vakyaAnalyzer::analyze)

    fun analyze(ukti: Ukti): UktiAnalysis {
        val initialFrames = ukti.body.invocations().mapIndexed { index, invocation ->
            analyzeVakya(invocation.vakya, KriyaId("kriya-${index + 1}"))
        }
        val links = buildLinks(ukti, initialFrames)
        return UktiAnalysis(ukti, initialFrames, links)
    }

    private fun buildLinks(ukti: Ukti, frames: List<KriyaFrame>): List<KriyaLink> {
        if (frames.size < 2) return emptyList()
        val structuralLinks = buildStructuralLinks(ukti.body, frames)

        val semanticLinks = buildList {
            frames.zipWithNext().forEach { (source, target) ->
                val sourceText = source.vakya.sourceText
                val isPurvakalika = sourceText.contains("क्त्वा") || sourceText.contains("त्वा") ||
                        sourceText.contains("ल्याप्") || sourceText.endsWith("त्वा") || sourceText.endsWith("या")
                val isPurpose = sourceText.contains("तुमुन्") || sourceText.contains("तुम्") || sourceText.endsWith("तुम्")

                if (isPurvakalika) {
                    add(KriyaLink.Purvakalika(source.id, target.id))
                    val sharedAgent = source.relations.firstOrNull {
                        (it.resolution as? FrameKarakaResolution.Resolved)?.karaka == dev.panini.core.Karaka.KARTR
                    }?.participant?.pada?.sourceText ?: target.relations.firstOrNull {
                        (it.resolution as? FrameKarakaResolution.Resolved)?.karaka == dev.panini.core.Karaka.KARTR
                    }?.participant?.pada?.sourceText
                    if (sharedAgent != null) {
                        add(KriyaLink.SharedParticipant(source.id, target.id, sharedAgent))
                    }
                }
                if (isPurpose) {
                    add(KriyaLink.Purpose(source.id, target.id))
                    val sharedAgent = source.relations.firstOrNull {
                        (it.resolution as? FrameKarakaResolution.Resolved)?.karaka == dev.panini.core.Karaka.KARTR
                    }?.participant?.pada?.sourceText ?: target.relations.firstOrNull {
                        (it.resolution as? FrameKarakaResolution.Resolved)?.karaka == dev.panini.core.Karaka.KARTR
                    }?.participant?.pada?.sourceText
                    if (sharedAgent != null) {
                        add(KriyaLink.SharedParticipant(source.id, target.id, sharedAgent))
                    }
                }
            }
        }

        return structuralLinks + semanticLinks
    }

    private fun buildStructuralLinks(
        root: ProgramNode,
        frames: List<KriyaFrame>,
    ): List<KriyaLink> {
        var nextFrame = 0
        val links = mutableListOf<KriyaLink>()

        val visitor = object : ProgramNodeVisitor<Pair<KriyaId, KriyaId>> {
            private fun visit(node: ProgramNode): Pair<KriyaId, KriyaId> = node.accept(this)

            override fun visitInvocation(node: Invocation): Pair<KriyaId, KriyaId> =
                frames[nextFrame++].id.let { it to it }

            override fun visitSequence(node: Sequence): Pair<KriyaId, KriyaId> {
                val ranges = node.statements.map(::visit)
                ranges.zipWithNext().forEachIndexed { index, (source, target) ->
                    links += KriyaLink.Coordination(
                        source = source.second,
                        target = target.first,
                        connector = node.connectors.getOrElse(index) { "" },
                    )
                }
                return ranges.first().first to ranges.last().second
            }

            override fun visitConditional(node: Conditional): Pair<KriyaId, KriyaId> {
                val condition = visit(node.condition)
                val consequent = visit(node.consequent)
                links += KriyaLink.Condition(condition.second, consequent.first)
                val alternate = node.alternate?.let(::visit)
                if (alternate != null) {
                    links += KriyaLink.Alternative(condition.second, alternate.first)
                }
                return condition.first to (alternate?.second ?: consequent.second)
            }
            override fun visitQuotation(node: Quotation): Pair<KriyaId, KriyaId> = visit(node.reporting)

            override fun visitRepeat(node: Repeat): Pair<KriyaId, KriyaId> = visit(node.body)
            override fun visitPipeline(node: Pipeline): Pair<KriyaId, KriyaId> =
                error("A semantic pipeline does not contain grammatical kriya frames.")
            override fun visitProcedure(node: Procedure): Pair<KriyaId, KriyaId> =
                error("A procedure declaration is not an analyzable utterance.")
            override fun visitScope(node: Scope): Pair<KriyaId, KriyaId> =
                error("A scope declaration is not an analyzable utterance.")
        }

        root.accept(visitor)
        return links
    }
}
