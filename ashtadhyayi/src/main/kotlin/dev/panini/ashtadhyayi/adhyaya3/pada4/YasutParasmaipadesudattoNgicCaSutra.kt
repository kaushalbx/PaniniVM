package dev.panini.ashtadhyayi.adhyaya3.pada4

import dev.panini.core.PadaType
import dev.panini.core.TingAffix
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.TermKind
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraPriority
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** 3.4.103: yāsuṭ parasamaipadeṣūdātto ṅic ca. */
object YasutParasmaipadesudattoNgicCaSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.4.103",
    text = "यासुट् परस्मैपदेषूदात्तो ङिच्च",
    hindiExplanation = "लिङ् के परस्मैपद तिङ् प्रत्यय से पहले यासुट् आगम होता है; वह उदात्त और ङित् है।",
    type = SutraType.APAVADA,
    chapter = 3,
    pada = 4,
    optional = false,
    kramaValue = 340103,
    role = SutraRole.Apavada,
    action = SutraAction.AGAMA,
    scope = SutraScope.PRATYAYA,
    priority = SutraPriority.APAVADA,
    blocks = setOf("3.4.102"),
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        val ting = context.terms.lastOrNull() ?: return false
        return context.stage == DerivationStage.PRATYAYA_SELECTED &&
            ting.kind == TermKind.PRATYAYA &&
            ting.matchesUpadesha("लिङ्") &&
            TingAffix.entries.any { it.upadesha == ting.upadesha && it.pada == PadaType.PARASMAIPADA } &&
            context.terms.none { it.id == "yasut" }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val ting = context.terms.last()
        val yasut = DerivationTerm("yasut", "यासुँट्", TermKind.AGAMA, upadesha = "यासुट्", itProcessingPending = true)
        return DerivationChange(
            context.copy(terms = context.terms.dropLast(1) + yasut + ting),
            "3.4.103 inserts यासुट् before the liṅ parasmaipada termination.",
        )
    }
}
