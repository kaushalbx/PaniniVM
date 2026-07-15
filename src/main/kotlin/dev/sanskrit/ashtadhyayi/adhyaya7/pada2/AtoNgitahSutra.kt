package dev.sanskrit.ashtadhyayi.adhyaya7.pada2

import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.derivation.DerivationTerm
import dev.sanskrit.derivation.Lakara
import dev.sanskrit.derivation.TermKind
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

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
        return context.effectiveContext.rupa.lakara == Lakara.LAT &&
            ending.upadesha in setOf("आताम्", "आथाम्") &&
            context.terms.any { it.id == "shap" && it.surface.endsWith('अ') } &&
            context.terms.none { it.id == "ato-ngit-it" }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val ending = context.terms.last()
        val augment = DerivationTerm("ato-ngit-it", "इ", TermKind.AGAMA, upadesha = "इट्")
        return DerivationChange(
            context.copy(terms = context.terms.dropLast(1) + augment + ending),
            "7.2.81 inserts इट् after the शप् अ before ${ending.upadesha}.",
        )
    }
}
