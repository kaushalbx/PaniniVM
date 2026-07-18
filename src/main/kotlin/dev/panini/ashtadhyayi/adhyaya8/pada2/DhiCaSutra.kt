package dev.panini.ashtadhyayi.adhyaya8.pada2

import dev.panini.derivation.*
import dev.panini.sutra.*

/** 8.2.25: धि च. */
object DhiCaSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.2.25", text = "धि च",
    hindiExplanation = "धकारादि प्रत्यय के पहले सिच् अथवा तास् के सकार का लोप होता है।",
    type = SutraType.NITYA, chapter = 8, pada = 2, optional = false, kramaValue = 820025,
    role = SutraRole.Vidhi, action = SutraAction.LOPA, scope = SutraScope.PRATYAYA,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        val targetIndex = context.terms.indexOfFirst {
            (it.upadesha == "सिच्" && it.surface == "स्") ||
                (it.upadesha == "तासि" && it.surface.endsWith("स्"))
        }
        return targetIndex >= 0 && context.terms.getOrNull(targetIndex + 1)?.surface?.startsWith("ध") == true
    }

    override fun apply(context: DerivationState): DerivationChange {
        val target = context.terms.first {
            (it.upadesha == "सिच्" && it.surface == "स्") ||
                (it.upadesha == "तासि" && it.surface.endsWith("स्"))
        }
        val nextState = if (target.upadesha == "सिच्") {
            context.removeTerm(target.id).copy(stage = DerivationStage.PADA_FORMED)
        } else {
            context.replaceTerm(target.id, target.copy(surface = target.surface.removeSuffix("स्")))
        }
        return DerivationChange(
            nextState,
            "8.2.25 deletes the स of ${target.upadesha} before a dh-initial suffix.",
        )
    }
}
