package dev.panini.ashtadhyayi.adhyaya7.pada1

import dev.panini.core.SupAffix
import dev.panini.derivation.AdeshaPlacement
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.TermKind
import dev.panini.derivation.introduceRawAdesha
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.sutra.*

/**
 * 7.1.94 ऋदुशनस्पुरुदंसोऽनेहसां च.
 * Source: https://www.ashtadhyayi.com/sutraani/plsk102
 */
object RdusanasPurudamsoAnehasamCaSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.1.94",
    text = "ऋदुशनस्पुरुदंसोऽनेहसां च",
    hindiExplanation = "असम्बुद्धि सुँ परे ऋदन्त अङ्ग तथा उशनस्, पुरुदंसस् और अनेहस् के अन्त्य को अनङ् आदेश होता है।",
    type = SutraType.NITYA,
    chapter = 7,
    pada = 1,
    optional = false,
    kramaValue = 710094,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
    blocks = setOf("7.3.110"),
), DerivationSutra {
    private fun target(context: DerivationState) = context.terms.getOrNull(context.terms.lastIndex - 1)
        ?.takeIf { stem ->
            stem.kind == TermKind.PRATIPADIKA &&
                (stem.upadeshaVarnas.lastOrNull() == Svara.R || stem.upadesha in namedStems) &&
                context.terms.lastOrNull()?.sourceSupAffix == SupAffix.SU &&
                sutra !in stem.establishedBySutras &&
                context.terms.none { it.adeshaTarget?.termId == stem.id }
        }

    override fun matches(context: DerivationState): Boolean = target(context) != null

    override fun apply(context: DerivationState): DerivationChange {
        val stem = requireNotNull(target(context))
        return DerivationChange(
            context.introduceRawAdesha(
                stem.id,
                listOf(Svara.A, Vyanjana.NA, Vyanjana.NGA),
                AdeshaPlacement.NGIT_FINAL,
                sutra,
                "anang:${stem.id}",
                upadesha = "अनङ्",
            ),
            "7.1.94 introduces raw अनङ्; independent इत् processing and 1.1.53 determine its exact final target.",
        )
    }

    private val namedStems = setOf("उशनस्", "पुरुदंसस्", "अनेहस्")
}
