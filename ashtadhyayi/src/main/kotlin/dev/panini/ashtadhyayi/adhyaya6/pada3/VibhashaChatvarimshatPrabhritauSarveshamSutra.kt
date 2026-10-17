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

/** 6.3.49: विभाषा चत्वारिंशत्प्रभृतौ सर्वेषाम्। */
object VibhashaChatvarimshatPrabhritauSarveshamSutra : Sutra<DerivationState, DerivationChange>(
    number = "6.3.49", text = "विभाषा चत्वारिंशत्प्रभृतौ सर्वेषाम्",
    hindiExplanation = "चत्वारिंशत् से नवति तक द्वि, अष्टन् और त्रि के पूर्वोक्त आदेश विकल्प से होते हैं।",
    type = SutraType.VIBHASHA, chapter = 6, pada = 3, optional = true, kramaValue = 630049,
    role = SutraRole.Vidhi, action = SutraAction.ADESHA, scope = SutraScope.DERIVATION,
    stage = dev.panini.sutra.SutraStage.ANGAKARYA,
), DerivationSutra {

    override fun matches(context: DerivationState): Boolean {
        if (context.terms.size < 2) return false
        if (context.effectiveContext.samasaType == SamasaType.BAHUVRIHI) return false
        val purvapada = context.terms[0]
        val uttarapada = context.terms[1]
        return purvapada.compoundNumeralStem() != null &&
            uttarapada.compoundNumeralUttara()?.optional == true &&
            context.samjnas.any { it.targetId == uttarapada.id && it.samjna == Samjna.SANKHYA }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val purvapada = context.terms[0]
        return DerivationChange(
            context.substituteCompoundNumeral(purvapada, sutra),
            "6.3.49 optionally applies the numeral substitutions of 6.3.47–48."
        )
    }
}
