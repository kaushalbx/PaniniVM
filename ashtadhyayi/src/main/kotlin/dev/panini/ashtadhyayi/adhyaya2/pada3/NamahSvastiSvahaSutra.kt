package dev.panini.ashtadhyayi.adhyaya2.pada3

import dev.panini.core.Karaka
import dev.panini.core.Vibhakti
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraInput
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType
import dev.panini.analysis.KarakaEvidence
import dev.panini.analysis.SemanticRelation
import dev.panini.analysis.VibhaktiRuleContext
import dev.panini.analysis.VibhaktiRuleResult
import dev.panini.vyakaranam.ast.MulaPratipadika

/**
 * Sūtra 2.3.16 नमःस्वस्तिस्वाहास्वधालंवषड्योगाच्च.
 * Assigns Caturthī in connection with namaḥ, svasti, svāhā, svadhā, alam, vaṣaṭ.
 */
object NamahSvastiSvahaSutra : Sutra<VibhaktiRuleContext, VibhaktiRuleResult>(
    number = "2.3.16", text = "नमःस्वस्तिस्वाहास्वधालंवषड्योगाच्च",
    hindiExplanation = "नमः स्वस्ति स्वाहा स्वधा अलम् वषट् इत्येतैर्योगे चतुर्थी स्यात्।",
    type = SutraType.NITYA, chapter = 2, pada = 3, optional = false, kramaValue = 230016,
    role = SutraRole.Vidhi, action = SutraAction.VIDHI, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.KARAKA, SutraInput.SEMANTIC_FEATURE, SutraInput.PRATIPADIKA),
    adhikara = setOf("2.3.1"),
) {
    fun hasConstruction(context: VibhaktiRuleContext): Boolean {
        val p = context.participant ?: return false
        val avyaya = p.governingAvyaya?.takeIf { it.derivation == null }
        if (avyaya?.form in setOf("नमः", "नमस्", "स्वस्ति", "स्वाहा", "स्वधा", "वषट्")) return true
        val sufficiency = SemanticRelation.SUFFICIENCY in p.semanticRelations
        if (avyaya?.form in setOf("अलम्", "अलं")) return sufficiency
        val stem = (p.governingNominal?.pratipadika as? MulaPratipadika)
            ?.takeIf { it.vikaras.isEmpty() }?.text
        return stem in setOf("प्रभु", "शक्त") && sufficiency
    }

    override fun matches(context: VibhaktiRuleContext): Boolean {
        return !context.abhihita &&
            hasConstruction(context) &&
            (context.karaka == Karaka.SAMPRADANA || context.karaka == Karaka.ANIRDHARITA) &&
            Vibhakti.CHATURTHI in context.morphologicalCandidates
    }

    override fun apply(context: VibhaktiRuleContext) = VibhaktiRuleResult.Assigned(
        Vibhakti.CHATURTHI,
        KarakaEvidence(number, text, "चतुर्थी under the linked 2.3.16 governor " +
            "${context.participant?.governingAvyaya?.form ?: context.participant?.governingNominal?.sourceText}."),
    )
}
