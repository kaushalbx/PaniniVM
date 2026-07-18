package dev.panini.ashtadhyayi.adhyaya4.pada1

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.SupAffix
import dev.panini.derivation.TermKind
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** 4.1.2: first implemented slot of the sup inventory, सु for first-singular. */
object SvaujasamauTchastaSutra : Sutra<DerivationState, DerivationChange>(
    number = "4.1.2",
    text = "स्वौजसमौट्छष्टाभ्याम्भिस्ङेभ्याम्भ्यस्ङसिभ्याम्भ्यस्ङसोसाङ्ङ्योस्सुप्",
    hindiExplanation = "प्रातिपदिक के बाद विभक्ति और वचन के अनुसार सुप् प्रत्यय होते हैं।",
    type = SutraType.NITYA,
    chapter = 4,
    pada = 1,
    optional = false,
    kramaValue = 410002,
    role = SutraRole.Vidhi,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.PRATYAYA,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        context.stage == DerivationStage.INITIAL &&
                context.terms.any { it.kind == TermKind.PRATIPADIKA } &&
                selectionFor(context) != null

    override fun apply(context: DerivationState): DerivationChange {
        val affix =
            requireNotNull(selectionFor(context)) { "4.1.2 applied without a supported sup slot." }
        return DerivationChange(
            state = context.addTerm(affix.term()).copy(stage = DerivationStage.PRATYAYA_SELECTED),
            explanation = "4.1.2 selects ${affix.upadesha} for the requested case and number.",
        )
    }

    private fun selectionFor(context: DerivationState): SupAffix? =
        context.effectiveContext.rupa.let { morphology ->
            if (morphology.vibhakti != null && morphology.vacana != null) {
                SupAffix.select(morphology.vibhakti, morphology.vacana)
            } else null
        }
}
