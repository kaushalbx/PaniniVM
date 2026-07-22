package dev.panini.ashtadhyayi.adhyaya6.pada3

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.shiksha.Samjna
import dev.panini.sutra.*

/** 6.3.49: विभाषा चत्वारिंशत्प्रभृतौ सर्वेषाम्। */
object VibhashaChatvarimshatPrabhritauSarveshamSutra : Sutra<DerivationState, DerivationChange>(
    number = "6.3.49", text = "विभाषा चत्वारिंशत्प्रभृतौ सर्वेषाम्",
    hindiExplanation = "चत्वारिंशत् से नवति तक द्वि, अष्टन् और त्रि के पूर्वोक्त आदेश विकल्प से होते हैं।",
    type = SutraType.VIBHASHA, chapter = 6, pada = 3, optional = true, kramaValue = 630049,
    role = SutraRole.Vidhi, action = SutraAction.ADESHA, scope = SutraScope.DERIVATION,
), DerivationSutra {
    private val optionalUttarapadas = setOf("चत्वारिंशत्", "पञ्चाशत्", "षष्टि", "सप्तति", "नवति")

    override fun matches(context: DerivationState): Boolean {
        if (context.terms.size < 2) return false
        val purvapada = context.terms[0]
        val uttarapada = context.terms[1]
        return purvapada.surface in setOf("द्वि", "अष्टन्", "त्रि") &&
            uttarapada.surface in optionalUttarapadas &&
            context.samjnas.any { it.targetId == uttarapada.id && it.samjna == Samjna.SANKHYA }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val purvapada = context.terms[0]
        val replacement = when (purvapada.surface) {
            "द्वि" -> "द्वा"
            "अष्टन्" -> "अष्टा"
            "त्रि" -> "त्रयस्"
            else -> error("Unexpected pūrvapada ${purvapada.surface}")
        }
        return DerivationChange(
            context.replaceTerm(purvapada.id, purvapada.copy(surface = replacement)),
            "6.3.49 optionally applies the substitutions of 6.3.47–48: ${purvapada.surface} → $replacement."
        )
    }
}
