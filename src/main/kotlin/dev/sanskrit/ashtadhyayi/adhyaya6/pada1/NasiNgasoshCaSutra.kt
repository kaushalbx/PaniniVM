package dev.sanskrit.ashtadhyayi.adhyaya6.pada1

import dev.sanskrit.ashtadhyayi.Ashtadhyayi
import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationStage
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.shiksha.Samjna
import dev.sanskrit.derivation.VarnaSubstitution
import dev.sanskrit.pratyahara.Pratyahara
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

import dev.sanskrit.sutra.SutraPriority

/**
 * 6.1.100: ṅasi-ṅasoś ca.
 * When 'e' or 'o' (Eṅ) is followed by the short 'a' of the affixes ṅasi or ṅas, 
 * a single substitute of the former (pūrvarūpa) replaces both.
 * This is crucial for i/u stems (e.g., Muneḥ).
 */
object NasiNgasoshCaSutra : Sutra<DerivationState, DerivationChange>(
    number = "6.1.110",
    text = "ङसिङसोश्च",
    hindiExplanation = "एङ् (ए, ओ) के बाद ङसि या ङस् का अकार आने पर पूर्वरूप एकादेश होता है।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 1,
    optional = false,
    kramaValue = 610110,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
    priority = SutraPriority.APAVADA,
    blocks = setOf("6.1.78")
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.terms.size < 2) return false
        val left = context.terms[context.terms.size - 2]
        val right = context.terms.last()

        // 1. Left term must end in 'e' or 'o' (usually from Ghi guna)
        val lastChar = left.surface.lastOrNull() ?: return false
        val engine = Ashtadhyayi.pratyaharaEngine
        val isEng = engine.contains(Pratyahara.EC, lastChar) && (lastChar in setOf('ए', 'ओ', 'े', 'ो'))
        
        if (!isEng) return false

        // 2. Right term must be the 'a' of ṅasi or ṅas
        // In our engine, suffixes are already it-processed, so 'ṅasi' is 'as' or 'i' 
        // depending on previous rules. Specifically, ṅasi/ṅas starts with 'a'.
        return right.upadesha in setOf("ङसि", "ङस्") && right.surface.startsWith('अ')
    }

    override fun apply(context: DerivationState): DerivationChange {
        val terms = context.terms
        val right = terms.last()
        
        // Pūrvarūpa: replace the 'a' with avagraha or delete it.
        val newRightSurface = "ऽ" + right.surface.drop(1)
        
        return DerivationChange(
            state = context.replaceTerm(right.id, right.copy(surface = newRightSurface))
                .copy(stage = DerivationStage.PADA_FORMED),
            explanation = "6.1.110: Pūrvarūpa substitution for final vowel + ङसि/ङस्."
        )
    }
}
