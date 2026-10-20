package dev.panini.linganushasanam.adhyaya1

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.shiksha.Svara
import dev.panini.shiksha.toVarnas

/** Liṅgānuśāsanam 10: ईकारान्तश्च. */
object IkarantaSutra : LinganushasanaSutra(
    number = "10",
    text = "ईकारान्तश्च ।",
    hindiExplanation = "ईकारान्त शब्द स्त्रीलिङ्ग होते हैं।",
    section = LinganushasanaSection.STRILINGA,
    targetLinga = Linga.STRI,
    priority = 30,
) {
    override fun matches(context: LingaRuleContext): Boolean =
        context.pratipadika.toVarnas().lastOrNull() == Svara.II
}
