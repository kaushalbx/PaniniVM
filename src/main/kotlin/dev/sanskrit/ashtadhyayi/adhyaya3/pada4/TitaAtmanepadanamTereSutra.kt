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
    blocks = setOf("3.4.94", "6.1.78", "8.2.23"),
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        val lakara = context.effectiveContext.rupa.lakara
        if (lakara !in setOf(Lakara.LAT, Lakara.LET, Lakara.LIT, Lakara.LOT, Lakara.LRT, Lakara.LUT)) return false
        val ending = context.terms.last()
        val atoNgitahCompleted = context.droppedTerms.any { it.id == "ato-ngit-it" }
        val isNonAStem = context.terms.any { it.id in setOf("shnu", "tanadi-u") }
        if (lakara == Lakara.LOT && context.substitutions.any { it.sutra in setOf("3.4.90", "3.4.91", "3.4.93") }) return false
        if (lakara == Lakara.LIT && ending.upadesha in setOf("त", "झ")) return false
        if (lakara == Lakara.LUT && ending.upadesha in setOf("त", "आताम्", "झ")) return false
        if (lakara == Lakara.LET && context.substitutions.any { it.sutra in setOf("3.4.94", "3.4.96") }) return false
        if (lakara == Lakara.LUT && context.substitutions.any { it.sutra == "7.4.52" }) return false
        if (lakara == Lakara.LET && ending.upadesha in setOf("आताम्", "आथाम्") &&
            context.substitutions.any { it.sutra == "3.4.95" }) return false
        val replacement = when (ending.upadesha) {
            "त" -> "ते"
            "आताम्" -> if (lakara in setOf(Lakara.LOT, Lakara.LRT)) "एते" else if (lakara == Lakara.LAT && atoNgitahCompleted) "ते" else "आते"
            "आथाम्" -> if (lakara in setOf(Lakara.LOT, Lakara.LRT)) "एथे" else if (lakara == Lakara.LAT && atoNgitahCompleted) "थे" else "आथे"
            "ध्वम्" -> "ध्वे"
            "वहि" -> "वहे"
            "महिङ्" -> "महे"
            "इट्" -> "ए"
            else -> null
        }
        if (replacement != null) {
            val requiresAtoNgitah = ending.upadesha in setOf("आताम्", "आथाम्")
            return ending.surface != replacement && (lakara in setOf(Lakara.LET, Lakara.LIT, Lakara.LOT, Lakara.LRT, Lakara.LUT) || !requiresAtoNgitah || atoNgitahCompleted || isNonAStem)
        }
        return ending.surface.endsWith("न्त्") &&
            context.substitutions.any { it.sutra == "7.1.3" } &&
            ending.upadesha in setOf("झ", "शप्") ||
            (ending.surface.endsWith("अत") && context.substitutions.any { it.sutra == "7.1.5" })
    }

    override fun apply(context: DerivationState): DerivationChange {
        val ending = context.terms.last()
        if (ending.surface.endsWith("न्त्") && context.substitutions.any { it.sutra == "7.1.3" }) {
            return DerivationChange(
                context.replaceTerm(ending.id, ending.copy(surface = ending.surface.dropLast(2) + "ते")),
                "3.4.79 replaces the final टि of the झ्-अन्ति outcome with ए.",
            )
        }
        if (ending.surface.endsWith("अत") && context.substitutions.any { it.sutra == "7.1.5" }) {
            return DerivationChange(
                context.replaceTerm(ending.id, ending.copy(surface = ending.surface + "े")),
                "3.4.79 replaces the टि portion of the 7.1.5 अत् outcome with ए.",
            )
        }

        val atoNgitahCompleted = context.droppedTerms.any { it.id == "ato-ngit-it" }
        val lakara = context.effectiveContext.rupa.lakara
        val replacement = when (ending.upadesha) {
            "त" -> "ते"
            "आताम्" -> if (lakara in setOf(Lakara.LOT, Lakara.LRT)) "एते" else if (lakara == Lakara.LAT && atoNgitahCompleted) "ते" else "आते"
            "आथाम्" -> if (lakara in setOf(Lakara.LOT, Lakara.LRT)) "एथे" else if (lakara == Lakara.LAT && atoNgitahCompleted) "थे" else "आथे"
            "ध्वम्" -> "ध्वे"
            "वहि" -> "वहे"
            "महिङ्" -> "महे"
            "इट्" -> "ए"
            else -> error("3.4.79 received a non-Ātmanepada ending: ${ending.upadesha}")
        }
        return DerivationChange(
            context.replaceTerm(ending.id, ending.copy(surface = replacement)),
            "3.4.79 replaces the टि portion of ${ending.upadesha} with ए.",
        )
    }
}
