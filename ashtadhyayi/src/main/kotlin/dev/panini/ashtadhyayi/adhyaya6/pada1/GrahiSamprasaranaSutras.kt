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
        if (index < 0 || context.terms[index].surface != "ग्रह्") return false
        return context.terms.getOrNull(index + 1)?.matchesUpadesha("श्ना") == true
    }
    override fun apply(context: DerivationState): DerivationChange {
        val root = context.terms.first { it.kind == TermKind.DHATU }
        return DerivationChange(context.substituteTermVarnas(root.id,
            root.varnas.map { if (it == Vyanjana.RA) Svara.R else it },
            Vyanjana.RA, listOf(Svara.R), sutra), "6.1.16 gives samprasāraṇa to grah before ṅ-it śnā.")
    }
}

/** 6.1.108 contracts a samprasāraṇa vowel and its following vowel. */
object SamprasaranacCaSutra : Sutra<DerivationState, DerivationChange>(
    "6.1.108", "सम्प्रसारणाच्च", "सम्प्रसारण के परे स्वर होने पर पूर्वरूप एकादेश होता है।",
    type = SutraType.NITYA, chapter = 6, pada = 1, optional = false, kramaValue = 610108,
    role = SutraRole.Vidhi, action = SutraAction.ADESHA, scope = SutraScope.DHATU,
), DerivationSutra {
    private fun target(context: DerivationState) = context.terms.firstOrNull { term ->
        context.substitutions.any { it.targetId == term.id && it.sutra == "6.1.16" } &&
            term.varnas.zipWithNext().any { (a, b) -> a == Svara.R && b is Svara }
    }
    override fun matches(context: DerivationState) = target(context) != null
    override fun apply(context: DerivationState): DerivationChange {
        val root = requireNotNull(target(context))
        val index = root.varnas.indexOfFirst { it == Svara.R } + 1
        val removed = root.varnas[index]
        return DerivationChange(context.substituteTermVarnas(root.id,
            root.varnas.filterIndexed { i, _ -> i != index }, removed, emptyList(), sutra),
            "6.1.108 retains the samprasāraṇa vowel as the pūrvarūpa of the vowel pair.")
    }
}
