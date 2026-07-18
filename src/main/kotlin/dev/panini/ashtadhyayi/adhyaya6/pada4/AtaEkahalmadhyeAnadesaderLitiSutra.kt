package dev.panini.ashtadhyayi.adhyaya6.pada4

import dev.panini.derivation.*
import dev.panini.sutra.*

/** 6.4.120: अत एकहल्मध्येऽनादेशादेर्लिटि. Implements the weak perfect stem exemplified by लभ् → लेभ्. */
object AtaEkahalmadhyeAnadesaderLitiSutra : Sutra<DerivationState, DerivationChange>(
    number = "6.4.120", text = "अत एकहल्मध्येऽनादेशादेर्लिटि",
    hindiExplanation = "लिट् में एक हल् से व्यवहित अकारान्त अभ्यास का लोप और धातु के अकार का एकार होता है।",
    type = SutraType.NITYA, chapter = 6, pada = 4, optional = false, kramaValue = 640120,
    role = SutraRole.Vidhi, action = SutraAction.ADESHA, scope = SutraScope.DHATU,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.effectiveContext.rupa.lakara != Lakara.LIT) return false
        val abhyasa = context.terms.firstOrNull { it.id == "abhyasa" } ?: return false
        val dhatu = context.terms.firstOrNull { it.kind == TermKind.DHATU && it.id != "abhyasa" } ?: return false
        return abhyasa.surface == "ल" && dhatu.upadesha == "डुलभँष्" && dhatu.surface == "लभ्"
    }

    override fun apply(context: DerivationState): DerivationChange {
        val dhatu = context.terms.first { it.kind == TermKind.DHATU && it.id != "abhyasa" }
        return DerivationChange(
            context.removeTerm("abhyasa", sutra = "6.4.120").replaceTerm(dhatu.id, dhatu.copy(surface = "लेभ्")),
            "6.4.120 deletes the abhyāsa and changes the root vowel अ to ए in the weak perfect stem लेभ्.",
        )
    }
}
