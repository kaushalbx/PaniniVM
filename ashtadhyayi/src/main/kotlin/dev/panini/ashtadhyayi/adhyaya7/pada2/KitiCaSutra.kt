package dev.panini.ashtadhyayi.adhyaya7.pada2

import dev.panini.core.ItMarker
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.TermKind
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType
import dev.panini.shiksha.applyInitialVrddhi

/**
 * 7.2.118: kiti ca.
 * Substitutes Vṛddhi for the first vowel of a stem before a kit Taddhita affix.
 */
object KitiCaSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.2.118",
    text = "किति च",
    hindiExplanation = "कित् तद्धित प्रत्यय परे होने पर भी अङ्ग के प्रथम अच् को वृद्धि आदेश होता है।",
    type = SutraType.NITYA,
    chapter = 7,
    pada = 2,
    optional = false,
    kramaValue = 720118,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DHATU,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        val pratyaya = context.terms.lastOrNull { it.kind == TermKind.PRATYAYA } ?: return false
        val isTaddhita = "4.1.76" in context.activeAdhikaras ||
            pratyaya.upadesha in setOf("अण्", "इञ्", "यञ्", "फक्", "ढक्", "वत्", "तसिल्", "त्रल्", "आयन्", "एय्", "ईन्", "ईय्", "इय्") ||
            pratyaya.id.contains("taddhita") || pratyaya.id.contains("apatya")
        if (!isTaddhita) return false

        val isKit = pratyaya.itMarkers.contains(ItMarker.KIT) ||
            pratyaya.upadesha == "फक्" || pratyaya.upadesha == "ढक्"
        if (!isKit) return false

        val stem = context.terms.firstOrNull { it.kind == TermKind.PRATIPADIKA } ?: return false
        return !isAlreadyVrddhi(stem.surface)
    }

    override fun apply(context: DerivationState): DerivationChange {
        val stemIndex = context.terms.indexOfFirst { it.kind == TermKind.PRATIPADIKA }
        val stem = context.terms[stemIndex]

        val newSurface = applyInitialVrddhi(stem.surface)
        val updatedStem = stem.copy(surface = newSurface)

        val newTerms = context.terms.toMutableList()
        newTerms[stemIndex] = updatedStem

        return DerivationChange(
            state = context.copy(
                terms = newTerms,
                stage = DerivationStage.ANGAKARYA,
            ),
            explanation = "7.2.118 applies initial vowel Vṛddhi before kit affix: '${stem.surface}' -> '$newSurface'.",
        )
    }

    private fun isAlreadyVrddhi(s: String): Boolean = when {
        s.startsWith("ना") || s.startsWith("वा") || s.startsWith("भा") || s.startsWith("रा") -> true
        s.startsWith("वै") || s.startsWith("सै") || s.startsWith("दै") -> true
        s.startsWith("औ") || s.startsWith("सौ") -> true
        else -> false
    }

}
