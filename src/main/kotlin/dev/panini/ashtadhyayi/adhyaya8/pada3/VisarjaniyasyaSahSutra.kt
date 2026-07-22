package dev.panini.ashtadhyayi.adhyaya8.pada3

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.VarnaSubstitution
import dev.panini.pratyahara.Pratyahara
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
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.terms.size < 2) return false
        val left = context.terms[context.terms.size - 2].surface
        val right = context.terms.last().surface

        if (!left.endsWith('ः')) return false

        val nextChar = right.firstOrNull() ?: return false
        return Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.KHAR, nextChar)
    }

    override fun apply(context: DerivationState): DerivationChange {
        val leftTerm = context.terms[context.terms.size - 2]
        val rightTerm = context.terms.last()
        val nextChar = rightTerm.surface.first()

        // Basic replacement is 's'. Subsequent rules (8.4.40, 8.4.41) will
        // handle conversion to 'ś' or 'ṣ' if needed.
        val replacement = "स्"
        val newSurface = leftTerm.surface.dropLast(1) + replacement

        return DerivationChange(
            state = context.replaceTerm(leftTerm.id, leftTerm.copy(surface = newSurface)),
            explanation = "8.3.34: Replaced visarga with 's' before khar sound '$nextChar'."
        ).let { it.copy(state = it.state.addSubstitution(VarnaSubstitution(leftTerm.id, 'ः', replacement, sutra))) }
    }
}
