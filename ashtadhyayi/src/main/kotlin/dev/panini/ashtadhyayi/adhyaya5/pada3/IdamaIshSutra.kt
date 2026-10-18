package dev.panini.ashtadhyayi.adhyaya5.pada3

import dev.panini.core.TaddhitaAffix
import dev.panini.derivation.AdeshaPlacement
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.TermKind
import dev.panini.derivation.introduceRawAdesha
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Varna
import dev.panini.shiksha.Vyanjana
import dev.panini.sutra.NimittaScope
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** 5.3.3 इदम इश्: whole-stem śit substitute before a prāgdiśīya affix. */
object IdamaIshSutra : Sutra<DerivationState, DerivationChange>(
    number = "5.3.3", text = "इदम इश्",
    hindiExplanation = "प्राग्दिशीय प्रत्यय के परे इदम् के स्थान पर शित् इश् आदेश होता है।",
    type = SutraType.APAVADA, chapter = 5, pada = 3, optional = false, kramaValue = 530003,
    role = SutraRole.Apavada, action = SutraAction.ADESHA, scope = SutraScope.DERIVATION,
    nimittaScope = NimittaScope.BOTH, dependencies = setOf("5.3.1", "5.3.2", "1.1.55"),
    blocks = setOf("7.2.102"),
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.terms.any { it.id == carrierId } || context.droppedTerms.any { it.id == carrierId }) return false
        val stem = context.terms.singleOrNull { it.kind == TermKind.PRATIPADIKA } ?: return false
        val affix = context.terms.lastOrNull { it.kind == TermKind.PRATYAYA } ?: return false
        return stem.upadesha == "इदम्" && stem.varnas == idam &&
            affix.sourceTaddhitaAffix in setOf(TaddhitaAffix.TASIL, TaddhitaAffix.TRAL)
    }

    override fun apply(context: DerivationState): DerivationChange {
        val stem = context.terms.single { it.kind == TermKind.PRATIPADIKA }
        return DerivationChange(
            context.introduceRawAdesha(stem.id, ish, AdeshaPlacement.SHIT_WHOLE, sutra, carrierId),
            "5.3.3 introduces raw इश्; its श् must undergo इत् designation before 1.1.55 targets the whole stem.",
        )
    }

    private const val carrierId = "adesha-5-3-3-ish"
    private val idam: List<Varna> = listOf(Svara.I, Vyanjana.DA, Svara.A, Vyanjana.MA)
    private val ish: List<Varna> = listOf(Svara.I, Vyanjana.SHA)
}
