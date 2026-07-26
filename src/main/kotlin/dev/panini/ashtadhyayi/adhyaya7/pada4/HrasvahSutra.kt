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
