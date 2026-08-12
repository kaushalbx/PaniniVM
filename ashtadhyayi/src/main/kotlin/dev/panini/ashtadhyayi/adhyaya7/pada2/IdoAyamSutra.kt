package dev.panini.ashtadhyayi.adhyaya7.pada2

import dev.panini.core.Linga
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.sutra.NimittaScope
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 7.2.111: ido 'yam.
 * Substitutes 'ayam' for the stem 'idam' before nominative singular 'su' in masculine gender.
 */
object IdoAyamSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.2.111",
    text = "इदोऽयम्",
    hindiExplanation = "इदम् शब्द के स्थान पर अयम् आदेश होता है पुँल्लिङ्ग में सुँ-प्रत्यय परे होने पर।",
    type = SutraType.NITYA,
    chapter = 7,
    pada = 2,
    optional = false,
    kramaValue = 720111,
    role = SutraRole.Apavada,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
    nimittaScope = NimittaScope.BOTH,
    dependencies = setOf("6.4.1"),
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.terms.size < 2) return false
        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()
        if (stem.surface == "अयम्") return false
        val isIdam = stem.upadesha == "इदम्" || stem.surface == "इदम्" || stem.surface == "इम"
        val isMasculine = context.effectiveContext.rupa.linga == Linga.PUMS
        return isIdam && isMasculine && (affix.id == "sup-su" || affix.upadesha == "सुँ")
    }

    override fun apply(context: DerivationState): DerivationChange {
        val stem = context.terms[context.terms.size - 2]

        return DerivationChange(
            state = context.replaceTerm(stem.id, stem.copy(surface = "अयम्"))
                .copy(stage = DerivationStage.ANGAKARYA),
            explanation = "7.2.111: Replaced 'idam' with 'ayam' before nominative singular 'su'."
        )
    }
}
