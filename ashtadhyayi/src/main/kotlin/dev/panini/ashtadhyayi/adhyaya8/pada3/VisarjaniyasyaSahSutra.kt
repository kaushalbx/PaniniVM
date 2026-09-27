package dev.panini.ashtadhyayi.adhyaya8.pada3

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.pratyahara.Pratyahara
import dev.panini.shiksha.Ayogavaha
import dev.panini.shiksha.Vyanjana
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 8.3.34: visarjanīyasya saḥ.
 * A visarga is replaced by 's' when followed by a khar sound (voiceless consonant).
 */
object VisarjaniyasyaSahSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.3.34",
    text = "विसर्जनीयस्य सः",
    hindiExplanation = "खर् वर्ण परे होने पर विसर्ग के स्थान पर सकार होता है।",
    type = SutraType.NITYA,
    chapter = 8,
    pada = 3,
    optional = false,
    kramaValue = 830034,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.VARNA,
    stage = dev.panini.sutra.SutraStage.SIBILANT_SANDHI,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.terms.size < 2) return false
        val left = context.terms[context.terms.size - 2]
        val next = context.terms.last().varnas.firstOrNull() as? Vyanjana ?: return false
        return left.varnas.lastOrNull() == Ayogavaha.VISARGA &&
            Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.KHAR, next.devanagari.single())
    }

    override fun apply(context: DerivationState): DerivationChange {
        val leftTerm = context.terms[context.terms.size - 2]
        val next = context.terms.last().varnas.first()
        val result = leftTerm.varnas.dropLast(1) + Vyanjana.SA

        return DerivationChange(
            state = context.substituteTermVarnas(leftTerm.id, result, Ayogavaha.VISARGA, listOf(Vyanjana.SA), sutra),
            explanation = "8.3.34 replaces visarga with s before khar sound $next."
        )
    }
}
