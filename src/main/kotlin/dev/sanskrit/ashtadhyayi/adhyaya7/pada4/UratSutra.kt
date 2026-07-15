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

/** 7.4.66: उरत्. */
object UratSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.4.66", text = "उरत्",
    hindiExplanation = "अभ्यास में ऋ-वर्ण के स्थान पर अकार का आदेश होता है।",
    type = SutraType.NITYA, chapter = 7, pada = 4, optional = false, kramaValue = 740066,
    role = SutraRole.Vidhi, action = SutraAction.ADESHA, scope = SutraScope.DHATU,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        val abhyasa = context.terms.firstOrNull { it.id == "abhyasa" } ?: return false
        return context.samjnas.any { it.targetId == abhyasa.id && it.samjna == Samjna.ABHYASA } &&
            abhyasa.surface.any { it == 'ऋ' || it == 'ॠ' || it == 'ृ' || it == 'ॄ' }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val abhyasa = context.terms.first { it.id == "abhyasa" }
        val substituted = abhyasa.surface
            .replace('ऋ', 'अ').replace('ॠ', 'अ')
            .replace("ृ", "").replace("ॄ", "")
        return DerivationChange(
            context.replaceTerm(abhyasa.id, abhyasa.copy(surface = substituted)),
            "7.4.66 replaces ऋ in the abhyāsa ${abhyasa.surface} with अ.",
        )
    }
}
