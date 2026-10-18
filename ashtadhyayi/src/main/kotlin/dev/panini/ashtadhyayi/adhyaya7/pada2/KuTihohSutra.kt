package dev.panini.ashtadhyayi.adhyaya7.pada2

import dev.panini.core.TaddhitaAffix
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.TermKind
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.sutra.NimittaScope
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** 7.2.104 कु तिहोः: substitutes कु for किम् before a t- or h-initial vibhakti. */
object KuTihohSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.2.104", text = "कु तिहोः",
    hindiExplanation = "तकारादि अथवा हकारादि विभक्ति के परे किम् के स्थान पर कु आदेश होता है।",
    type = SutraType.APAVADA, chapter = 7, pada = 2, optional = false, kramaValue = 720104,
    role = SutraRole.Apavada, action = SutraAction.ADESHA, scope = SutraScope.DERIVATION,
    nimittaScope = NimittaScope.BOTH,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.terms.size < 2 || context.substitutions.any { it.sutra == number }) return false
        val stem = context.terms[context.terms.lastIndex - 1]
        val affix = context.terms.last()
        val isPragdisiyaVibhakti = affix.sourceTaddhitaAffix in setOf(TaddhitaAffix.TASIL, TaddhitaAffix.TRAL)
        return stem.kind == TermKind.PRATIPADIKA && stem.upadesha == "किम्" &&
            stem.varnas == listOf(Vyanjana.KA, Svara.I, Vyanjana.MA) && isPragdisiyaVibhakti &&
            affix.varnas.firstOrNull() in setOf(Vyanjana.TA, Vyanjana.HA)
    }

    override fun apply(context: DerivationState): DerivationChange {
        val stem = context.terms[context.terms.lastIndex - 1]
        return DerivationChange(
            context.replaceTermVarnaRange(stem.id, 0, stem.varnas.size,
                listOf(Vyanjana.KA, Svara.U), mapOf(0 to 0, 1 to 1), sutra)
                .copy(stage = DerivationStage.ANGAKARYA),
            "7.2.104 substitutes canonical कु for किम् before the t-initial prāgdiśīya vibhakti.",
        )
    }
}
