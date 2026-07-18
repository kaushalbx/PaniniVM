package dev.panini.ashtadhyayi.adhyaya7.pada3

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.Lakara
import dev.panini.derivation.VarnaSubstitution
import dev.panini.sutra.NimittaScope
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 7.3.101: ato dīrgho yañi.
 * The final 'a' of an aṅga is lengthened when followed by a 'yañ'-initial suffix.
 * Primarily used in verbal derivation (e.g., bhavāmi).
 */
object AtoDirghoYaniSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.3.101",
    text = "अतो दीर्घो यञि",
    hindiExplanation = "यञादि प्रत्यय परे होने पर अदन्त अङ्ग को दीर्घ होता है।",
    type = SutraType.NITYA,
    chapter = 7,
    pada = 3,
    optional = false,
    kramaValue = 730101,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DHATU,
    nimittaScope = NimittaScope.EXTERNAL,
    dependencies = setOf("6.4.1")
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.stage == DerivationStage.INITIAL || context.stage == DerivationStage.PRATYAYA_SELECTED) return false
        if ("6.4.1" !in context.activeAdhikaras) return false
        if (context.terms.size < 2) return false

        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()
        if (context.effectiveContext.rupa.lakara == Lakara.LOT && affix.upadesha == "झि") return false
        if (context.effectiveContext.rupa.lakara in setOf(Lakara.LANG, Lakara.LRNG, Lakara.LUNG, Lakara.LING) &&
            affix.upadesha == "मिप्" && context.substitutions.none { it.sutra == "3.4.101" }
        ) return false
        if (!affix.id.startsWith("ting-")) return false
        if (affix.upadesha == "ङि") return false

        val isAEnding = dev.panini.shiksha.Varnamala.endsWithA(stem.surface)
        val startsWithYan = affix.surface.firstOrNull() in setOf('य', 'व', 'र', 'ल', 'ञ', 'म', 'ङ', 'ण', 'न')

        return isAEnding && startsWithYan
    }

    override fun apply(context: DerivationState): DerivationChange {
        val stem = context.terms[context.terms.size - 2]
        val lastChar = stem.surface.last()
        val newSurface = if (lastChar !in dev.panini.shiksha.Varnamala.independentVowelsOrMarks) {
            stem.surface + "ा"
        } else {
            stem.surface.dropLast(1) + "ा"
        }

        return DerivationChange(
            state = context.replaceTerm(stem.id, stem.copy(surface = newSurface))
                .copy(stage = DerivationStage.ANGAKARYA),
            explanation = "7.3.101: Lengthened final 'a' before yañ-initial suffix."
        ).let { it.copy(state = it.state.addSubstitution(VarnaSubstitution(stem.id, 'अ', "ा", sutra))) }
    }
}
