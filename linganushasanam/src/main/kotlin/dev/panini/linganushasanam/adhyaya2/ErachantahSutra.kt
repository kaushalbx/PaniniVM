package dev.panini.linganushasanam.adhyaya2

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra

/**
 * Liṅgānuśāsanam 2.4: इर्-अच्-अन्ताः.
 * Words formed with Kṛt affixes Er, Ac, Ap (-अच्, -अप्) are masculine.
 */
object ErachantahSutra : LinganushasanaSutra(
    number = "2.4",
    text = "इर्-अच्-अन्ताः",
    hindiExplanation = "अच्, अप् आदि प्रत्ययान्त शब्द पुंलिङ्ग होते हैं (उदा. जयः, शयः, करः, ग्रहः, भरः)।",
    section = LinganushasanaSection.PUMLINGA,
    targetLinga = Linga.PUMS,
    priority = 20,
) {
    private val AC_STEMS = setOf("जय", "शय", "कर", "ग्रह", "भर", "चय", "मय", "नय")

    override fun matches(context: LingaRuleContext): Boolean {
        val stem = context.pratipadika
        if (context.pratyaya in setOf("अच्", "अप्", "इर्")) return true
        return stem in AC_STEMS
    }
}
