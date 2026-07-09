package dev.sanskrit.patha.adhyaya6.pada1

import dev.sanskrit.sandhi.BaseSandhiSutra
import dev.sanskrit.sandhi.BoundaryChange
import dev.sanskrit.sandhi.SandhiContext
import dev.sanskrit.sandhi.Shiksha
import dev.sanskrit.shiksha.Svara
import dev.sanskrit.shiksha.Vyanjana
import dev.sanskrit.sutra.SutraMetadata
import dev.sanskrit.sutra.SutraType

/**
 * English: Before an अच् vowel, एच् vowels take the य्/व् based अय्, अव्, आय्, or आव् substitutes.
 * हिन्दी: अच् के आने पर एच् वर्णों के स्थान पर क्रम से अय्, अव्, आय् या आव् आदेश होता है।
 * Code reference: dev.sanskrit.pratyahara.Pratyahara.AC and EC.
 */
object EcoYavayavahSutra : BaseSandhiSutra(
    SutraMetadata(
        sutraNumber = "6.1.78",
        sutraText = "एचोऽयवायावः",
        hindiVyakhya = "एच् वर्णों के बाद अच् आए तो क्रम से अय्, अव्, आय्, आव् आदेश होते हैं।",
        type = SutraType.NITYA,
        adhyaya = 6,
        pada = 1,
        vaikalpika = false,
        krama = 610078,
    ),
) {
    override fun matches(context: SandhiContext): Boolean {
        val left = Shiksha.endingSvara(context.left) ?: return false
        Shiksha.startingSvara(context.right) ?: return false
        return left.svara in setOf(Svara.E, Svara.O, Svara.AI, Svara.AU)
    }

    override fun apply(context: SandhiContext): BoundaryChange {
        val left = Shiksha.endingSvara(context.left)
            ?: error("Sutra applied without final svara: $context")
        val right = Shiksha.startingSvara(context.right)
            ?: error("Sutra applied without initial svara: $context")
        val replacement = when (left.svara) {
            Svara.E -> Svara.A to Vyanjana.YA
            Svara.O -> Svara.A to Vyanjana.VA
            Svara.AI -> Svara.AA to Vyanjana.YA
            Svara.AU -> Svara.AA to Vyanjana.VA
            else -> error("Sutra applied to non-ec svara: $context")
        }
        val svaraPart = Shiksha.replaceFinalSvara(left, replacement.first)
        return BoundaryChange(svaraPart + replacement.second.devanagari + right.rest)
    }
}
