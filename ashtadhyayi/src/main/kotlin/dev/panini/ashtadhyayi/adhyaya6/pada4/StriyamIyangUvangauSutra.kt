package dev.panini.ashtadhyayi.adhyaya6.pada4

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.core.SupAffix
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Varna
import dev.panini.shiksha.Vyanjana
import dev.panini.sutra.NimittaScope
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 6.4.79: striyāḥ.
 * The 'strī' stem receives 'iyaṅ' (iy) substitution before vowel-initial case affixes (yielding striyam, striyā, etc.).
 */
object StriyamIyangUvangauSutra : Sutra<DerivationState, DerivationChange>(
    number = "6.4.79",
    text = "स्त्रियाः",
    hindiExplanation = "स्त्री अङ्ग के स्थान पर इयङ् (इय्) आदेश होता है अच्-आदि सर्वनामस्थान/विभक्ति परे होने पर।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 4,
    optional = false,
    kramaValue = 640079,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
    nimittaScope = NimittaScope.BOTH,
    dependencies = setOf("6.4.1")
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if ("6.4.1" !in context.activeAdhikaras) return false
        if (context.terms.size < 2) return false

        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()

        if (stem.varnas == striyVarnas) return false
        val isStriStem = stem.varnas == striiVarnas || stem.upadeshaVarnas == striiVarnas
        if (!isStriStem) return false

        val isVowelAffix = affix.varnas.firstOrNull() is Svara ||
            vowelConditionedSup.any { affix.matchesUpadesha(it.upadesha) }
        return isVowelAffix
    }

    override fun apply(context: DerivationState): DerivationChange {
        val stem = context.terms[context.terms.size - 2]

        return DerivationChange(
            state = context.substituteTermVarnas(stem.id, striyVarnas, Svara.II, listOf(Svara.I, Vyanjana.YA), sutra)
                .copy(stage = DerivationStage.ANGAKARYA),
            explanation = "6.4.79: Applied 'iyaṅ' (iy) substitution to 'strī' stem before vowel affix (becoming striy-)."
        )
    }

    private val striiVarnas: List<Varna> = listOf(Vyanjana.SA, Vyanjana.TA, Vyanjana.RA, Svara.II)
    private val striyVarnas: List<Varna> = listOf(Vyanjana.SA, Vyanjana.TA, Vyanjana.RA, Svara.I, Vyanjana.YA)
    private val vowelConditionedSup = setOf(
        SupAffix.AM, SupAffix.AU, SupAffix.JAS, SupAffix.SAS, SupAffix.TA, SupAffix.NGE,
        SupAffix.NGASI, SupAffix.NGAS, SupAffix.OS_6, SupAffix.AM_6, SupAffix.NGI, SupAffix.OS_7,
    )
}
