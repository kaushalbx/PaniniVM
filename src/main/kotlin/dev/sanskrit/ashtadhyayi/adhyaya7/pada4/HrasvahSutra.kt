package dev.sanskrit.ashtadhyayi.adhyaya7.pada4

import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.shiksha.Samjna
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

/** 7.4.59: ह्रस्वः. */
object HrasvahSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.4.59", text = "ह्रस्वः",
    hindiExplanation = "अभ्यास में स्थित स्वर का ह्रस्व आदेश होता है।",
    type = SutraType.NITYA, chapter = 7, pada = 4, optional = false, kramaValue = 740059,
    role = SutraRole.Vidhi, action = SutraAction.ADESHA, scope = SutraScope.DHATU,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        val abhyasa = context.terms.firstOrNull { it.id == "abhyasa" } ?: return false
        return context.samjnas.any { it.targetId == abhyasa.id && it.samjna == Samjna.ABHYASA } &&
            abhyasa.surface.any { it in setOf('आ', 'ई', 'ऊ', 'ॠ', 'ा', 'ी', 'ू', 'ॄ') }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val abhyasa = context.terms.first { it.id == "abhyasa" }
        val shortened = abhyasa.surface
            .replace('आ', 'अ').replace('ई', 'इ').replace('ऊ', 'उ').replace('ॠ', 'ऋ')
            .replace("ा", "").replace('ी', 'ि').replace('ू', 'ु').replace('ॄ', 'ृ')
        return DerivationChange(context.replaceTerm(abhyasa.id, abhyasa.copy(surface = shortened)), "7.4.59 shortens the vowel of the abhyāsa ${abhyasa.surface}.")
    }
}
