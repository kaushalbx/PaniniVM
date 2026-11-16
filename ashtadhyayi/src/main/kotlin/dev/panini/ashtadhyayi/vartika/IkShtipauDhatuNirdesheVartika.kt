package dev.panini.ashtadhyayi.vartika

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.ItProcessingPhase
import dev.panini.derivation.SanadiAffixes
import dev.panini.derivation.TermKind
import dev.panini.shiksha.Samjna
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType
import dev.panini.sutra.runtime.SutraSource

/**
 * Vārttika under 3.3.108 इक्श्तिपौ धातुनिर्देशे: a dhātu is named with इक् (मृज् → मृजि, वच् → वचि).
 * The कित् affix keeps the root free of guṇa and vṛddhi (1.1.5). Only the इक् branch is given here,
 * and only when a dhātu's name is asked for.
 */
object IkShtipauDhatuNirdesheVartika : Sutra<DerivationState, DerivationChange>(
    number = "vartika:3.3.108:ik-shtipau", text = "इक्श्तिपौ धातुनिर्देशे",
    hindiExplanation = "धातु का निर्देश (नाम) करने के लिए धातु से 'इक्' (इ) और 'श्तिप्' (ति) प्रत्यय होते हैं।",
    type = SutraType.NITYA, chapter = 3, pada = 3, optional = false, kramaValue = 330108,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override val source = SutraSource.Vartika("3.3.108", text, "https://ashtadhyayi.com/sutraani/3/3/108")

    override fun matches(context: DerivationState): Boolean =
        (context.stage == DerivationStage.INITIAL || context.stage == DerivationStage.PRATYAYA_SELECTED) &&
            context.samjnas.any { it.samjna == Samjna.IK_PRATYAYA } &&
            context.terms.any { it.kind == TermKind.DHATU } &&
            context.terms.none { it.kind == TermKind.PRATYAYA && !SanadiAffixes.contains(it.upadesha) }

    override fun apply(context: DerivationState): DerivationChange {
        val ik = DerivationTerm("ik", "इक्", TermKind.PRATYAYA, upadesha = "इक्", createdBySutra = number,
            itProcessingPhase = ItProcessingPhase.RAW_UPADESHA)
        return DerivationChange(context.addTerm(ik), "$number names the dhātu with इक्.")
    }
}
