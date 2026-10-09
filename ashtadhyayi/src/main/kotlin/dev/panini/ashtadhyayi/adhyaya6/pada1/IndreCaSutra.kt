package dev.panini.ashtadhyayi.adhyaya6.pada1

import dev.panini.derivation.*
import dev.panini.shiksha.*
import dev.panini.sutra.*

/** 6.1.124: compulsory avaṅ for pada-final go before Indra. */
object IndreCaSutra : Sutra<DerivationState, DerivationChange>(
    number = "6.1.124", text = "इन्द्रे च",
    hindiExplanation = "इन्द्र शब्द परे पदान्त गो के ओकार के स्थान पर नित्य अवङ् आदेश होता है।",
    type = SutraType.NITYA, chapter = 6, pada = 1, optional = false,
    kramaValue = 610124, role = SutraRole.Apavada, action = SutraAction.ADESHA,
    scope = SutraScope.VARNA, stage = SutraStage.SANDHI,
    priority = SutraPriority.APAVADA, blocks = setOf("6.1.78", "6.1.123"),
), DerivationSutra {
    private val go: List<Varna> = listOf(Vyanjana.GA, Svara.O)
    private val indra: List<Varna> = listOf(Svara.I, Vyanjana.NA, Vyanjana.DA, Vyanjana.RA, Svara.A)

    private fun boundary(context: DerivationState): Int? =
        (0 until context.terms.lastIndex).firstOrNull { index ->
            val left = context.terms[index]
            val right = context.terms[index + 1]
            left.varnas == go &&
                context.samjnas.any { it.targetId == left.id && it.samjna == Samjna.PADA } &&
                (right.upadeshaVarnas == indra || right.varnas == indra ||
                    right.varnas == indra + Ayogavaha.VISARGA ||
                    right.varnas == indra + Vyanjana.MA ||
                    right.varnas == indra + Ayogavaha.ANUSVARA)
        }

    override fun matches(context: DerivationState): Boolean = boundary(context) != null

    override fun apply(context: DerivationState): DerivationChange {
        return applyAvang(context, requireNotNull(boundary(context)), sutra)
    }
}
