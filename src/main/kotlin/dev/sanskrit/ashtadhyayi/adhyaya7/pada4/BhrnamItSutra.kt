package dev.sanskrit.ashtadhyayi.adhyaya7.pada4

import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.derivation.TermKind
import dev.sanskrit.shiksha.Samjna
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

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
