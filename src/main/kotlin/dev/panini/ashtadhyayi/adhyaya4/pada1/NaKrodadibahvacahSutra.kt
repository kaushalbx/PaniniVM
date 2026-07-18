package dev.panini.ashtadhyayi.adhyaya4.pada1

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationalEnvironment
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.HasDerivationalEnvironment
import dev.panini.derivation.HasMorphosyntax
import dev.panini.derivation.TermKind
import dev.panini.ganapatha.GanaPatha
import dev.panini.shiksha.Linga
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** 4.1.56: न क्रोडादिबह्वचः. */
object NaKrodadibahvacahSutra : Sutra<DerivationState, DerivationChange>(
    number = "4.1.56", text = "न क्रोडादिबह्वचः",
    hindiExplanation = "स्वाङ्ग अर्थ में क्रोडादि और बह्वच् प्रातिपदिकों से ङीष् प्रत्यय नहीं होता।",
    type = SutraType.NISHEDHA, chapter = 4, pada = 1, optional = false, kramaValue = 410056,
    role = SutraRole.Nishedha, action = SutraAction.NISHEDHA, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        HasMorphosyntax(linga = Linga.STRI).matches(context) &&
            HasDerivationalEnvironment(DerivationalEnvironment.SVANGA).matches(context) &&
            context.terms.any { it.kind == TermKind.PRATIPADIKA && GanaPatha.isEligibleMember(50, it.surface, it.lexicalUses) }

    override fun apply(context: DerivationState): DerivationChange = DerivationChange(
        context.blockSutra("4.1.54", "4.1.56"),
        "4.1.56 blocks the ङीष् outcome otherwise inherited from the svāṅga feminine rule.",
    )
}
