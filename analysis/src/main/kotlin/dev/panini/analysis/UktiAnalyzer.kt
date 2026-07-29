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
    private val analyzeVakya: (dev.panini.vyakaranam.ast.Vakya, KriyaId) -> KriyaFrame?,
) {
    constructor(vakyaAnalyzer: VakyaAnalyzer) : this(vakyaAnalyzer::analyze)

    fun analyze(ukti: Ukti): UktiAnalysis {
        val initialFrames = ukti.vakyas.mapIndexedNotNull { index, vakya ->
            analyzeVakya(vakya, KriyaId("kriya-${index + 1}"))
        }
        val links = buildLinks(ukti, initialFrames)
        val frames = initialFrames.map { frame ->
            frame.copy(links = links.filter { it.source == frame.id || it.target == frame.id })
        }
        return UktiAnalysis(ukti, frames, links)
    }

    private fun buildLinks(ukti: Ukti, frames: List<KriyaFrame>): List<KriyaLink> {
        if (frames.size < 2) return emptyList()
        return when (val structure = ukti.structure) {
            UktiStructure.Sequence -> frames.zipWithNext().mapIndexed { index, (source, target) ->
                KriyaLink.Coordination(
                    source = source.id,
                    target = target.id,
                    connector = ukti.sambandhas.getOrElse(index) { "" },
                )
            }
            is UktiStructure.Conditional -> buildList {
                add(KriyaLink.Condition(frames[0].id, frames[1].id))
                if (structure.hasAlternate && frames.size > 2) {
                    add(KriyaLink.Alternative(frames[0].id, frames[2].id))
                }
            }
        }
    }
}
