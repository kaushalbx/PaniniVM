package dev.panini.linganushasanam.adhyaya3

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 141: वक्त्रनेत्रारण्यगाण्डीवानि पुंसि च । */
object VaktraNetraAranyaGandivaSutra : LinganushasanaSutra(
    number = "141", text = "वक्त्रनेत्रारण्यगाण्डीवानि पुंसि च ।", hindiExplanation = "वक्त्र, नेत्र, अरण्य और गाण्डीव पुंलिङ्ग तथा नपुंसकलिङ्ग होते हैं।",
    section = LinganushasanaSection.NAPUMSAKALINGA, targetLinga = Linga.NAPUMSAKA, priority = 50,
) {
    override val targetLingas = setOf(Linga.PUMS, Linga.NAPUMSAKA)
    private val members = setOf("वक्त्र", "नेत्र", "अरण्य", "गाण्डीव")
    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}

