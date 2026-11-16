package dev.panini.ashtadhyayi.adhyaya8.pada4

import dev.panini.derivation.*
import dev.panini.dhatupatha.DhatuPatha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.toVarnas
import dev.panini.sutra.*

/*
 * 8.4.20–8.4.25: ṇatva of अन् at the end of a pada and in reduplication, of हन्, and after अन्तर्.
 * Roots are known by their Dhātupāṭha upadeśa (`sourceDhatuId`), never by spelling.
 */

private fun DerivationTerm.rootUpadesha(): String? = sourceDhatuId?.let { DhatuPatha.find(it)?.upadesha }
private fun DerivationState.hasRoot(upadesha: String) =
    (terms + droppedTerms).any { it.rootUpadesha() == upadesha }

/** 8.4.20 अन्तः — the न of अन् after an upasarga is ण even at the end of the pada (8.4.37 notwithstanding): हे प्राण्. */
object AntahSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.4.20", text = "अन्तः",
    hindiExplanation = "उपसर्गस्थ निमित्त से परे अन् धातु के पदान्त न् को भी ण् होता है।",
    type = SutraType.NITYA, chapter = 8, pada = 4, optional = false, kramaValue = 840020,
    role = SutraRole.Vidhi, action = SutraAction.ADESHA, scope = SutraScope.DERIVATION, stage = SutraStage.SANDHI,
), DerivationSutra {
    private fun target(state: DerivationState) =
        AnitehSutra.rootNasal(state)?.takeIf { AnitehSutra.isPadaFinal(state, it) }
    override fun matches(context: DerivationState) = target(context) != null
    override fun apply(context: DerivationState): DerivationChange {
        val (id, index) = requireNotNull(target(context))
        return DerivationChange(context.replaceTermVarna(id, index, listOf(Vyanjana.NNA), sutra),
            "8.4.20: the pada-final न of अन् becomes ण after the upasarga.")
    }
}

/** 8.4.21 उभौ साभ्यासस्य — with the abhyāsa, both न of अन् become ण: प्राणिणिषति, प्राणिणत्. */
object UbhauSabhyasasyaSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.4.21", text = "उभौ साभ्यासस्य",
    hindiExplanation = "अभ्याससहित अन् धातु के दोनों न् को ण् होता है।",
    type = SutraType.NITYA, chapter = 8, pada = 4, optional = false, kramaValue = 840021,
    role = SutraRole.Vidhi, action = SutraAction.ADESHA, scope = SutraScope.DERIVATION, stage = SutraStage.SANDHI,
    dependencies = setOf("8.4.19"),
), DerivationSutra {
    // After 8.4.19 the root's ण is followed by the abhyāsa's इ and न (अन् → अनिन् by 6.1.2): ण इ न.
    private fun target(state: DerivationState): Pair<String, Int>? {
        if ("8.4.19" !in state.appliedSutras || !state.hasRoot("अनँ")) return null
        return state.terms.firstNotNullOfOrNull { term ->
            (2..term.varnas.lastIndex).firstOrNull { i ->
                term.varnas[i] == Vyanjana.NA && term.varnas[i - 1] == Svara.I && term.varnas[i - 2] == Vyanjana.NNA
            }?.let { term.id to it }
        }
    }
    override fun matches(context: DerivationState) = target(context) != null
    override fun apply(context: DerivationState): DerivationChange {
        val (id, index) = requireNotNull(target(context))
        return DerivationChange(context.replaceTermVarna(id, index, listOf(Vyanjana.NNA), sutra),
            "8.4.21: the abhyāsa's न of अन् also becomes ण.")
    }
}

/** The न of हन् preceded by अ (ह अ न), after a र्/ष् of [prefix]: 8.4.22–8.4.24. */
internal data class HanNasal(val termId: String, val index: Int, val beforeVam: Boolean, val afterAntar: Boolean)

internal fun hanNasal(state: DerivationState): HanNasal? {
    if (!state.hasRoot("हनँ")) return null
    // Unmerged: a prefix term, then the verb or root term.
    (0 until state.terms.lastIndex).firstNotNullOfOrNull { i ->
        val prefix = state.terms[i]
        val verb = state.terms[i + 1]
        if (verb.rootUpadesha() != "हनँ") return@firstNotNullOfOrNull null
        val antar = prefix.upadesha == "अन्तर्"
        val nasal = (2..minOf(3, verb.varnas.lastIndex)).firstOrNull { at ->
            (at == 2 || verb.varnas[0] == Svara.A) && verb.varnas[at] == Vyanjana.NA &&
                verb.varnas[at - 1] == Svara.A && verb.varnas[at - 2] == Vyanjana.HA
        }
        UpasargadAsamasePiSutra.nasalAfterUpasarga(state, prefix, verb, nasal, requireUpasarga = !antar)
            ?.let { at -> classify(verb, at, antar, verb.varnas.getOrNull(at + 1) ?: state.terms.getOrNull(i + 2)?.varnas?.firstOrNull()) }
    }?.let { return it }
    // Merged by a vowel ekādeśa (प्र + अहन् → प्राहन्): the upasarga term carries the verb.
    val word = state.terms.firstOrNull { term -> state.samjnas.any { it.targetId == term.id && it.samjna == Samjna.UPASARGA } }
        ?: return null
    if (state.droppedTerms.none { it.rootUpadesha() == "हनँ" }) return null
    val prefixLength = word.upadesha.toVarnas().size
    val trigger = (0 until minOf(prefixLength, word.varnas.size)).lastOrNull {
        word.varnas[it] == Vyanjana.RA || word.varnas[it] == Vyanjana.SSA
    } ?: return null
    val nasal = (trigger + 3..word.varnas.lastIndex).firstOrNull {
        word.varnas[it] == Vyanjana.NA && word.varnas[it - 1] == Svara.A && word.varnas[it - 2] == Vyanjana.HA
    } ?: return null
    if (!word.varnas.subList(trigger + 1, nasal).all(UpasargadAsamasePiSutra::intervenes)) return null
    return classify(word, nasal, false, word.varnas.getOrNull(nasal + 1))
}


private fun classify(term: DerivationTerm, at: Int, antar: Boolean, next: dev.panini.shiksha.Varna?): HanNasal? {
    // Before a dental, 8.3.24 has already made the न an anusvāra (प्रहन्ति); a word-final न is padānta (8.4.37).
    if (next == null || next in setOf(Vyanjana.TA, Vyanjana.THA, Vyanjana.DA, Vyanjana.DHA, Vyanjana.NA)) return null
    return HanNasal(term.id, at, next == Vyanjana.VA || next == Vyanjana.MA, antar)
}

/** 8.4.22 हन्तेरत्पूर्वस्य — after an upasarga's र्/ष्, the न of हन् preceded by अ becomes ण: प्रहण्यते, प्रहणनम्; not प्रघ्नन्ति. */
object HanterAtpurvasyaSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.4.22", text = "हन्तेरत्पूर्वस्य",
    hindiExplanation = "उपसर्गस्थ निमित्त से परे अकारपूर्वक हन् के न् को ण् होता है।",
    type = SutraType.NITYA, chapter = 8, pada = 4, optional = false, kramaValue = 840022,
    role = SutraRole.Vidhi, action = SutraAction.ADESHA, scope = SutraScope.DERIVATION, stage = SutraStage.SANDHI,
), DerivationSutra {
    private fun target(state: DerivationState) = hanNasal(state)?.takeIf { !it.beforeVam && !it.afterAntar }
    override fun matches(context: DerivationState) = target(context) != null
    override fun apply(context: DerivationState): DerivationChange {
        val t = requireNotNull(target(context))
        return DerivationChange(context.replaceTermVarna(t.termId, t.index, listOf(Vyanjana.NNA), sutra),
            "8.4.22: the न of हन् after अ becomes ण.")
    }
}

/** 8.4.23 वमोर्वा — before व् or म् it is optional: प्रहण्वः/प्रहन्वः, प्रहण्मः/प्रहन्मः. */
object VamorVaSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.4.23", text = "वमोर्वा",
    hindiExplanation = "व् या म् परे होने पर हन् के न् को विकल्प से ण् होता है।",
    type = SutraType.VIBHASHA, chapter = 8, pada = 4, optional = true, kramaValue = 840023,
    role = SutraRole.Vidhi, action = SutraAction.ADESHA, scope = SutraScope.DERIVATION, stage = SutraStage.SANDHI,
), DerivationSutra {
    private fun target(state: DerivationState) = hanNasal(state)?.takeIf { it.beforeVam && !it.afterAntar }
    override val appliesByDefault = false
    override fun matches(context: DerivationState) = target(context) != null
    override fun apply(context: DerivationState) = applyAll(context).first()
    override fun applyAll(state: DerivationState): List<DerivationChange> {
        val t = requireNotNull(target(state))
        return listOf(DerivationChange(state.replaceTermVarna(t.termId, t.index, listOf(Vyanjana.NNA), sutra),
            "8.4.23: the न of हन् before व्/म् optionally becomes ण."),
            DerivationChange(state, "8.4.23 declined.", applied = false))
    }
}

/**
 * 8.4.24 अन्तरदेशे — after अन्तर्, the न of हन् is ण when no place is meant: अन्तर्हण्यते, अन्तर्हणनम्
 * (but अन्तर्हननो देशः). The derivation does not know whether a place is meant, so both are offered,
 * the unchanged form by default.
 */
object AntarAdesheSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.4.24", text = "अन्तरदेशे",
    hindiExplanation = "देश अर्थ न होने पर अन्तर् से परे हन् के न् को ण् होता है।",
    type = SutraType.NITYA, chapter = 8, pada = 4, optional = true, kramaValue = 840024,
    role = SutraRole.Vidhi, action = SutraAction.ADESHA, scope = SutraScope.DERIVATION, stage = SutraStage.SANDHI,
), DerivationSutra {
    private fun target(state: DerivationState) = hanNasal(state)?.takeIf { it.afterAntar }
    override val appliesByDefault = false
    override fun matches(context: DerivationState) = target(context) != null
    override fun apply(context: DerivationState) = applyAll(context).first()
    override fun applyAll(state: DerivationState): List<DerivationChange> {
        val t = requireNotNull(target(state))
        return listOf(DerivationChange(state.replaceTermVarna(t.termId, t.index, listOf(Vyanjana.NNA), sutra),
            "8.4.24: after अन्तर्, not of a place, the न of हन् becomes ण."),
            DerivationChange(state, "8.4.24 declined (a place is meant).", applied = false))
    }
}

/** 8.4.25 अयनं च — after अन्तर्, the न of अयन too, when no place is meant: अन्तरयणं शोभनम्. Offered as 8.4.24 is. */
object AyanamCaSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.4.25", text = "अयनं च",
    hindiExplanation = "देश अर्थ न होने पर अन्तर् से परे अयन के न् को ण् होता है।",
    type = SutraType.NITYA, chapter = 8, pada = 4, optional = true, kramaValue = 840025,
    role = SutraRole.Vidhi, action = SutraAction.ADESHA, scope = SutraScope.DERIVATION, stage = SutraStage.SANDHI,
), DerivationSutra {
    private val ayana = listOf(Svara.A, Vyanjana.YA, Svara.A, Vyanjana.NA)
    private fun target(state: DerivationState): Pair<String, Int>? = (0 until state.terms.lastIndex).firstNotNullOfOrNull { i ->
        val left = state.terms[i]
        val right = state.terms[i + 1]
        (right.id to 3).takeIf { left.upadesha == "अन्तर्" && right.varnas.take(4) == ayana }
    }
    override val appliesByDefault = false
    override fun matches(context: DerivationState) = target(context) != null
    override fun apply(context: DerivationState) = applyAll(context).first()
    override fun applyAll(state: DerivationState): List<DerivationChange> {
        val (id, index) = requireNotNull(target(state))
        return listOf(DerivationChange(state.replaceTermVarna(id, index, listOf(Vyanjana.NNA), sutra),
            "8.4.25: after अन्तर्, not of a place, the न of अयन becomes ण."),
            DerivationChange(state, "8.4.25 declined (a place is meant).", applied = false))
    }
}
