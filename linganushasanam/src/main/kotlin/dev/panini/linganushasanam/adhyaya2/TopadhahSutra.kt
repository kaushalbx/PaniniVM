package dev.panini.linganushasanam.adhyaya2

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.toVarnas

/** Liṅgānuśāsanam 64: टोपधः. */
object TopadhahSutra : LinganushasanaSutra(
    number = "64",
    text = "टोपधः",
    hindiExplanation = "टकारोपध अकारान्त शब्द पुंलिङ्ग होते हैं।",
    section = LinganushasanaSection.PUMLINGA,
    targetLinga = Linga.PUMS,
    priority = 30,
) {
    private val exclusiveNeuters = setOf("किरीट", "मुकुट", "ललाट", "वट", "वीट", "शृङ्गाटक", "रट", "लोष्ट")

    override fun matches(context: LingaRuleContext): Boolean {
        val varnas = context.pratipadika.toVarnas()
        return context.pratipadika !in exclusiveNeuters &&
            varnas.takeLast(2) == listOf(Vyanjana.TTA, Svara.A)
    }
}
