package dev.sanskrit.ashtadhyayi.adhyaya3.pada1

import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.derivation.DerivationTerm
import dev.sanskrit.derivation.TermKind
import dev.sanskrit.derivation.TingAffix
import dev.sanskrit.derivation.Lakara
import dev.sanskrit.dhatupatha.Gana
import dev.sanskrit.dhatupatha.PadaType
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

/** 3.1.81: क्र्यादिभ्यः श्ना. The nā/nī vikaraṇa follows Kryādi roots. */
object KryadibhyahShnaSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.1.81",
    text = "क्र्यादिभ्यः श्ना",
    hindiExplanation = "क्र्यादि-गण के धातुओं से परे श्ना विकरण होता है।",
    type = SutraType.APAVADA,
    chapter = 3,
    pada = 1,
    optional = false,
    kramaValue = 310081,
    role = SutraRole.Apavada,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION,
    blocks = setOf("3.1.68", "7.3.84"),
), DerivationSutra {
    private val latStrongAffixes = setOf(TingAffix.TIP, TingAffix.SIP, TingAffix.MIP)
    private val lotStrongAffixes = setOf(
        TingAffix.TIP,
        TingAffix.MIP,
        TingAffix.VAS,
        TingAffix.MAS,
        TingAffix.VAHI,
        TingAffix.MAHING,
    )
    private val vowelInitialAffixes = setOf(
        TingAffix.JHI,
        TingAffix.ATAM,
        TingAffix.JHA,
        TingAffix.ATHAM,
        TingAffix.IT,
    )

    override fun matches(context: DerivationState): Boolean {
        val dhatu = context.terms.firstOrNull { it.kind == TermKind.DHATU } ?: return false
        return dhatu.gana == Gana.KRYADI &&
            context.terms.lastOrNull()?.upadesha in TingAffix.entries.map { it.upadesha } &&
            context.allEffectiveTerms.none { it.upadesha == "श्ना" }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val affix = TingAffix.entries.single { it.upadesha == context.terms.last().upadesha }
        val strongAffixes = when (context.effectiveContext.rupa.lakara) {
            Lakara.LOT -> lotStrongAffixes
            Lakara.LING -> emptySet()
            else -> latStrongAffixes
        }
        val surface = if (
            context.effectiveContext.rupa.lakara == Lakara.LING &&
            affix.pada == PadaType.PARASMAIPADA
        ) {
            "नी"
        } else if (context.effectiveContext.rupa.lakara == Lakara.LING) {
            "न्"
        } else {
            when (affix) {
                in strongAffixes -> "ना"
                in vowelInitialAffixes -> "न्"
                else -> "नी"
            }
        }
        val shna = DerivationTerm("shna", surface, TermKind.PRATYAYA, upadesha = "श्ना")
        return DerivationChange(
            context.insertBeforeTingOrLingAugment(shna),
            "3.1.81 introduces the $surface allomorph of श्ना after a Kryādi root.",
        )
    }
}
