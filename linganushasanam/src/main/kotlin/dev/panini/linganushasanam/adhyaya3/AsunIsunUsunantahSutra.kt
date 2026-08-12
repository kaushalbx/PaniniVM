package dev.panini.linganushasanam.adhyaya3

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra

/**
 * Liṅgānuśāsanam 3.3: असुन्-इसुन्-उसुन्-अन्तः.
 * Stems ending in -असुन्, -इसुन्, -उसुन् (-स्) are Neuter.
 */
object AsunIsunUsunantahSutra : LinganushasanaSutra(
    number = "3.3",
    text = "असुन्-इसुन्-उसुन्-अन्तः",
    hindiExplanation = "असुन्, इसुन्, उसुन् प्रत्ययान्त शब्द नपुंसकलिङ्ग होते हैं (उदा. हविस्, मनस्, पयस्, चक्षुस्)।",
    section = LinganushasanaSection.NAPUMSAKALINGA,
    targetLinga = Linga.NAPUMSAKA,
    priority = 20,
) {
    private val ASUN_STEMS = setOf("हविस्", "मनस्", "पयस्", "उरस्", "चक्षुस्", "सरस्", "यशस्", "तेजस्")

    override fun matches(context: LingaRuleContext): Boolean {
        val stem = context.pratipadika
        if (context.pratyaya in setOf("असुन्", "इसुन्", "उसुन्")) return true
        if (stem in ASUN_STEMS) return true
        return stem.endsWith("स्") && (stem.endsWith("अस्") || stem.endsWith("इस्") || stem.endsWith("उस्"))
    }
}
