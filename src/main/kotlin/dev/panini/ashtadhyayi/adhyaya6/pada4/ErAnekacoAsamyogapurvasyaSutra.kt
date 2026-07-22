package dev.panini.ashtadhyayi.adhyaya6.pada4

import dev.panini.core.Lakara
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

/** 6.4.82: एरनेकाचोऽसंयोगपूर्वस्य. */
object ErAnekacoAsamyogapurvasyaSutra : Sutra<DerivationState, DerivationChange>(
    number = "6.4.82",
    text = "एरनेकाचोऽसंयोगपूर्वस्य",
    hindiExplanation = "अनेकाच् अङ्ग के असंयोगपूर्व इवर्णान्त धातु को अजादि प्रत्यय परे होने पर यण् आदेश होता है।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 4,
    optional = false,
    kramaValue = 640082,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DHATU,
), DerivationSutra {
    private val iVowels = setOf('इ', 'ई', 'ि', 'ी')
    private val vowelMatras = mapOf(
        'अ' to "", 'आ' to "ा", 'इ' to "ि", 'ई' to "ी", 'उ' to "ु", 'ऊ' to "ू",
        'ऋ' to "ृ", 'ॠ' to "ॄ", 'ऌ' to "ॢ", 'ए' to "े", 'ऐ' to "ै", 'ओ' to "ो", 'औ' to "ौ",
    )

    override fun matches(context: DerivationState): Boolean {
        val dhatuIndex = context.terms.indexOfFirst { it.kind == TermKind.DHATU && it.id != "abhyasa" }
        if (dhatuIndex < 0 || context.terms.none { it.id == "abhyasa" }) return false
        val dhatu = context.terms[dhatuIndex]
        val following = context.terms.getOrNull(dhatuIndex + 1) ?: return false
        val final = dhatu.surface.lastOrNull() ?: return false
        if (context.effectiveContext.rupa.lakara == Lakara.LIT &&
            context.terms.lastOrNull()?.establishedBySutras?.contains("1.2.5") != true
        ) return false
        val isReadyAffix = following.kind == TermKind.AGAMA || following.surface in setOf(
            "अ", "अतुस्", "उस्", "अथुस्", "ए", "आते", "इरे", "आथे",
        )
        return final in iVowels &&
            !isConjunctPreceded(dhatu.surface) &&
            isReadyAffix &&
            following.surface.firstOrNull() in vowelMatras
    }

    override fun apply(context: DerivationState): DerivationChange {
        val dhatuIndex = context.terms.indexOfFirst { it.kind == TermKind.DHATU && it.id != "abhyasa" }
        val dhatu = context.terms[dhatuIndex]
        val following = context.terms[dhatuIndex + 1]
        val firstVowel = following.surface.first()
        val yanStem = if (dhatu.surface.last() in setOf('ि', 'ी')) {
            dhatu.surface.dropLast(1) + "्य्"
        } else {
            dhatu.surface.dropLast(1) + "य्"
        }
        val merged = yanStem.dropLast(1) + requireNotNull(vowelMatras[firstVowel]) + following.surface.drop(1)
        val newTerms = context.terms.toMutableList().also {
            it[dhatuIndex] = dhatu.copy(surface = merged)
            it.removeAt(dhatuIndex + 1)
        }
        return DerivationChange(
            context.copy(
                terms = newTerms,
                droppedTerms = context.droppedTerms + following.copy(surface = ""),
                stage = DerivationStage.ANGAKARYA,
            ),
            "6.4.82 substitutes यण् for the non-conjunct-preceded final i-vowel of the many-vowel aṅga.",
        )
    }

    private fun isConjunctPreceded(surface: String): Boolean {
        val vowelIndex = surface.lastIndex
        return vowelIndex >= 3 && surface[vowelIndex - 2] == '्'
    }
}
