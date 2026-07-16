package dev.sanskrit.ashtadhyayi.adhyaya8.pada2

import dev.sanskrit.derivation.*
import dev.sanskrit.sutra.*

/** 8.2.25: धि च. */
object DhiCaSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.2.25", text = "धि च",
    hindiExplanation = "धकारादि प्रत्यय के पहले सिच् के सकार का लोप होता है।",
    type = SutraType.NITYA, chapter = 8, pada = 2, optional = false, kramaValue = 820025,
    role = SutraRole.Vidhi, action = SutraAction.LOPA, scope = SutraScope.PRATYAYA,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        val sicIndex = context.terms.indexOfFirst { it.upadesha == "सिच्" && it.surface == "स्" }
        return sicIndex >= 0 && context.terms.getOrNull(sicIndex + 1)?.surface?.startsWith("ध") == true
    }

    override fun apply(context: DerivationState): DerivationChange {
        val sic = context.terms.first { it.upadesha == "सिच्" && it.surface == "स्" }
        return DerivationChange(
            context.removeTerm(sic.id).copy(stage = DerivationStage.PADA_FORMED),
            "8.2.25 deletes the स of सिच् before a dh-initial suffix.",
        )
    }
}
