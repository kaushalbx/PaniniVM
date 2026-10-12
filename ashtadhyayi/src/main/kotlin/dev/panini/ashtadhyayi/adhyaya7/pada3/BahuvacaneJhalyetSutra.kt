package dev.panini.ashtadhyayi.adhyaya7.pada3

import dev.panini.core.Vacana
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.HasMorphosyntax
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Varna
import dev.panini.shiksha.Vyanjana
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** 7.3.103: substitutes ए for final अ before plural झल-initial sup affix. */
object BahuvacaneJhalyetSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.3.103",
    text = "बहुवचने झल्येत्",
    hindiExplanation = "झलादि बहुवचन सुप् के परे अकारान्त अङ्ग का अकार एकार होता है।",
    type = SutraType.NITYA,
    chapter = 7,
    pada = 3,
    optional = false,
    kramaValue = 730103,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.stage == DerivationStage.INITIAL || context.stage == DerivationStage.PRATYAYA_SELECTED) return false
        if (context.terms.size < 2) return false
        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()

        // 7.3.103 applies to a-final aṅgas; feminine ā-stems retain their ā.
        val isAEnding = stem.varnas.lastOrNull() == Svara.A
        val firstVarna = affix.varnas.firstOrNull() ?: return false

        val isPlural = HasMorphosyntax(vacana = Vacana.BAHUVACANA).matches(context)

        return affix.sourceSupAffix != null &&
            affix.upadesha !in setOf("शि", "शस्") &&
            isAEnding && isPlural && isJhal(firstVarna) &&
                context.samjnas.any { it.targetId == affix.id && it.samjna == Samjna.PRATYAYA }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val terms = context.terms
        val stem = terms[terms.size - 2]
        val transformed = context.replaceTermVarna(stem.id, stem.varnas.lastIndex, listOf(Svara.E), sutra)

        val affix = terms.last()
        val changedState = if (affix.upadesha == "भ्यस्") {
            transformed.concatenateFollowingTerm(stem.id, affix.id, sutra)
                .copy(stage = DerivationStage.PADA_FORMED)
        } else {
            transformed.copy(stage = DerivationStage.ANGAKARYA)
        }

        return DerivationChange(
            state = changedState,
            explanation = "7.3.103: Substituted 'e' for final 'a' before plural jhal-initial sup."
        )
    }

    private fun isJhal(varna: Varna): Boolean = varna in setOf(
        Vyanjana.JHA, Vyanjana.BHA, Vyanjana.GHA, Vyanjana.DDHA, Vyanjana.DHA,
        Vyanjana.JA, Vyanjana.BA, Vyanjana.GA, Vyanjana.DDA, Vyanjana.DA,
        Vyanjana.KHA, Vyanjana.PHA, Vyanjana.CHA, Vyanjana.TTHA, Vyanjana.THA,
        Vyanjana.CA, Vyanjana.TTA, Vyanjana.TA, Vyanjana.KA, Vyanjana.PA,
        Vyanjana.SHA, Vyanjana.SSA, Vyanjana.SA, Vyanjana.HA,
    )
}
