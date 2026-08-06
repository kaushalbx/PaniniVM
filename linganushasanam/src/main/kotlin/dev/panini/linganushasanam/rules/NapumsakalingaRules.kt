package dev.panini.linganushasanam.rules

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LingaRuleResult
import dev.panini.linganushasanam.LinganushasanamRule

/**
 * Pāṇinian Liṅgānuśāsanam rules for neuter gender assignment (नपुंसकलिङ्ग-नियम).
 */
object NapumsakalingaAffixRule : LinganushasanamRule {
    override val ruleId: String = "LINGA_NAPUMSAKA_AFFIX"
    override val description: String = "Stems ending in neuter Uṇādi/Kṛt affixes (-ल्युट्, -असुन्, -इसुन्, -उसुन्) or specific neuter stems are neuter."
    override val priority: Int = 20

    private val NAPUMSAKA_SUFFIXES = setOf("ल्युट्", "असुन्", "इसुन्", "उसुन्", "क्त")
    private val NAPUMSAKA_STEMS = setOf(
        "हविस्", "मनस्", "पयस्", "उरस्", "चक्षुस्", "सरस्", "यशस्", "तेजस्",
        "पद", "ज", "कुल", "वन", "अक्ष", "जल", "फल", "गृह", "हृदय", "अवच", "ज्ञान", "पुष्प"
    )

    override fun matches(context: LingaRuleContext): Boolean {
        val stem = context.pratipadika
        val pratyaya = context.pratyaya
        if (pratyaya != null && pratyaya in NAPUMSAKA_SUFFIXES) return true
        if (stem in NAPUMSAKA_STEMS) return true
        if (stem.endsWith("स्") && (stem.endsWith("अस्") || stem.endsWith("इस्") || stem.endsWith("उस्"))) return true
        return false
    }

    override fun apply(context: LingaRuleContext): LingaRuleResult {
        return LingaRuleResult.Matched(
            linga = Linga.NAPUMSAKA,
            ruleId = ruleId,
            explanation = "Neuter affix/stem rule matched for '${context.pratipadika}'.",
        )
    }
}
