package dev.panini.ashtadhyayi.adhyaya7.pada1

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.TermKind
import dev.panini.derivation.ItProcessingPhase
import dev.panini.derivation.GrammaticalAccent
import dev.panini.derivation.AccentType
import dev.panini.derivation.hasSarvanamasthanaSupIdentity
import dev.panini.derivation.HasMorphosyntax
import dev.panini.core.Linga
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Varna
import dev.panini.shiksha.Vyanjana
import dev.panini.sutra.NimittaScope
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 7.1.98: catur-anaduhor ām.
 * Adds the augment 'ām' (ā) after the last vowel of 'catur' and 'anaḍuh' before a sarvanāmasthāna affix.
 */
object CaturanuduhorAmSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.1.98",
    text = "चतुरनडुहोरामुदात्तः",
    hindiExplanation = "सर्वनामस्थाने विभक्तौ परे चतुर् और अनडुह् अङ्गों को आम् (आ) आगम होता है।",
    type = SutraType.NITYA,
    chapter = 7,
    pada = 1,
    optional = false,
    kramaValue = 710098,
    role = SutraRole.Apavada,
    action = SutraAction.AGAMA,
    scope = SutraScope.DERIVATION,
    nimittaScope = NimittaScope.BOTH,
    dependencies = setOf("6.4.1", "1.1.47")
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if ("6.4.1" !in context.activeAdhikaras) return false
        if (context.terms.size < 2) return false

        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()

        // A later feminine substitution retains the lexical upadeśa, but it
        // is no longer the चतुर् aṅga to which this augment applies.
        if (stem.kind != TermKind.PRATIPADIKA || stem.varnas !in eligibleStems) return false

        // Sarvanāmasthāna affixes for catur: jas (7.1.20 shi in neuter!), su, au, etc.
        val isSarvanamasthana = context.samjnas.any {
            it.targetId == affix.id && it.samjna == Samjna.SARVANAMASTHANA
        } || (affix.kind == TermKind.PRATYAYA && affix.upadesha == "शि") ||
            (affix.hasSarvanamasthanaSupIdentity() &&
            !HasMorphosyntax(linga = Linga.NAPUMSAKA).matches(context))

        return isSarvanamasthana && context.allEffectiveTerms.none {
            it.createdBySutra == sutra && it.augmentTargetId == stem.id
        }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val stem = context.terms[context.terms.size - 2]
        require(matches(context))
        val augment = DerivationTerm("am-augment:${stem.id}", "आम्", TermKind.AGAMA,
            upadesha = "आम्", createdBySutra = sutra, augmentTargetId = stem.id,
            itProcessingPhase = ItProcessingPhase.RAW_UPADESHA,
            grammaticalAccents = listOf(GrammaticalAccent(0, AccentType.UDATTA, sutra)))

        return DerivationChange(
            state = context.addTerm(augment),
            explanation = "7.1.98 introduces raw m-it ām with udātta for independent placement."
        )
    }

    private val catur: List<Varna> = listOf(Vyanjana.CA, Svara.A, Vyanjana.TA, Svara.U, Vyanjana.RA)
    private val anaduh: List<Varna> = listOf(Svara.A, Vyanjana.NA, Svara.A, Vyanjana.DDA, Svara.U, Vyanjana.HA)
    private val eligibleStems = setOf(catur, anaduh)
}
