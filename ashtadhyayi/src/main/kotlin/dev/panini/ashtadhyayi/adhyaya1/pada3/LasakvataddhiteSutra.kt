package dev.panini.ashtadhyayi.adhyaya1.pada3

import dev.panini.core.ItMarker
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.ItDesignation
import dev.panini.derivation.TermKind
import dev.panini.shiksha.Samjna
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
    stage = dev.panini.sutra.SutraStage.IT_PROCESSING,
), DerivationSutra {
    fun hasSamjnaTarget(state: DerivationState): Boolean {
        if (state.stage != DerivationStage.PRATYAYA_SELECTED && state.terms.none { it.itProcessingPending }) return false

        return state.terms.any { term ->
            term.kind == TermKind.PRATYAYA && term.surface.isNotEmpty() && isLaShaKu(term.surface.first()) &&
                (term.itDesignations + term.deferredItDesignations).none { it.start == 0 }
        }
    }

    fun assignSamjna(state: DerivationState): DerivationChange {
        val newTerms = state.terms.map { term ->
            if (term.kind == TermKind.PRATYAYA && term.surface.isNotEmpty()) {
                val firstChar = term.surface.first()
                if (isLaShaKu(firstChar)) {
                    val marker = when (firstChar.toString()) {
                        "ल" -> ItMarker.KIT
                        "श" -> ItMarker.SH
                        "ङ" -> ItMarker.NGIT
                        else -> if (isKu(firstChar)) ItMarker.KIT else ItMarker.KIT
                    }
                    val sign = term.surface.getOrNull(1)
                    val vowel = when (sign) {
                        '्' -> ""
                        'ा' -> "आ"; 'ि' -> "इ"; 'ी' -> "ई"; 'ु' -> "उ"; 'ू' -> "ऊ"
                        'ृ' -> "ऋ"; 'ॄ' -> "ॠ"; 'ॢ' -> "ऌ"; 'े' -> "ए"; 'ै' -> "ऐ"; 'ो' -> "ओ"; 'ौ' -> "औ"
                        else -> "अ"
                    }
                    val length = if (sign == '्' || sign in setOf('ा', 'ि', 'ी', 'ु', 'ू', 'ृ', 'ॄ', 'ॢ', 'े', 'ै', 'ो', 'ौ')) 2 else 1
                    val designation = ItDesignation(0, length, vowel, marker, sutra, designatedText = term.surface.substring(0, length))
                    // The initial झ् of tiṅ झ/झि is designated here, but
                    // 7.1.3/7.1.5 (or the liṭ replacement) supersedes that
                    // exact segment before 1.3.9. Keep the designation alive
                    // for that substitution to consume explicitly.
                    val awaitsJhaSubstitution = term.itProcessingPending && term.upadesha in setOf("झ", "झि")
                    term.copy(
                        itMarkers = term.itMarkers + marker,
                        itProcessingPhase = when {
                            awaitsJhaSubstitution -> dev.panini.derivation.ItProcessingPhase.DEFERRED_SUBSTITUTION
                            term.itProcessingPending -> dev.panini.derivation.ItProcessingPhase.DESIGNATED
                            else -> term.itProcessingPhase
                        },
                        itDesignations = if (term.itProcessingPending && !awaitsJhaSubstitution) term.itDesignations + designation else term.itDesignations,
                        deferredItDesignations = if (awaitsJhaSubstitution || !term.itProcessingPending) term.deferredItDesignations + designation else term.deferredItDesignations,
                    )
                } else term
            } else term
        }

        return DerivationChange(
            state = state.copy(terms = newTerms),
            explanation = "1.3.8: Assigned it-status to initial l, ś, or ku."
        )
    }

    override fun matches(context: DerivationState): Boolean = hasSamjnaTarget(context)

    override fun apply(context: DerivationState): DerivationChange = assignSamjna(context)

    private fun isLaShaKu(c: Char): Boolean = c == 'ल' || c == 'श' || isKu(c)
    private fun isKu(c: Char): Boolean = c in setOf('क', 'ख', 'ग', 'घ', 'ङ')
}
