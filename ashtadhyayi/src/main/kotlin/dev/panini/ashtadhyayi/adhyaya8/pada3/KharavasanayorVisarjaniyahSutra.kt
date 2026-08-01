package dev.panini.ashtadhyayi.adhyaya8.pada3

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.pratyahara.Pratyahara
import dev.panini.shiksha.Ayogavaha
import dev.panini.shiksha.Samjna
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

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
    stage = dev.panini.sutra.SutraStage.VISARJANIYA,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        val internal = internalSankhyaTerm(context)
        val lastTerm = internal ?: context.terms.lastOrNull() ?: return false
        val surface = lastTerm.surface

        // Target: word-final ru (rendered र) or the terminal ष् produced from a suffixal स्.
        if (!surface.endsWith('र') && !surface.endsWith("ष्")) return false

        // Nimitta 1: Avasāna (End of derivation)
        // In this engine, we treat the PADA_FORMED stage with no following terms as avasāna.
        if (internal == null && context.terms.size == 1) return true

        // Nimitta 2: Khar (Voiceless consonants)
        // (If there were multiple terms, we'd check the start of the next term)
        if (internal != null) {
            val index = context.terms.indexOf(internal)
            val next = context.terms[index + 1].surface.firstOrNull() ?: return false
            return Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.KHAR, next)
        }
        return true // Simplified for the single-word derivation case
    }

    override fun apply(context: DerivationState): DerivationChange {
        val lastTerm = internalSankhyaTerm(context) ?: context.terms.last()
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

    private fun internalSankhyaTerm(context: DerivationState) = context.terms.firstOrNull { term ->
        val index = context.terms.indexOf(term)
        index < context.terms.lastIndex && term.surface.endsWith('र') &&
            context.samjnas.any { it.targetId == term.id && it.samjna == Samjna.SANKHYA } &&
            context.samjnas.any { it.targetId == context.terms[index + 1].id && it.samjna == Samjna.SANKHYA }
    }
}
