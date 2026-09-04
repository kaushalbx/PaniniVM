package dev.panini.ashtadhyayi.adhyaya7.pada2

import dev.panini.core.DhatuGana
import dev.panini.core.Lakara
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.ItProcessingPhase
import dev.panini.derivation.TermKind
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** 7.2.81: आतो ङितः. */
object AtoNgitahSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.2.81",
    text = "आतो ङितः",
    hindiExplanation = "अकार के बाद ङित् प्रत्यय के अवयव से पहले इट् आगम होता है।",
    type = SutraType.NITYA,
    chapter = 7,
    pada = 2,
    optional = false,
    kramaValue = 720081,
    role = SutraRole.Vidhi,
    action = SutraAction.AGAMA,
    scope = SutraScope.PRATYAYA,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        val ending = context.terms.last()
        if (context.terms.any { it.kind == TermKind.DHATU && it.gana == DhatuGana.ADADI }) return false
        val hasAEndingAnga = context.terms.any {
            it.id in setOf("shap", "shyan", "sha", "sya") && dev.panini.shiksha.Varnamala.endsWithA(it.surface)
        }
        return context.effectiveContext.rupa.lakara in setOf(Lakara.LAT, Lakara.LANG, Lakara.LRNG) &&
            ending.upadesha in setOf("आताम्", "आथाम्") &&
            hasAEndingAnga &&
            context.terms.none { it.id == "ato-ngit-it" }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val ending = context.terms.last()
        if (context.effectiveContext.rupa.lakara in setOf(Lakara.LANG, Lakara.LRNG)) {
            return DerivationChange(
                context.replaceTerm(ending.id, ending.copy(surface = "इ${ending.surface.drop(1)}")),
                "7.2.81 replaces the initial आ of ${ending.upadesha} with इ after an a-ending अङ्ग.",
            )
        }
        val augment = DerivationTerm(
            "ato-ngit-it", "इट्", TermKind.AGAMA,
            upadesha = "इट्",
            createdBySutra = sutra,
            itProcessingPhase = ItProcessingPhase.RAW_UPADESHA,
            augmentTargetId = ending.id,
            mergeIntoAugmentTarget = false,
        )
        return DerivationChange(
            context.copy(terms = context.terms.dropLast(1) + augment + ending),
            "7.2.81 inserts इट् after an a-ending अङ्ग before ${ending.upadesha}.",
        )
    }
}
