package dev.panini.ashtadhyayi.adhyaya6.pada4

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.isHrasva
import dev.panini.shiksha.toDirgha
import dev.panini.sutra.NimittaScope
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 6.4.3: nāmi.
 * The final vowel of an aṅga is lengthened when followed by 'nām' (genitive plural).
 */
object NamiSutra : Sutra<DerivationState, DerivationChange>(
    number = "6.4.3",
    text = "नामि",
    hindiExplanation = "नाम् परे होने पर अङ्ग के अन्त्य स्वर को दीर्घ होता है।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 4,
    optional = false,
    kramaValue = 640003,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DHATU,
    nimittaScope = NimittaScope.EXTERNAL,
    dependencies = setOf("6.4.1", "7.1.54") // Depends on Anga jurisdiction and 'nuṭ' augment
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if ("6.4.1" !in context.activeAdhikaras) return false
        if (context.terms.size < 2) return false

        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()

        // 1. Check if affix is 'nām' (combination of nuṭ + ām)
        // In our engine, this appears as 'nuṭ-augment' + 'ām' or merged as 'नाम्'
        val nam = listOf(Vyanjana.NA, Svara.AA, Vyanjana.MA)
        val isNam = affix.varnas.take(nam.size) == nam ||
            affix.upadesha == "आम्" && context.terms.any { it.upadesha == "नुट्" }

        if (!isNam) return false

        // 2. Stem must end in a short vowel
        val final = stem.varnas.lastOrNull() ?: return false
        return final is Svara && final.isHrasva
    }

    override fun apply(context: DerivationState): DerivationChange {
        val stem = context.terms[context.terms.size - 2]
        val final = stem.varnas.last()
        val source = final as Svara
        val replacement = source.toDirgha()

        return DerivationChange(
            state = context.replaceTermVarna(stem.id, stem.varnas.lastIndex, listOf(replacement), sutra)
                .copy(stage = DerivationStage.ANGAKARYA),
            explanation = "6.4.3: Lengthened stem vowel before 'nām'."
        )
    }

}
