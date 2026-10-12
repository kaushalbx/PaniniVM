package dev.panini.ashtadhyayi.adhyaya8.pada4

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.pratyahara.Pratyahara
import dev.panini.shiksha.Varna
import dev.panini.shiksha.Vyanjana
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 8.4.56: vāvasāne.
 * At the end of a word (avasāna), a jhal sound is optionally replaced by a car sound (devoiced).
 * This makes the devoicing optional at the end of a sentence or pause.
 */
object VavasaneSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.4.56",
    text = "वावसाने",
    hindiExplanation = "अवसान (विराम) में झल् वर्णों के स्थान पर विकल्प से चर् आदेश होता है।",
    type = SutraType.VIBHASHA,
    chapter = 8,
    pada = 4,
    optional = true,
    kramaValue = 840056,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        // Condition: End of word (Avasāna)
        if (context.stage != DerivationStage.PADA_FORMED && context.stage != DerivationStage.FINAL) return false

        val lastTerm = context.terms.lastOrNull() ?: return false
        val finalConsonant = lastTerm.varnas.lastOrNull() ?: return false
        if (finalConsonant in alreadyDevoiced) return false

        val engine = Ashtadhyayi.pratyaharaEngine
        return engine.contains(Pratyahara.JHAL, finalConsonant)
    }

    override fun apply(context: DerivationState): DerivationChange {
        val lastTerm = context.terms.last()
        val finalConsonant = lastTerm.varnas.last()
        val substitute = devoiced(finalConsonant)

        return DerivationChange(
            state = context.replaceTermVarna(lastTerm.id, lastTerm.varnas.lastIndex, listOf(substitute), sutra)
                .copy(stage = DerivationStage.FINAL),
            explanation = "8.4.56: Optionally devoiced ${finalConsonant.devanagari} to ${substitute.devanagari} at avasāna."
        )
    }

    override fun applyAll(state: DerivationState): List<DerivationChange> = listOf(
        apply(state),
        DerivationChange(state, "8.4.56: Declined optional devoicing at avasāna.", applied = false)
    )

    private fun devoiced(source: Varna): Vyanjana = when (source) {
        Vyanjana.JA, Vyanjana.JHA -> Vyanjana.CA
        Vyanjana.DDA, Vyanjana.DDHA -> Vyanjana.TTA
        Vyanjana.DA, Vyanjana.DHA -> Vyanjana.TA
        Vyanjana.GA, Vyanjana.GHA -> Vyanjana.KA
        Vyanjana.BA, Vyanjana.BHA -> Vyanjana.PA
        else -> Vyanjana.TA
    }

    private val alreadyDevoiced: Set<Varna> = setOf(
        Vyanjana.CA, Vyanjana.TTA, Vyanjana.TA, Vyanjana.KA, Vyanjana.PA,
        Vyanjana.SHA, Vyanjana.SSA, Vyanjana.SA, Vyanjana.HA,
    )
}
