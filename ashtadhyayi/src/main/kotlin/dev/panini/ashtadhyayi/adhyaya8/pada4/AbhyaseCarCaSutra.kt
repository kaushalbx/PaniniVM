package dev.panini.ashtadhyayi.adhyaya8.pada4

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.VarnaSubstitution
import dev.panini.shiksha.Samjna
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 8.4.54: abhyāse car ca.
 * A jhal consonant in an abhyāsa receives its nearest car or jaś substitute.
 */
object AbhyaseCarCaSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.4.54",
    text = "अभ्यासे चर्च",
    hindiExplanation = "अभ्यास में झल् वर्णों के स्थान पर चर् और जश् आदेश होते हैं।",
    type = SutraType.NITYA,
    chapter = 8,
    pada = 4,
    optional = false,
    kramaValue = 840054,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.VARNA,
), DerivationSutra {
    private val carOrJash = mapOf(
        'क' to 'क', 'ख' to 'क', 'ग' to 'ग', 'घ' to 'ग',
        'च' to 'च', 'छ' to 'च', 'ज' to 'ज', 'झ' to 'ज',
        'ट' to 'ट', 'ठ' to 'ट', 'ड' to 'ड', 'ढ' to 'ड',
        'त' to 'त', 'थ' to 'त', 'द' to 'द', 'ध' to 'द',
        'प' to 'प', 'फ' to 'प', 'ब' to 'ब', 'भ' to 'ब',
    )

    override fun matches(context: DerivationState): Boolean {
        val abhyasa = context.terms.firstOrNull { it.id == "abhyasa" } ?: return false
        return context.samjnas.any { it.targetId == abhyasa.id && it.samjna == Samjna.ABHYASA } &&
            abhyasa.surface.firstOrNull() in carOrJash &&
            carOrJash.getValue(abhyasa.surface.first()) != abhyasa.surface.first()
    }

    override fun apply(context: DerivationState): DerivationChange {
        val abhyasa = context.terms.first { it.id == "abhyasa" }
        val source = abhyasa.surface.first()
        val substitute = carOrJash.getValue(source)
        val newSurface = substitute + abhyasa.surface.drop(1)
        return DerivationChange(
            state = context.replaceTerm(abhyasa.id, abhyasa.copy(surface = newSurface)),
            explanation = "8.4.54 changes $source to its nearest $substitute substitute in the abhyāsa.",
        ).let { it.copy(state = it.state.addSubstitution(VarnaSubstitution(abhyasa.id, source, substitute.toString(), sutra))) }
    }
}
