package dev.panini.ashtadhyayi.adhyaya6.pada1

import dev.panini.derivation.*
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.sutra.*

/** Executable grah branch of 6.1.16 before the ṅ-it śnā suffix. */
object GrahijyaSamprasaranaSutra : Sutra<DerivationState, DerivationChange>(
    "6.1.16", "ग्रहिज्यावयिव्यधिवष्टिविचतिवृश्चतिपृच्छतिभृज्जतीनां ङिति च",
    "ग्रह् आदि धातुओं को कित् अथवा ङित् प्रत्यय परे सम्प्रसारण होता है।",
    type = SutraType.NITYA, chapter = 6, pada = 1, optional = false, kramaValue = 610016,
    role = SutraRole.Vidhi, action = SutraAction.ADESHA, scope = SutraScope.DHATU,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        val index = context.terms.indexOfFirst { it.kind == TermKind.DHATU }
        if (index < 0 || context.terms[index].varnas != listOf(Vyanjana.GA, Vyanjana.RA, Svara.A, Vyanjana.HA)) return false
        return context.terms.getOrNull(index + 1)?.matchesUpadesha("श्ना") == true
    }
    override fun apply(context: DerivationState): DerivationChange {
        val root = context.terms.first { it.kind == TermKind.DHATU }
        return DerivationChange(context.replaceTermVarna(root.id, 1, listOf(Svara.R), sutra),
            "6.1.16 gives samprasāraṇa to grah before ṅ-it śnā.")
    }
}

/** 6.1.108 contracts a samprasāraṇa vowel and its following vowel. */
object SamprasaranacCaSutra : Sutra<DerivationState, DerivationChange>(
    "6.1.108", "सम्प्रसारणाच्च", "सम्प्रसारण के परे स्वर होने पर पूर्वरूप एकादेश होता है।",
    type = SutraType.NITYA, chapter = 6, pada = 1, optional = false, kramaValue = 610108,
    role = SutraRole.Vidhi, action = SutraAction.ADESHA, scope = SutraScope.DHATU,
), DerivationSutra {
    // This executable branch follows the implemented grah operation above.
    // A bare vocalic ṛ elsewhere is not proof of samprasāraṇa.
    private fun target(context: DerivationState): Pair<DerivationTerm, Int>? {
        for (term in context.terms) {
            if (term.kind != TermKind.DHATU) continue
            for (change in context.substitutions.asReversed()) {
                if (change.targetId != term.id || change.sutra != "6.1.16" ||
                    change.replacementVarnas != listOf(Svara.R)) continue
                val index = change.sourceVarnaIndex ?: continue
                if (term.varnas.getOrNull(index) == Svara.R && term.varnas.getOrNull(index + 1) is Svara)
                    return term to index
            }
        }
        return null
    }
    override fun matches(context: DerivationState) = target(context) != null
    override fun apply(context: DerivationState): DerivationChange {
        val (root, index) = requireNotNull(target(context))
        return DerivationChange(context.replaceTermVarnaRange(root.id, index, 2,
            listOf(Svara.R), mapOf(0 to 0), sutra),
            "6.1.108 retains the samprasāraṇa vowel as the pūrvarūpa of the vowel pair.")
    }
}
