package dev.sanskrit.ashtadhyayi.adhyaya3.pada4

import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.derivation.Lakara
import dev.sanskrit.derivation.TermKind
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

/** 3.4.79: टित आत्मनेपदानां टेरे. */
object TitaAtmanepadanamTereSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.4.79",
    text = "टित आत्मनेपदानां टेरे",
    hindiExplanation = "टित् लकार के आत्मनेपद तिङ्-प्रत्ययों के टि-भाग के स्थान पर ए होता है।",
    type = SutraType.NITYA,
    chapter = 3,
    pada = 4,
    optional = false,
    kramaValue = 340079,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.PRATYAYA,
    blocks = setOf("6.1.78"),
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.effectiveContext.rupa.lakara != Lakara.LAT) return false
        val ending = context.terms.last()
        val atoNgitahCompleted = context.droppedTerms.any { it.id == "ato-ngit-it" }
        val replacement = when (ending.upadesha) {
            "त" -> "ते"
            "आताम्" -> if (atoNgitahCompleted) "ते" else "आते"
            "आथाम्" -> if (atoNgitahCompleted) "थे" else "आथे"
            "ध्वम्" -> "ध्वे"
            "वहि" -> "वहे"
            "महिङ्" -> "महे"
            else -> null
        }
        if (replacement != null) {
            val requiresAtoNgitah = ending.upadesha in setOf("आताम्", "आथाम्")
            return ending.surface != replacement && (!requiresAtoNgitah || atoNgitahCompleted)
        }
        return ending.surface.endsWith("न्त्") &&
            context.substitutions.any { it.sutra == "7.1.3" } &&
            ending.upadesha == "शप्"
    }

    override fun apply(context: DerivationState): DerivationChange {
        val ending = context.terms.last()
        if (ending.surface.endsWith("न्त्") && context.substitutions.any { it.sutra == "7.1.3" }) {
            return DerivationChange(
                context.replaceTerm(ending.id, ending.copy(surface = ending.surface.dropLast(2) + "ते")),
                "3.4.79 replaces the final टि of the झ्-अन्ति outcome with ए.",
            )
        }

        val atoNgitahCompleted = context.droppedTerms.any { it.id == "ato-ngit-it" }
        val replacement = when (ending.upadesha) {
            "त" -> "ते"
            "आताम्" -> if (atoNgitahCompleted) "ते" else "आते"
            "आथाम्" -> if (atoNgitahCompleted) "थे" else "आथे"
            "ध्वम्" -> "ध्वे"
            "वहि" -> "वहे"
            "महिङ्" -> "महे"
            else -> error("3.4.79 received a non-Ātmanepada ending: ${ending.upadesha}")
        }
        return DerivationChange(
            context.replaceTerm(ending.id, ending.copy(surface = replacement)),
            "3.4.79 replaces the टि portion of ${ending.upadesha} with ए.",
        )
    }
}
