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
 * 7.2.117: taddhiteṣv acām ādeḥ.
 * Substitutes Vṛddhi for the first vowel of a stem before a ñ-it or ṇ-it Taddhita affix.
 */
object TaddhitesvAcamAdehSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.2.117",
    text = "तद्धितेष्वचामादेः",
    hindiExplanation = "ञिद् या णिद् तद्धित प्रत्यय परे होने पर अङ्ग के प्रथम अच् (स्वर) को वृद्धि आदेश होता है।",
    type = SutraType.NITYA,
    chapter = 7,
    pada = 2,
    optional = false,
    kramaValue = 720117,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DHATU,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        val pratyaya = context.terms.lastOrNull { it.kind == TermKind.PRATYAYA } ?: return false
        val isTaddhita = "4.1.76" in context.activeAdhikaras ||
            pratyaya.upadesha in setOf("अण्", "इञ्", "यञ्", "फक्", "ढक्", "वत्", "तसिल्", "त्रल्", "आयन्", "एय्", "ईन्", "ईय्", "इय्")
        if (!isTaddhita) return false

        val isNgitOrNit = pratyaya.hasEffectiveMarker(ItMarker.NYIT) ||
            pratyaya.hasEffectiveMarker(ItMarker.NIT)
        if (!isNgitOrNit) return false

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
            explanation = "7.2.117 applies initial vowel Vṛddhi to '${stem.surface}' -> '$newSurface'.",
        )
    }

    private fun isAlreadyVrddhi(s: String): Boolean = when {
        s.startsWith("दा") || s.startsWith("गा") || s.startsWith("वा") || s.startsWith("भा") || s.startsWith("रा") -> true
        s.startsWith("वै") || s.startsWith("सै") || s.startsWith("दै") || s.startsWith("शै") -> true
        s.startsWith("औ") || s.startsWith("सौ") || s.startsWith("गौ") -> true
        else -> false
    }

}
