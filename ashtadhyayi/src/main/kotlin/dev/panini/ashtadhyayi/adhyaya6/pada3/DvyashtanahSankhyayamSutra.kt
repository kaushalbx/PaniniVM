package dev.panini.ashtadhyayi.adhyaya6.pada3

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.shiksha.Samjna
import dev.panini.core.SamasaType
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** 6.3.47: द्व्यष्टनः संख्यायामबहुव्रीह्यशीत्योः */
object DvyashtanahSankhyayamSutra : Sutra<DerivationState, DerivationChange>(
    number = "6.3.47",
    text = "द्व्यष्टनः संख्यायामबहुव्रीह्यशीत्योः",
    hindiExplanation = "संख्या उत्तरपद होने पर द्वि और अष्टन् के स्थान पर आ होता है।",
    type = SutraType.UTSARGA,
    chapter = 6,
    pada = 3,
    optional = false,
    kramaValue = 630047,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
    stage = dev.panini.sutra.SutraStage.ANGAKARYA,
), DerivationSutra {

    override fun matches(context: DerivationState): Boolean {
        if (context.terms.size < 2) return false
        if (context.effectiveContext.samasaType == SamasaType.BAHUVRIHI) return false
        val purvapada = context.terms.firstOrNull() ?: return false
        val uttarapada = context.terms.getOrNull(1) ?: return false

        val purvaMatch = purvapada.compoundNumeralStem() in setOf(CompoundNumeralStem.DVI, CompoundNumeralStem.ASTAN)
        val isUttaraSankhya = context.samjnas.any { it.targetId == uttarapada.id && it.samjna == Samjna.SANKHYA } &&
            uttarapada.compoundNumeralUttara()?.optional == false

        return purvaMatch && isUttaraSankhya
    }

    override fun apply(context: DerivationState): DerivationChange {
        val purvapada = context.terms.first()
        return DerivationChange(
            state = context.substituteCompoundNumeral(purvapada, sutra),
            explanation = "6.3.47 replaces the specified numeral's ending with ā."
        )
    }
}
