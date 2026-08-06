package dev.panini.linganushasanam.rules

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LingaRuleResult
import dev.panini.linganushasanam.LinganushasanamRule

/**
 * Pāṇinian Liṅgānuśāsanam rules for feminine gender assignment (स्त्रीलिङ्ग-नियम).
 */
object StrilingaAffixRule : LinganushasanamRule {
    override val ruleId: String = "LINGA_STRI_AFFIX"
    override val description: String = "Stems ending in feminine affixes (-क्तिन्, -ता, -तल्, -ङीप्, -ङीष्, -आप्) are feminine."
    override val priority: Int = 20

    private val STRI_SUFFIXES = setOf("क्तिन्", "ता", "तल्", "ङीप्", "ङीष्", "ङीन्", "आप्", "टाप्", "डाप्", "चाप्")
    private val STRI_ENDINGS = setOf("ता", "ति", "ई", "आ", "ा", "ी")
    private val STRI_STEMS = setOf("नवमी", "भक्ति", "सभा", "शाला", "सेना", "शक्ति", "रात्रि", "गङ्गा", "पूर्णिमा", "प्रज्ञा")

    override fun matches(context: LingaRuleContext): Boolean {
        val stem = context.pratipadika
        val pratyaya = context.pratyaya
        if (pratyaya != null && pratyaya in STRI_SUFFIXES) return true
        if (stem in STRI_STEMS) return true
        return STRI_ENDINGS.any { stem.endsWith(it) }
    }

    override fun apply(context: LingaRuleContext): LingaRuleResult {
        return LingaRuleResult.Matched(
            linga = Linga.STRI,
            ruleId = ruleId,
            explanation = "Feminine affix/stem rule matched for '${context.pratipadika}'.",
        )
    }
}
