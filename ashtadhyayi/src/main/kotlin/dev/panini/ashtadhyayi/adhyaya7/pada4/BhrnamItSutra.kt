package dev.panini.ashtadhyayi.adhyaya7.pada4

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.TermKind
import dev.panini.shiksha.Samjna
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** 7.4.76: भृञामित्. The ऋ of a reduplicated भृञ् root is replaced by इ. */
object BhrnamItSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.4.76",
    text = "भृञामित्",
    hindiExplanation = "भृञ्-धातु के अभ्यास में ऋ-वर्ण के स्थान पर इ होता है।",
    type = SutraType.NITYA,
    chapter = 7,
    pada = 4,
    optional = false,
    kramaValue = 740076,
    role = SutraRole.Apavada,
    action = SutraAction.ADESHA,
    scope = SutraScope.DHATU,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        val abhyasa = context.terms.firstOrNull { it.id == "abhyasa" } ?: return false
        val dhatu = context.terms.firstOrNull { it.kind == TermKind.DHATU && it.id != "abhyasa" } ?: return false
        return context.samjnas.any { it.targetId == abhyasa.id && it.samjna == Samjna.ABHYASA } &&
            dhatu.surface == "भृ" && dhatu.upadesha?.endsWith("भृञ्") == true &&
            abhyasa.surface.any { it in setOf('ऋ', 'ॠ', 'ृ', 'ॄ') }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val abhyasa = context.terms.first { it.id == "abhyasa" }
        val substituted = abhyasa.surface
            .replace('ऋ', 'इ').replace('ॠ', 'इ')
            .replace('ृ', 'ि').replace('ॄ', 'ि')
        return DerivationChange(
            context.replaceTerm(abhyasa.id, abhyasa.copy(surface = substituted)),
            "7.4.76 replaces ऋ with इ in the भृञ् abhyāsa ${abhyasa.surface}.",
        )
    }
}
