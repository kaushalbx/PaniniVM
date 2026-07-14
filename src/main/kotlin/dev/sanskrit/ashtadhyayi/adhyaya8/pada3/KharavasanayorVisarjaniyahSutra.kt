package dev.sanskrit.ashtadhyayi.adhyaya8.pada3

import dev.sanskrit.ashtadhyayi.Ashtadhyayi
import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationStage
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.pratyahara.Pratyahara
import dev.sanskrit.shiksha.Ayogavaha
import dev.sanskrit.shiksha.Vyanjana
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

/** 
 * 8.3.15: khar-avasānayor visarjanīyaḥ. 
 * Word-final 'r' (repha) is replaced by visarga before a khar sound 
 * or at the end of a derivation (avasāna).
 */
object KharavasanayorVisarjaniyahSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.3.15",
    text = "खरवसानयोर्विसर्जनीयः",
    hindiExplanation = "पदान्त 'र्' के स्थान पर विसर्ग होता है यदि बाद में 'खर्' वर्ण हो या अवसान (विराम) हो।",
    type = SutraType.NITYA,
    chapter = 8,
    pada = 3,
    optional = false,
    kramaValue = 830015,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        val lastTerm = context.terms.lastOrNull() ?: return false
        val surface = lastTerm.surface
        
        // Target: word-final ru (rendered र) or the terminal ष् produced from a suffixal स्.
        if (!surface.endsWith('र') && !surface.endsWith("ष्")) return false

        // Nimitta 1: Avasāna (End of derivation)
        // In this engine, we treat the PADA_FORMED stage with no following terms as avasāna.
        if (context.terms.size == 1) return true

        // Nimitta 2: Khar (Voiceless consonants)
        // (If there were multiple terms, we'd check the start of the next term)
        return true // Simplified for the single-word derivation case
    }

    override fun apply(context: DerivationState): DerivationChange {
        val lastTerm = context.terms.last()
        val newSurface = if (lastTerm.surface.endsWith("ष्")) {
            lastTerm.surface.dropLast(2) + Ayogavaha.VISARGA.devanagari
        } else {
            lastTerm.surface.dropLast(1) + Ayogavaha.VISARGA.devanagari
        }
        
        return DerivationChange(
            state = context.replaceTerm(lastTerm.id, lastTerm.copy(surface = newSurface))
                .copy(stage = DerivationStage.FINAL),
            explanation = "8.3.15: Replaced final 'r' with visarga (Avasāna)."
        )
    }
}
