package dev.panini.ashtadhyayi.adhyaya6.pada3

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.shiksha.Samjna
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** 6.3.47: द्व्यष्टनः संख्यायामबहुव्रीह्यशीत्योः */
object DvyashtanahSankhyayamSutra : Sutra<DerivationState, DerivationChange>(
    number = "6.3.47",
    text = "द्व्यष्टनः संख्यायामबहुव्रीह्यशीत्योः",
    hindiExplanation = "संख्या उत्तरपद होने पर द्वि और अष्टन् के स्थान पर आ होता है।",
    type = SutraType.UTSARGA,
    chapter = 6,
    pada = 3,
    optional = false,
    kramaValue = 630047,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION
), DerivationSutra {

    override fun matches(context: DerivationState): Boolean {
        if (context.terms.size < 2) return false
        val purvapada = context.terms.firstOrNull() ?: return false
        val uttarapada = context.terms.getOrNull(1) ?: return false

        val purvaMatch = purvapada.surface in setOf("द्वि", "अष्टन्")
        val isUttaraSankhya = context.samjnas.any { it.targetId == uttarapada.id && it.samjna == Samjna.SANKHYA } &&
            uttarapada.surface in setOf("दश", "विंशति", "त्रिंशत्")
        val notAshiti = uttarapada.surface != "अशीति"

        return purvaMatch && isUttaraSankhya && notAshiti
    }

    override fun apply(context: DerivationState): DerivationChange {
        val purvapada = context.terms.first()
        val replacement = when (purvapada.surface) {
            "द्वि" -> "द्वा"
            "अष्टन्" -> "अष्टा"
            else -> when (purvapada.upadesha) {
                "द्वि" -> "द्वा"
                "अष्टन्" -> "अष्टा"
                else -> purvapada.surface
            }
        }
        val updatedTerm = purvapada.copy(surface = replacement)
        val newTerms = listOf(updatedTerm) + context.terms.drop(1)
        return DerivationChange(
            state = context.copy(terms = newTerms),
            explanation = "$text: ${purvapada.surface} -> $replacement"
        )
    }
}
