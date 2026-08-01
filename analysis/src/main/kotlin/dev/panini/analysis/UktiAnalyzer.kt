package dev.panini.analysis

import dev.panini.vyakaranam.ast.Ukti
import dev.panini.vyakaranam.ast.UktiStructure

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
        val initialFrames = ukti.vakyas.mapIndexed { index, vakya ->
            analyzeVakya(vakya, KriyaId("kriya-${index + 1}"))
        }
        val links = buildLinks(ukti, initialFrames)
        return UktiAnalysis(ukti, initialFrames, links)
    }

    private fun buildLinks(ukti: Ukti, frames: List<KriyaFrame>): List<KriyaLink> {
        if (frames.size < 2) return emptyList()
        val structuralLinks: List<KriyaLink> = when (val structure = ukti.structure) {
            UktiStructure.Sequence -> frames.zipWithNext().mapIndexed { index, (source, target) ->
                KriyaLink.Coordination(
                    source = source.id,
                    target = target.id,
                    connector = ukti.sambandhas.getOrElse(index) { "" },
                )
            }
            is UktiStructure.Conditional -> buildList<KriyaLink> {
                add(KriyaLink.Condition(frames[0].id, frames[1].id))
                if (structure.hasAlternate && frames.size > 2) {
                    add(KriyaLink.Alternative(frames[0].id, frames[2].id))
                }
            }
        }

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
}
