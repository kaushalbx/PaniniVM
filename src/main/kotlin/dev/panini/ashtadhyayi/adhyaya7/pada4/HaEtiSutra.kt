package dev.panini.ashtadhyayi.adhyaya7.pada4

import dev.panini.core.Lakara
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** 7.4.52: ह एति. The final s of तास् becomes h before the e-ending of luṭ. */
object HaEtiSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.4.52",
    text = "ह एति",
    hindiExplanation = "लुट् में एकारादेश परे होने पर तास् के सकार के स्थान पर हकार होता है।",
    type = SutraType.NITYA,
    chapter = 7,
    pada = 4,
    optional = false,
    kramaValue = 740052,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.effectiveContext.rupa.lakara != Lakara.LUT) return false
        val tasiIndex = context.terms.indexOfFirst { it.upadesha == "तासि" && it.surface.endsWith("स्") }
        return tasiIndex >= 0 && context.terms.getOrNull(tasiIndex + 1)?.surface == "ए"
    }

    override fun apply(context: DerivationState): DerivationChange {
        val tasiIndex = context.terms.indexOfFirst { it.upadesha == "तासि" && it.surface.endsWith("स्") }
        val tasi = context.terms[tasiIndex]
        val ending = context.terms[tasiIndex + 1]
        return DerivationChange(
            context
                .replaceTerm(tasi.id, tasi.copy(surface = tasi.surface.removeSuffix("स्")))
                .replaceTerm(ending.id, ending.copy(surface = "हे")),
            "7.4.52 replaces the final स of तास् with ह before the e-ending.",
        )
    }
}
