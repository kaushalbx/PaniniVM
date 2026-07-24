package dev.panini.ashtadhyayi.adhyaya2.pada3

import dev.panini.core.Karaka
import dev.panini.core.Vibhakti
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraInput
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType
import dev.panini.vyakaranam.analysis.KarakaEvidence
import dev.panini.vyakaranam.analysis.SemanticRelation
import dev.panini.vyakaranam.analysis.VibhaktiRuleContext
import dev.panini.vyakaranam.analysis.VibhaktiRuleResult

/**
 * Sūtra 2.3.16 नमःस्वस्तिस्वाहास्वधालंवषड्योगाच्च.
 * Assigns Caturthī in connection with namaḥ, svasti, svāhā, svadhā, alam, vaṣaṭ.
 */
object NamahSvastiSvahaSutra : Sutra<VibhaktiRuleContext, VibhaktiRuleResult>(
    number = "2.3.16", text = "नमःस्वस्तिस्वाहास्वधालंवषड्योगाच्च",
    hindiExplanation = "नमः स्वस्ति स्वाहा स्वधा अलम् वषट् इत्येतैर्योगे चतुर्थी स्यात्।",
    type = SutraType.NITYA, chapter = 2, pada = 3, optional = false, kramaValue = 230016,
    role = SutraRole.Vidhi, action = SutraAction.VIDHI, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.KARAKA, SutraInput.SEMANTIC_FEATURE),
    adhikara = setOf("2.3.1"),
) {
    override fun matches(context: VibhaktiRuleContext): Boolean {
        val hasConflictingRelation = context.participant?.semanticRelations.orEmpty().any {
            it in setOf(SemanticRelation.ACCOMPANIMENT, SemanticRelation.BODY_DEFORMITY, SemanticRelation.CHARACTERISTIC_MARK)
        }
        return !context.abhihita &&
            !hasConflictingRelation &&
            (context.karaka == Karaka.SAMPRADANA || context.karaka == Karaka.ANIRDHARITA) &&
            Vibhakti.CHATURTHI in context.morphologicalCandidates
    }

    override fun apply(context: VibhaktiRuleContext) = VibhaktiRuleResult.Assigned(
        Vibhakti.CHATURTHI,
        KarakaEvidence(number, text, "चतुर्थी realizes salutation/offering relation with namaḥ/svāhā (2.3.16)."),
    )
}
