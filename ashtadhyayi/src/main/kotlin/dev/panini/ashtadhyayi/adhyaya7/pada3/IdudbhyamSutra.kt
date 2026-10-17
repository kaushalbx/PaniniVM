package dev.panini.ashtadhyayi.adhyaya7.pada3

import dev.panini.core.SupAffix
import dev.panini.derivation.*
import dev.panini.shiksha.*
import dev.panini.sutra.*

/** 7.3.117 retains the ām substitution specifically after short i/u nadī. */
object IdudbhyamSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.3.117", text = "इदुद्भ्याम्",
    hindiExplanation = "नदीसंज्ञक ह्रस्व इ/उकारान्त के बाद ङि को आम् आदेश होता है।",
    type = SutraType.APAVADA, chapter = 7, pada = 3, optional = false, kramaValue = 730117,
    role = SutraRole.Apavada, action = SutraAction.ADESHA, scope = SutraScope.DERIVATION,
    stage = SutraStage.ANGAKARYA, priority = SutraPriority.APAVADA, blocks = setOf("7.3.116", "7.3.118"),
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if ("6.4.1" !in context.activeAdhikaras || context.terms.size < 2) return false
        val stem = context.terms[context.terms.lastIndex - 1]
        val suffix = context.terms.last()
        return stem.varnas.lastOrNull() in setOf(Svara.I, Svara.U) &&
            context.samjnas.any { it.targetId == stem.id && it.samjna == Samjna.NADI } &&
            suffix.sourceSupAffix == SupAffix.NGI && suffix.hasCurrentAffix(SupAffix.NGI) &&
            suffix.itProcessingPhase == ItProcessingPhase.PROCESSED && sutra !in suffix.establishedBySutras
    }

    override fun apply(context: DerivationState): DerivationChange {
        val suffix = context.terms.last()
        val replaced = context.replaceWholeAffix(suffix.id, listOf(Svara.AA, Vyanjana.MA), sutra,
            WholeAffixDesignationPolicy.Consume)
        val current = replaced.terms.single { it.id == suffix.id }
        return DerivationChange(replaced.replaceTerm(suffix.id,
            current.copy(establishedBySutras = current.establishedBySutras + sutra)).copy(stage = DerivationStage.ANGAKARYA),
            "7.3.117 substitutes ām for ṅi after short i/u with the chosen nadī designation.")
    }
}
