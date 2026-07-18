package dev.panini.ashtadhyayi.adhyaya3.pada4

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationalEnvironment
import dev.panini.derivation.DerivationalMeaning
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.HasDerivationalEnvironment
import dev.panini.derivation.HasRequestedMeaning
import dev.panini.derivation.TermKind
import dev.panini.ganapatha.GanaPatha
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** 3.4.74: भीमादयोऽपादाने. */
object BhimadayoApadaneSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.4.74", text = "भीमादयोऽपादाने",
    hindiExplanation = "अपादान अर्थ में भीमादि शब्द उणादि-प्रत्ययान्त रूप में निपातित हैं।",
    type = SutraType.ATIDESHA, chapter = 3, pada = 4, optional = false, kramaValue = 340074,
    role = SutraRole.Atidesha, action = SutraAction.ATIDESHA, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        HasRequestedMeaning(DerivationalMeaning.APADANA).matches(context) &&
            !HasDerivationalEnvironment(DerivationalEnvironment.UNADI_LICENSED).matches(context) &&
            context.terms.any { it.kind == TermKind.PRATIPADIKA && GanaPatha.isEligibleMember(44, it.surface, it.lexicalUses) }

    override fun apply(context: DerivationState): DerivationChange = DerivationChange(
        context.copy(
            context = context.effectiveContext.copy(
                environments = context.effectiveContext.environments + DerivationalEnvironment.UNADI_LICENSED,
            ),
        ),
        "3.4.74 licenses the eligible भीमादि lexical formation in an apādāna relation.",
    )
}
