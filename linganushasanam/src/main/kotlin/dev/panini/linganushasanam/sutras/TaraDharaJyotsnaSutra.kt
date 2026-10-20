package dev.panini.linganushasanam.sutras

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 33: ताराधाराज्योत्स्नादयश्च । */
object TaraDharaJyotsnaSutra : LinganushasanaSutra(
    number = "33",
    text = "ताराधाराज्योत्स्नादयश्च",
    hindiExplanation = "तारा, धारा, ज्योत्स्ना आदि शब्द स्त्रीलिङ्ग होते हैं।",
    section = LinganushasanaSection.STRILINGA,
    targetLingas = setOf(Linga.STRI),
    priority = 50,
) {
    private val members = setOf("तारा", "धारा", "ज्योत्स्ना")

    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}

