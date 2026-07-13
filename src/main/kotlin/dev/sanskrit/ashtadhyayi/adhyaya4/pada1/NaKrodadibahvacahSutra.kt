package dev.sanskrit.ashtadhyayi.adhyaya4.pada1

import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.derivation.TermKind
import dev.sanskrit.ganapatha.GanaPatha
import dev.sanskrit.shiksha.SemanticFeature
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

/** 4.1.56: न क्रोडादिबह्वचः. */
object NaKrodadibahvacahSutra : Sutra<DerivationState, DerivationChange>(
    number = "4.1.56", text = "न क्रोडादिबह्वचः",
    hindiExplanation = "स्वाङ्ग अर्थ में क्रोडादि और बह्वच् प्रातिपदिकों से ङीष् प्रत्यय नहीं होता।",
    type = SutraType.NISHEDHA, chapter = 4, pada = 1, optional = false, kramaValue = 410056,
    role = SutraRole.Nishedha, action = SutraAction.NISHEDHA, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        SemanticFeature.STRI in context.semanticFeatures &&
            SemanticFeature.SVANGA in context.semanticFeatures &&
            context.terms.any { it.kind == TermKind.PRATIPADIKA && GanaPatha.isEligibleMember(50, it.surface, it.lexicalUses) }

    override fun apply(context: DerivationState): DerivationChange = DerivationChange(
        context.blockSutra("4.1.54", "4.1.56"),
        "4.1.56 blocks the ङीष् outcome otherwise inherited from the svāṅga feminine rule.",
    )
}
