package dev.panini.linganushasanam.adhyaya3

import dev.panini.core.Linga
import dev.panini.linganushasanam.*
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.toVarnas

/** Liṅgānuśāsanam 143: लोपधः. */
object LopadhaSutra : LinganushasanaSutra(
    number = "143", text = "लोपधः ।", hindiExplanation = "लकारोपध शब्द नपुंसकलिङ्ग होते हैं।",
    section = LinganushasanaSection.NAPUMSAKALINGA, targetLinga = Linga.NAPUMSAKA,
) {
    override fun matches(context: LingaRuleContext): Boolean {
        val varnas = context.pratipadika.toVarnas()
        val upadha = context.upadha?.toVarnas()?.firstOrNull { it is Vyanjana } ?: varnas.dropLast(1).lastOrNull()
        return upadha == Vyanjana.LA
    }
}
