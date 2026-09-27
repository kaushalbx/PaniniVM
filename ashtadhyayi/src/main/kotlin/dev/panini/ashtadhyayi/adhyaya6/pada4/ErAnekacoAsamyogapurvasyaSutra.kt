package dev.panini.ashtadhyayi.adhyaya6.pada4

import dev.panini.core.Lakara
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.TermKind
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Varna
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.toDevanagari
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
    private val iVowels = setOf(Svara.I, Svara.II)

    override fun matches(context: DerivationState): Boolean {
        val dhatuIndex = context.terms.indexOfFirst { it.kind == TermKind.DHATU && it.id != "abhyasa" }
        if (dhatuIndex < 0 || context.terms.none { it.id == "abhyasa" }) return false
        val dhatu = context.terms[dhatuIndex]
        val following = context.terms.getOrNull(dhatuIndex + 1) ?: return false
        val final = dhatu.varnas.lastOrNull() ?: return false
        if (context.effectiveContext.rupa.lakara == Lakara.LIT &&
            context.terms.lastOrNull()?.establishedBySutras?.contains("1.2.5") != true
        ) return false
        val isReadyAffix = following.kind == TermKind.AGAMA || following.varnas in readyAffixes
        return final in iVowels &&
            !isConjunctPreceded(dhatu.varnas) &&
            isReadyAffix &&
            following.varnas.firstOrNull() is Svara
    }

    override fun apply(context: DerivationState): DerivationChange {
        val dhatuIndex = context.terms.indexOfFirst { it.kind == TermKind.DHATU && it.id != "abhyasa" }
        val dhatu = context.terms[dhatuIndex]
        val following = context.terms[dhatuIndex + 1]
        val source = dhatu.varnas.last()
        val mergedVarnas = dhatu.varnas.dropLast(1) + Vyanjana.YA + following.varnas
        return DerivationChange(
            context.mergeTermsByVarnaSubstitution(
                dhatu.id, following.id, mergedVarnas.toDevanagari(), source, listOf(Vyanjana.YA), sutra,
            ).copy(stage = DerivationStage.ANGAKARYA),
            "6.4.82 substitutes यण् for the non-conjunct-preceded final i-vowel of the many-vowel aṅga.",
        )
    }

    private fun isConjunctPreceded(varnas: List<Varna>): Boolean =
        varnas.size >= 3 && varnas[varnas.lastIndex - 1] is Vyanjana && varnas[varnas.lastIndex - 2] is Vyanjana

    private val readyAffixes: Set<List<Varna>> = setOf(
        listOf(Svara.A),
        listOf(Svara.A, Vyanjana.TA, Svara.U, Vyanjana.SA),
        listOf(Svara.U, Vyanjana.SA),
        listOf(Svara.A, Vyanjana.THA, Svara.U, Vyanjana.SA),
        listOf(Svara.E),
        listOf(Svara.AA, Vyanjana.TA, Svara.E),
        listOf(Svara.I, Vyanjana.RA, Svara.E),
        listOf(Svara.AA, Vyanjana.THA, Svara.E),
    )
}
