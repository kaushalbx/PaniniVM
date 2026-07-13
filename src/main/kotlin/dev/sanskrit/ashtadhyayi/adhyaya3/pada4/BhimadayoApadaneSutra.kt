package dev.sanskrit.ashtadhyayi.adhyaya3.pada4

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

/** 3.4.74: भीमादयोऽपादाने. */
object BhimadayoApadaneSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.4.74", text = "भीमादयोऽपादाने",
    hindiExplanation = "अपादान अर्थ में भीमादि शब्द उणादि-प्रत्ययान्त रूप में निपातित हैं।",
    type = SutraType.ATIDESHA, chapter = 3, pada = 4, optional = false, kramaValue = 340074,
    role = SutraRole.Atidesha, action = SutraAction.ATIDESHA, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        SemanticFeature.APADANA in context.semanticFeatures &&
            SemanticFeature.UNADI_LICENSED !in context.semanticFeatures &&
            context.terms.any { it.kind == TermKind.PRATIPADIKA && GanaPatha.isEligibleMember(44, it.surface, it.lexicalUses) }

    override fun apply(context: DerivationState): DerivationChange = DerivationChange(
        context.copy(semanticFeatures = context.semanticFeatures + SemanticFeature.UNADI_LICENSED),
        "3.4.74 licenses the eligible भीमादि lexical formation in an apādāna relation.",
    )
}
