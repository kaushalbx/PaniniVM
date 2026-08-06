package dev.panini.linganushasanam.adhyaya3

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra

/**
 * Liṅgānuśāsanam 3.2: ल्युडाद्यन्तः.
 * Stems formed with Lyuṭ (-ल्युट्) or nominalizing suffixes (-अम्, -नम्) are Neuter.
 */
object LyudadyantahSutra : LinganushasanaSutra(
    number = "3.2",
    text = "ल्युडाद्यन्तः",
    hindiExplanation = "ल्युट् तथा क्त आदि प्रत्ययान्त शब्द नपुंसकलिङ्ग होते हैं (उदा. गमनम्, पठनम्, ज्ञानम्, कृताकृतम्)।",
    section = LinganushasanaSection.NAPUMSAKALINGA,
    targetLinga = Linga.NAPUMSAKA,
    priority = 20,
) {
    private val LYUT_STEMS = setOf("गमन", "पठन", "ज्ञान", "पुष्प", "फल", "जल", "गृह", "हृदय", "अवच", "कर्मन्", "पद", "अन्न", "कृत")

    override fun matches(context: LingaRuleContext): Boolean {
        val stem = context.pratipadika
        if (context.pratyaya in setOf("ल्युट्", "क्त")) return true
        if (stem in LYUT_STEMS || stem.endsWith("कृत") || context.padas.firstOrNull() == "कृत") return true
        return stem.endsWith("नम्")
    }
}
