package dev.panini.ashtadhyayi.adhyaya6.pada1

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.core.SupAffix
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.matchesAnyAffix
import dev.panini.pratyahara.Pratyahara
import dev.panini.shiksha.Svara
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraPriority
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 6.1.110: ṅasi-ṅasoś ca.
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
        val isEng = left.varnas.lastOrNull() in setOf(Svara.E, Svara.O)

        if (!isEng) return false

        // 2. Right term must be the 'a' of ṅasi or ṅas
        // In our engine, suffixes are already it-processed, so 'ṅasi' is 'as' or 'i'
        // depending on previous rules. Specifically, ṅasi/ṅas starts with 'a'.
        return right.matchesAnyAffix(SupAffix.NGASI, SupAffix.NGAS) && right.varnas.firstOrNull() == Svara.A
    }

    override fun apply(context: DerivationState): DerivationChange {
        val terms = context.terms
        val right = terms.last()

        return DerivationChange(
            // The stem's e/o survives the ekadesha. An internal affix-vowel
            // deletion does not itself license an avagraha sign.
            state = context.deleteTermVarnas(right.id, 0, 1, sutra)
                .copy(stage = DerivationStage.PADA_FORMED),
            explanation = "6.1.110: Pūrvarūpa substitution for final vowel + ङसि/ङस्."
        )
    }
}
