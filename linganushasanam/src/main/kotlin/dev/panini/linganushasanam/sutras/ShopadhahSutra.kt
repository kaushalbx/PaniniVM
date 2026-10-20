package dev.panini.linganushasanam.sutras

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.toVarnas

/** Liṅgānuśāsanam 93: षोपधः. */
object ShopadhahSutra : LinganushasanaSutra(
    number = "93",
    text = "षोपधः",
    hindiExplanation = "षकारोपध अकारान्त शब्द पुंलिङ्ग होते हैं।",
    section = LinganushasanaSection.PUMLINGA,
    targetLingas = setOf(Linga.PUMS),
    priority = 30,
) {
    private val exclusiveNeuters = setOf("शिरीष", "ऋजीष", "अम्बरीष", "पीयूष", "पुरीष", "किल्विष", "कल्माष")

    override fun matches(context: LingaRuleContext): Boolean {
        val varnas = context.pratipadika.toVarnas()
        return !context.pratipadika.phonologicallyIn(exclusiveNeuters) &&
            varnas.takeLast(2) == listOf(Vyanjana.SSA, Svara.A)
    }
}
