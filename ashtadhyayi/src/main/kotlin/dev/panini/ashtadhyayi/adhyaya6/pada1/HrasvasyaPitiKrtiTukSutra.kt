package dev.panini.ashtadhyayi.adhyaya6.pada1

import dev.panini.core.ItMarker
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.TermKind
import dev.panini.shiksha.Samjna
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 6.1.71: ह्रस्वस्य पिति कृति तुक्.
 * Inserts the augment 'तुक्' (t) after a short vowel ending root before a P-it Kṛt suffix (like ल्यप्).
 */
object HrasvasyaPitiKrtiTukSutra : Sutra<DerivationState, DerivationChange>(
    number = "6.1.71",
    text = "ह्रस्वस्य पिति कृति तुक्",
    hindiExplanation = "ह्रस्व अवर्ण/इवर्ण/उवर्ण/ऋवर्णन्त अङ्ग के बाद पित् कृत् प्रत्यय परे होने पर तुक् आगम होता है।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 1,
    optional = false,
    kramaValue = 610071,
    role = SutraRole.Vidhi,
    action = SutraAction.AGAMA,
    scope = SutraScope.DERIVATION,
    stage = dev.panini.sutra.SutraStage.ANGAKARYA,
), DerivationSutra {
    private val shortVowels = setOf('इ', 'ि', 'उ', 'ु', 'ऋ', 'ृ', 'अ')

    override fun matches(context: DerivationState): Boolean {
        val stem = context.terms.firstOrNull { it.kind == TermKind.DHATU } ?: return false
        val suffix = context.terms.lastOrNull {
            it.kind == TermKind.PRATYAYA &&
                (it.itMarkers.contains(ItMarker.P) || it.sthaniProps?.itMarkers?.contains(ItMarker.P) == true) &&
                it.upadesha in setOf("ल्यप्", "ल्पँ")
        } ?: return false

        val isShortVowelEnding = stem.surface.isNotEmpty() && shortVowels.any { stem.surface.endsWith(it) }
        val isPitKrt = context.allEffectiveTerms.none { it.id == "tuk_agama" }
        return isShortVowelEnding && isPitKrt
    }

    override fun apply(context: DerivationState): DerivationChange {
        val stemIndex = context.terms.indexOfFirst { it.kind == TermKind.DHATU }
        val tukTerm = DerivationTerm(
            id = "tuk_agama",
            surface = "तुँक्",
            kind = TermKind.AGAMA,
            upadesha = "तुक्",
            createdBySutra = sutra,
            itProcessingPending = true,
            augmentTargetId = context.terms[stemIndex].id,
        )
        val newTerms = context.terms.take(stemIndex + 1) + tukTerm + context.terms.drop(stemIndex + 1)
        return DerivationChange(
            state = context.copy(
                terms = newTerms,
                stage = maxOf(context.stage, DerivationStage.ANGAKARYA),
            ),
            explanation = "6.1.71 inserts तुक् (त्) after short-vowel root before P-it Kṛt affix."
        )
    }
}
