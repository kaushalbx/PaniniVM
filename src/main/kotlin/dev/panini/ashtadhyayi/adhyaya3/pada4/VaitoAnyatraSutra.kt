package dev.panini.ashtadhyayi.adhyaya3.pada4

import dev.panini.core.Lakara
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.LetEOption
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** 3.4.96: वैतोऽन्यत्र. Outside 3.4.95, LET optionally changes ending ए to ऐ. */
object VaitoAnyatraSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.4.96", text = "वैतोऽन्यत्र", hindiExplanation = "लेट् में 3.4.95 के विषय को छोड़कर ए के स्थान पर विकल्प से ऐ होता है।",
    type = SutraType.VIBHASHA, chapter = 3, pada = 4, optional = false, kramaValue = 340096,
    role = SutraRole.Vidhi, action = SutraAction.ADESHA, scope = SutraScope.PRATYAYA,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        val ending = context.terms.lastOrNull() ?: return false
        return context.effectiveContext.rupa.lakara == Lakara.LET &&
            context.effectiveContext.letEOption == LetEOption.AI &&
            ending.upadesha !in setOf("आताम्", "आथाम्") && ending.surface.endsWith("े")
    }

    override fun apply(context: DerivationState): DerivationChange {
        val ending = context.terms.last()
        return DerivationChange(
            context.replaceTerm(ending.id, ending.copy(surface = ending.surface.dropLast(1) + "ै")),
            "3.4.96 optionally replaces the ending's ए with ऐ.",
        )
    }
}
