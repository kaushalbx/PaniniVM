package dev.panini.ashtadhyayi.adhyaya7.pada3

import dev.panini.derivation.*
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Varna
import dev.panini.shiksha.Vyanjana
import dev.panini.sutra.*

private fun DerivationState.verbalRoot(): DerivationTerm? =
    terms.firstOrNull { it.kind == TermKind.DHATU && it.id != "abhyasa" }

/** 6.4.24: अनिदितां हल उपधायाः क्ङिति. The nasal drops before ṅit श्ना: बध्नाति. */
object AniditamNasalLopaSutra : Sutra<DerivationState, DerivationChange>(
    number = "6.4.24", text = "अनिदितां हल उपधायाः क्ङिति",
    hindiExplanation = "अनिदित् हलन्त धातु की उपधा के न् का क्ङित् परे लोप होता है।",
    type = SutraType.NITYA, chapter = 6, pada = 4, optional = false,
    kramaValue = 640024, role = SutraRole.Vidhi, action = SutraAction.LOPA, scope = SutraScope.DHATU,
), DerivationSutra {
    private val nasals: Set<Varna> = setOf(Vyanjana.NA, Vyanjana.NNA, Vyanjana.NYA, Vyanjana.NGA, Vyanjana.MA)

    private fun nasalIndex(context: DerivationState): Int? {
        val root = context.verbalRoot() ?: return null
        val next = context.terms.getOrNull(context.terms.indexOf(root) + 1) ?: return null
        if (!next.matchesUpadesha("श्ना") || root.varnas.size < 3) return null
        return (root.varnas.size - 2).takeIf { root.varnas[it] in nasals && root.varnas.last() is Vyanjana }
    }

    override fun matches(context: DerivationState) = nasalIndex(context) != null

    override fun apply(context: DerivationState): DerivationChange {
        val root = requireNotNull(context.verbalRoot())
        val index = requireNotNull(nasalIndex(context))
        return DerivationChange(context.replaceTermVarnaRange(root.id, index, 1, emptyList(), emptyMap(), sutra),
            "6.4.24 drops the penultimate nasal of the root before ṅit श्ना.")
    }
}

/** 8.2.32: दादेर्धातोर्घः. A द-initial root's final ह् becomes घ् before झल्: दोग्धि, दहति stays. */
object DaderDhatorGhahSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.2.32", text = "दादेर्धातोर्घः",
    hindiExplanation = "दकारादि धातु के ह् को झल् परे घ् होता है।",
    type = SutraType.NITYA, chapter = 8, pada = 2, optional = false,
    kramaValue = 820032, role = SutraRole.Vidhi, action = SutraAction.ADESHA, scope = SutraScope.DHATU,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        val root = context.verbalRoot() ?: return false
        val next = context.terms.getOrNull(context.terms.indexOf(root) + 1) ?: return false
        return root.varnas.firstOrNull() == Vyanjana.DA && root.varnas.lastOrNull() == Vyanjana.HA &&
            next.varnas.firstOrNull() in setOf(Vyanjana.TA, Vyanjana.THA, Vyanjana.SA, Vyanjana.DHA)
    }

    override fun apply(context: DerivationState): DerivationChange {
        val root = requireNotNull(context.verbalRoot())
        return DerivationChange(context.replaceTermVarna(root.id, root.varnas.lastIndex, listOf(Vyanjana.GHA), sutra),
            "8.2.32 replaces the final ह् of a द-initial root with घ् before झल्.")
    }
}

/** 7.3.93: ब्रुव ईट्. ब्रू adds ईट् to a हलादि pit सार्वधातुक: ब्रवीति. */
object BruvaItSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.3.93", text = "ब्रुव ईट्",
    hindiExplanation = "ब्रू से परे हलादि पित् सार्वधातुक को ईट् होता है।",
    type = SutraType.NITYA, chapter = 7, pada = 3, optional = false,
    kramaValue = 730093, role = SutraRole.Vidhi, action = SutraAction.AGAMA, scope = SutraScope.DHATU,
), DerivationSutra {
    private fun target(context: DerivationState): DerivationTerm? {
        val root = context.verbalRoot() ?: return null
        if (root.varnas.take(2) != listOf(Vyanjana.BA, Vyanjana.RA)) return null
        if (root.varnas.getOrNull(2) !in setOf(Svara.UU, Svara.O)) return null
        val next = context.terms.getOrNull(context.terms.indexOf(root) + 1) ?: return null
        return next.takeIf { term -> listOf("तिप्", "सिप्", "मिप्").any(term::matchesUpadesha) &&
            term.varnas.firstOrNull() is Vyanjana }
    }

    override fun matches(context: DerivationState) = target(context) != null

    override fun apply(context: DerivationState): DerivationChange {
        val affix = requireNotNull(target(context))
        return DerivationChange(context.replaceTermVarna(affix.id, 0, listOf(Svara.II, affix.varnas.first()), sutra),
            "7.3.93 adds ईट् to the हलादि pit ending after ब्रू.")
    }
}
