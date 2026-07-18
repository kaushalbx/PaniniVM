package dev.panini.ashtadhyayi.adhyaya1.pada3

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.ItMarker
import dev.panini.derivation.TermKind
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 1.3.8: la-śa-ku-a-taddhite.
 * Initial l, ś, and ka-varga characters in a non-taddhita affix are it-markers.
 */
object LasakvataddhiteSutra : Sutra<DerivationState, DerivationChange>(
    number = "1.3.8",
    text = "लशक्वतद्धिते",
    hindiExplanation = "अतद्धित प्रत्यय के आदि में ल्, श् और क-वर्ग की इत् संज्ञा होती है।",
    type = SutraType.SAMJNA,
    chapter = 1,
    pada = 3,
    optional = false,
    kramaValue = 130008,
    role = SutraRole.Samjna,
    action = SutraAction.SAMJNA,
    scope = SutraScope.PRATYAYA,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.stage != DerivationStage.PRATYAYA_SELECTED) return false

        return context.terms.any { term ->
            term.kind == TermKind.PRATYAYA && term.surface.isNotEmpty() && isLaShaKu(term.surface.first())
        }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val newTerms = context.terms.map { term ->
            if (term.kind == TermKind.PRATYAYA && term.surface.isNotEmpty()) {
                val firstChar = term.surface.first()
                if (isLaShaKu(firstChar)) {
                    val marker = when (firstChar.toString()) {
                        "ल" -> ItMarker.KIT
                        "श" -> ItMarker.SH
                        "ङ" -> ItMarker.NGIT
                        else -> if (isKu(firstChar)) ItMarker.KIT else ItMarker.KIT
                    }
                    term.copy(itMarkers = term.itMarkers + marker)
                } else term
            } else term
        }

        return DerivationChange(
            state = context.copy(terms = newTerms),
            explanation = "1.3.8: Assigned it-status to initial l, ś, or ku."
        )
    }

    private fun isLaShaKu(c: Char): Boolean = c == 'ल' || c == 'श' || isKu(c)
    private fun isKu(c: Char): Boolean = c in setOf('क', 'ख', 'ग', 'घ', 'ङ')
}
