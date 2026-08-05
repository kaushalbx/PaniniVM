package dev.panini.ashtadhyayi.adhyaya2.pada1

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.SamjnaAssignment
import dev.panini.derivation.TermKind
import dev.panini.shiksha.Samjna
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 2.1.30: तृतीया तत्कृतार्थेन गुणवचनेन.
 * Prescribes Tṛtīyā Tatpuruṣa compound between a 3rd-case subanta and a qualifying word denoting property/result.
 */
object TrtiyaTatkrtharthenaSutra : Sutra<DerivationState, DerivationChange>(
    number = "2.1.30",
    text = "तृतीया तत्कृतार्थेन गुणवचनेन",
    hindiExplanation = "तृतीयान्त समर्थ सुबन्त का तत्कृत अर्थ वाले गुणवाचक शब्द के साथ तत्पुरुष समास होता है (उदा. शङ्कुलाखण्डः)।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 1,
    optional = false,
    kramaValue = 210030,
    role = SutraRole.Vidhi,
    action = SutraAction.VIDHI,
    scope = SutraScope.DERIVATION,
), DerivationSutra {

    override fun matches(context: DerivationState): Boolean {
        if (context.terms.size < 2) return false
        val hasSamasaContext = context.samjnas.any { it.samjna == Samjna.SAMASA }
        val nominals = context.terms.filter { it.kind == TermKind.PRATIPADIKA }
        return hasSamasaContext && nominals.size >= 2 && context.allEffectiveTerms.none { it.id == "samasa_trtiya_tatpurusha" }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val nominals = context.terms.filter { it.kind == TermKind.PRATIPADIKA }
        val first = nominals[0]
        val second = nominals[1]
        val compoundSurface = first.surface + second.surface
        val compoundTerm = DerivationTerm("samasa_trtiya_tatpurusha", compoundSurface, TermKind.PRATIPADIKA, upadesha = compoundSurface)

        val newState = context.replaceTerm(first.id, compoundTerm).removeTerm(second.id, sutra)
        return DerivationChange(
            state = newState.withSamjnas(setOf(SamjnaAssignment(compoundTerm.id, Samjna.PRATIPADIKA))),
            explanation = "2.1.30 forms Tṛtīyā Tatpuruṣa compound '${compoundSurface}'."
        )
    }
}
