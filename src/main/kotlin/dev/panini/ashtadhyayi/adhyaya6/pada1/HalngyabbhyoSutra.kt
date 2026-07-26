package dev.panini.ashtadhyayi.adhyaya6.pada1

import dev.panini.core.Lakara
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.shiksha.Varnamala
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 6.1.68: hal-ṅy-ābbhyo dīrghāt sutisy-apṛktaṃ hal.
 */
object HalngyabbhyoSutra : Sutra<DerivationState, DerivationChange>(
    number = "6.1.68",
    text = "हल्ङ्याब्भ्यो दीर्घात्सुतिस्यपृक्तं हल्",
    hindiExplanation = "हल्, ङी (ई) और आप् (आ) के बाद सु, ति और सि के अपृक्त हल् (एकल व्यञ्जन) का लोप होता है।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 1,
    optional = false,
    kramaValue = 610068,
    role = SutraRole.Vidhi,
    action = SutraAction.LOPA,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.effectiveContext.rupa.lakara == Lakara.LET &&
            context.allEffectiveTerms.any { it.id == "sip-aorist" } &&
            context.substitutions.none { it.sutra == "3.4.94" }) return false
        if (context.terms.size < 2) return false
        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()
        if (stem.id in setOf("yasut", "shna")) return false

        val surface = affix.surface
        val isApṛktaHal =
            (surface.length == 1 && Varnamala.isConsonant(surface[0])) ||
                (surface.length == 2 && surface.last() == '्' && Varnamala.isConsonant(surface.first()))
        if (!isApṛktaHal) return false

        val isEligibleAffix = affix.upadesha in setOf("सुँ", "तिप्", "सिप्")
        if (!isEligibleAffix) return false

        val stemSurface = stem.surface
        if (stemSurface.isEmpty()) return false
        val lastChar = stemSurface.last()

        val endsInHal = stemSurface.endsWith('्')
        val endsInDirghaFeminine = lastChar == 'ी' || lastChar == 'ा'

        return endsInHal || endsInDirghaFeminine
    }

    override fun apply(context: DerivationState): DerivationChange {
        val affix = context.terms.last()
        return DerivationChange(
            state = context.removeTerm(affix.id).copy(stage = DerivationStage.FINAL),
            explanation = "6.1.68: Deleted the single-consonant affix after hal/ī/ā."
        )
    }
}
