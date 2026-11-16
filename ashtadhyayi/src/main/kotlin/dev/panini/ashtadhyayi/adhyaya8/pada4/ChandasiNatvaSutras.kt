package dev.panini.ashtadhyayi.adhyaya8.pada4

import dev.panini.derivation.*
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.sutra.*

/*
 * 8.4.26–8.4.28: Vedic ṇatva across a boundary. They apply only where the derivation is marked छन्दस्
 * (`DerivationalEnvironment.CHANDAS`); classical input never reaches them.
 */

private val DerivationState.isChandas get() = context.has(DerivationalEnvironment.CHANDAS)

/** The first न of [right] reached from the end of [left] through 8.4.2 intervenors only. */
private fun nasalAcross(leftTail: List<dev.panini.shiksha.Varna>, right: DerivationTerm): Int? {
    val at = right.varnas.indexOfFirst { it == Vyanjana.NA }.takeIf { it >= 0 } ?: return null
    return at.takeIf { (leftTail + right.varnas.take(at)).all(UpasargadAsamasePiSutra::intervenes) }
}

/** The sounds after the last र्/ष् of [term], or null when it has none. */
private fun tailAfterTrigger(term: DerivationTerm): List<dev.panini.shiksha.Varna>? {
    val trigger = term.varnas.indexOfLast { it == Vyanjana.RA || it == Vyanjana.SSA }
    return if (trigger < 0) null else term.varnas.drop(trigger + 1)
}

/** 8.4.26 छन्दस्यृदवग्रहात् — in the Veda, after a pūrvapada ending in ऋ (where the avagraha falls), न becomes ण: नृमणाः, पितृयाणम्. */
object ChandasyRdavagrahatSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.4.26", text = "छन्दस्यृदवग्रहात्",
    hindiExplanation = "वेद में ऋकारान्त अवग्रह (पूर्वपद) से परे न् को ण् होता है।",
    type = SutraType.NITYA, chapter = 8, pada = 4, optional = false, kramaValue = 840026,
    role = SutraRole.Vidhi, action = SutraAction.ADESHA, scope = SutraScope.DERIVATION, stage = SutraStage.SANDHI,
), DerivationSutra {
    private fun target(state: DerivationState): Pair<String, Int>? {
        if (!state.isChandas || !state.context.has(DerivationalEnvironment.SAMASA_PURVAPADA)) return null
        return (0 until state.terms.lastIndex).firstNotNullOfOrNull { i ->
            val left = state.terms[i]
            val right = state.terms[i + 1]
            if (left.varnas.lastOrNull() !in setOf(Svara.R, Svara.RR)) return@firstNotNullOfOrNull null
            nasalAcross(emptyList(), right)?.let { right.id to it }
        }
    }
    override fun matches(context: DerivationState) = target(context) != null
    override fun apply(context: DerivationState): DerivationChange {
        val (id, index) = requireNotNull(target(context))
        return DerivationChange(context.replaceTermVarna(id, index, listOf(Vyanjana.NNA), sutra),
            "8.4.26: in the Veda, न after an ऋ-final pūrvapada becomes ण.")
    }
}

/** नस् (8.1.21, the enclitic of अस्मद्), known by its upadeśa. */
private fun DerivationTerm.isNas() = upadesha == "नस्" && varnas.firstOrNull() == Vyanjana.NA

/** 8.4.27 नश्च धातुस्थोरुषुभ्यः — in the Veda, नस् after a र्/ष् in a root, after उरु, or after षु becomes णस्: अग्ने रक्षा णः, उरु णस्कृधि, अभी षु णः. */
object NashcaDhatusthoruShubhyahSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.4.27", text = "नश्च धातुस्थोरुषुभ्यः",
    hindiExplanation = "वेद में धातुस्थ र्/ष्, उरु और षु से परे नस् के न् को ण् होता है।",
    type = SutraType.NITYA, chapter = 8, pada = 4, optional = false, kramaValue = 840027,
    role = SutraRole.Vidhi, action = SutraAction.ADESHA, scope = SutraScope.DERIVATION, stage = SutraStage.SANDHI,
), DerivationSutra {
    private fun target(state: DerivationState): String? {
        if (!state.isChandas) return null
        return (0 until state.terms.lastIndex).firstNotNullOfOrNull { i ->
            val left = state.terms[i]
            val right = state.terms[i + 1]
            if (!right.isNas()) return@firstNotNullOfOrNull null
            val named = left.surface == "उरु" || left.surface == "षु"
            val inRoot = left.sourceDhatuId != null && tailAfterTrigger(left)?.all(UpasargadAsamasePiSutra::intervenes) == true
            right.id.takeIf { named || inRoot }
        }
    }
    override fun matches(context: DerivationState) = target(context) != null
    override fun apply(context: DerivationState): DerivationChange =
        DerivationChange(context.replaceTermVarna(requireNotNull(target(context)), 0, listOf(Vyanjana.NNA), sutra),
            "8.4.27: in the Veda, नस् becomes णस्.")
}

/** 8.4.28 उपसर्गाद्बहुलम् — in the Veda, नस् after an upasarga's र्/ष् variously becomes णस्: प्र णो मुञ्चतम्. */
object UpasargadBahulamSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.4.28", text = "उपसर्गाद्बहुलम्",
    hindiExplanation = "वेद में उपसर्ग से परे नस् के न् को बहुल करके ण् होता है।",
    type = SutraType.VIBHASHA, chapter = 8, pada = 4, optional = true, kramaValue = 840028,
    role = SutraRole.Vidhi, action = SutraAction.ADESHA, scope = SutraScope.DERIVATION, stage = SutraStage.SANDHI,
), DerivationSutra {
    private fun target(state: DerivationState): String? {
        if (!state.isChandas) return null
        return (0 until state.terms.lastIndex).firstNotNullOfOrNull { i ->
            val left = state.terms[i]
            val right = state.terms[i + 1]
            val isUpasarga = state.samjnas.any { it.targetId == left.id && it.samjna == Samjna.UPASARGA }
            right.id.takeIf { isUpasarga && right.isNas() &&
                tailAfterTrigger(left)?.all(UpasargadAsamasePiSutra::intervenes) == true }
        }
    }
    override val appliesByDefault = false
    override fun matches(context: DerivationState) = target(context) != null
    override fun apply(context: DerivationState) = applyAll(context).first()
    override fun applyAll(state: DerivationState): List<DerivationChange> = listOf(
        DerivationChange(state.replaceTermVarna(requireNotNull(target(state)), 0, listOf(Vyanjana.NNA), sutra),
            "8.4.28: in the Veda, नस् after an upasarga becomes णस्."),
        DerivationChange(state, "8.4.28 declined.", applied = false))
}
