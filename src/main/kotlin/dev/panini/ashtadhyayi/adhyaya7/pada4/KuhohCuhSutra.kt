package dev.panini.ashtadhyayi.adhyaya7.pada4

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.shiksha.Samjna
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** 7.4.62: कुहोश्चुः. */
object KuhohCuhSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.4.62", text = "कुहोश्चुः",
    hindiExplanation = "अभ्यास के आरम्भ में कवर्ग और हकार के स्थान पर चवर्ग का आदेश होता है।",
    type = SutraType.NITYA, chapter = 7, pada = 4, optional = false, kramaValue = 740062,
    role = SutraRole.Vidhi, action = SutraAction.ADESHA, scope = SutraScope.DHATU,
), DerivationSutra {
    private val cuhSubstitutions = mapOf('क' to 'च', 'ख' to 'छ', 'ग' to 'ज', 'घ' to 'झ', 'ङ' to 'ञ', 'ह' to 'ज')

    override fun matches(context: DerivationState): Boolean {
        val abhyasa = context.terms.firstOrNull { it.id == "abhyasa" } ?: return false
        return context.samjnas.any { it.targetId == abhyasa.id && it.samjna == Samjna.ABHYASA } &&
            abhyasa.surface.firstOrNull() in cuhSubstitutions
    }

    override fun apply(context: DerivationState): DerivationChange {
        val abhyasa = context.terms.first { it.id == "abhyasa" }
        val replacement = cuhSubstitutions.getValue(abhyasa.surface.first())
        val substituted = replacement + abhyasa.surface.drop(1)
        return DerivationChange(
            context.replaceTerm(abhyasa.id, abhyasa.copy(surface = substituted)),
            "7.4.62 changes initial ${abhyasa.surface.first()} of the abhyāsa to $replacement.",
        )
    }
}
