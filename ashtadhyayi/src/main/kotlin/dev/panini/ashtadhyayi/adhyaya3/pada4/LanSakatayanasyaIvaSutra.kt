package dev.panini.ashtadhyayi.adhyaya3.pada4

import dev.panini.core.Lakara
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.WholeAffixDesignationPolicy
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 3.4.111 लङ् शाकटायनस्यैव.
 * Prescribes jus replacement for jhi in Laṅ according to Śākaṭāyana.
 */
object LanSakatayanasyaIvaSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.4.111", text = "लङ् शाकटायनस्यैव",
    hindiExplanation = "शाकटायन आचार्य के मत में लङ् लकार के 'झि' के स्थान पर 'जुस्' आदेश होता है।",
    type = SutraType.NITYA, chapter = 3, pada = 4, optional = true, kramaValue = 340111,
    role = SutraRole.Vidhi, action = SutraAction.ADESHA, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        context.effectiveContext.rupa.lakara == Lakara.LANG &&
        "3.4.111" in context.activeAdhikaras &&
        context.allEffectiveTerms.any { it.upadesha == "झि" }

    override fun apply(context: DerivationState): DerivationChange {
        val jhi = context.allEffectiveTerms.last { it.upadesha == "झि" }
        return DerivationChange(
            state = context.replaceWholeAffix(
                id = jhi.id,
                surface = "जुस्",
                sutra = sutra,
                policy = WholeAffixDesignationPolicy.FreshUpadesha,
                upadesha = "जुस्",
            ),
            explanation = "3.4.111 substitutes जुस् for झि in Laṅ.",
        )
    }
}
