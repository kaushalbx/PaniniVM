package dev.panini.ashtadhyayi.adhyaya7.pada1

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.shiksha.Samjna
import dev.panini.sutra.NimittaScope
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 7.1.22: ṣaḍbhyo luk.
 * The nominative and accusative plural affixes 'jas' and 'śas' are elided by 'luk' after stems designated as 'ṣaṭ'.
 */
object SadbhyoLukSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.1.22",
    text = "षड्भ्यो लुक्",
    hindiExplanation = "षट् संज्ञक शब्दों के बाद प्रथमा और द्वितीया बहुवचन के प्रत्ययों (जस् और शस्) का लुक् होता है।",
    type = SutraType.NITYA,
    chapter = 7,
    pada = 1,
    optional = false,
    kramaValue = 710022,
    role = SutraRole.Apavada,
    action = SutraAction.LOPA,
    scope = SutraScope.DERIVATION,
    nimittaScope = NimittaScope.BOTH,
    dependencies = setOf("6.4.1", "1.1.24")
), DerivationSutra {
    private val SHAT_NUMERALS = setOf("पञ्चन्", "षष्", "षट्", "सप्तन्", "अष्टन्", "नवन्", "दशन्")

    override fun matches(context: DerivationState): Boolean {
        if ("6.4.1" !in context.activeAdhikaras) return false
        if (context.terms.size < 2) return false

        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()

        val isShat = context.samjnas.any { it.targetId == stem.id && it.samjna == Samjna.SHAT } ||
            stem.upadesha in SHAT_NUMERALS || stem.surface in SHAT_NUMERALS

        if (!isShat) return false

        val isJasOrSas = affix.id in setOf("sup-jas", "sup-sas") || affix.upadesha in setOf("जस्", "शस्")
        return isJasOrSas
    }

    override fun apply(context: DerivationState): DerivationChange {
        val affix = context.terms.last()
        val newTerms = context.terms.dropLast(1)

        return DerivationChange(
            state = context.copy(
                terms = newTerms,
                droppedTerms = context.droppedTerms + dev.panini.derivation.consumeAffixForDrop(affix, sutra),
                // 1.1.62 preserves the grammatical effect of the deleted
                // sup, so pada formation and subsequent Tripādī rules remain.
                stage = DerivationStage.ANGAKARYA,
            ),
            explanation = "7.1.22: Elided '${affix.surface}' (jas/śas) after ṣaṭ-designated stem."
        )
    }
}
