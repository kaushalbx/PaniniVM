package dev.panini.linganushasanam.sutras

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 49: क्रतुपुरुषकपोलगुल्फमेघाभिधानानि । */
object KratuPurusaKapolaGulphaMeghaSutra : LinganushasanaSutra(
    number = "49",
    text = "क्रतुपुरुषकपोलगुल्फमेघाभिधानानि",
    hindiExplanation = "क्रतु, पुरुष, कपोल, गुल्फ और मेघ के अभिधान पुंलिङ्ग होते हैं।",
    section = LinganushasanaSection.PUMLINGA,
    targetLingas = setOf(Linga.PUMS),
    priority = 50,
) {
    private val members = setOf("क्रतु", "पुरुष", "कपोल", "गुल्फ", "मेघ")

    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}

