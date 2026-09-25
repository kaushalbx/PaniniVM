package dev.panini.ashtadhyayi.adhyaya7.pada4

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.toDevanagari
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

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
            abhyasa.varnas.any { it in setOf(Svara.R, Svara.RR) }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val abhyasa = context.terms.first { it.id == "abhyasa" }
        val source = abhyasa.varnas.first { it in setOf(Svara.R, Svara.RR) }
        val substituted = abhyasa.varnas.map { if (it in setOf(Svara.R, Svara.RR)) Svara.A else it }.toDevanagari()
        return DerivationChange(
            context.substituteTermSurface(abhyasa.id, substituted, source, listOf(Svara.A), sutra),
            "7.4.66 replaces ऋ in the abhyāsa ${abhyasa.surface} with अ.",
        )
    }
}
