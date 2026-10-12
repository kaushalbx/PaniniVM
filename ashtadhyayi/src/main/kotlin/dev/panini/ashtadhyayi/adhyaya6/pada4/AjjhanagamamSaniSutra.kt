package dev.panini.ashtadhyayi.adhyaya6.pada4

import dev.panini.core.SanadiAffix
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.TermKind
import dev.panini.derivation.matchesAffix
import dev.panini.shiksha.Svara
import dev.panini.shiksha.toDirgha
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** 6.4.16: अज्झनगमां सनि — a final vowel of an aṅga is lengthened before सन्. */
object AjjhanagamamSaniSutra : Sutra<DerivationState, DerivationChange>(
    number = "6.4.16",
    text = "अज्झनगमां सनि",
    hindiExplanation = "सन् परे अजन्त अङ्ग के अन्त्य स्वर का दीर्घ होता है।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 4,
    optional = false,
    kramaValue = 640016,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    private val lengthenable = setOf(Svara.I, Svara.U, Svara.R)

    override fun matches(context: DerivationState): Boolean {
        if (context.terms.none { it.kind == TermKind.PRATYAYA && it.matchesAffix(SanadiAffix.SAN) }) return false
        // The aṅga, not the subsequently designated abhyāsa, receives this operation.
        // Requiring the reduplication to exist also preserves the grammatical order 6.1.9 → 6.4.16.
        if (context.terms.none { it.id == "abhyasa" }) return false
        val anga = context.terms.firstOrNull { it.kind == TermKind.DHATU && it.id != "abhyasa" } ?: return false
        return anga.varnas.lastOrNull() in lengthenable
    }

    override fun apply(context: DerivationState): DerivationChange {
        val anga = context.terms.first { it.kind == TermKind.DHATU && it.id != "abhyasa" }
        val source = anga.varnas.last() as Svara
        val replacement = source.toDirgha()
        return DerivationChange(
            state = context.replaceTermVarna(anga.id, anga.varnas.lastIndex, listOf(replacement), sutra)
                .copy(stage = DerivationStage.ANGAKARYA),
            explanation = "6.4.16 lengthens the final vowel of ${anga.surface} before सन्.",
        )
    }
}
