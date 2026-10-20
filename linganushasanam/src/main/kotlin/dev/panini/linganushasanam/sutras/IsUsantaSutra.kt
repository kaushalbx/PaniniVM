package dev.panini.linganushasanam.sutras

import dev.panini.core.Linga
import dev.panini.linganushasanam.*
import dev.panini.shiksha.*

/** Liṅgānuśāsanam 134: इसुसन्तः. */
object IsUsantaSutra : LinganushasanaSutra(
    number = "134", text = "इसुसन्तः", hindiExplanation = "इस् अथवा उस् अन्त वाले शब्द नपुंसकलिङ्ग होते हैं।",
    section = LinganushasanaSection.NAPUMSAKALINGA, targetLingas = setOf(Linga.NAPUMSAKA), priority = 30,
) {
    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.toVarnas().takeLast(2) in setOf(
        listOf(Svara.I, Vyanjana.SA), listOf(Svara.U, Vyanjana.SA),
    )
}
