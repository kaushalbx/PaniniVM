package dev.panini.ashtadhyayi.adhyaya7.pada1

import dev.panini.derivation.AdeshaPlacement
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.TermKind
import dev.panini.derivation.introduceRawAdesha
import dev.panini.derivation.hasSarvanamasthanaSupIdentity
import dev.panini.derivation.HasMorphosyntax
import dev.panini.core.Linga
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.toVarnas
import dev.panini.sutra.*

/**
 * 7.1.89 पुंसोऽसुङ् — before a सर्वनामस्थान, the aṅga पुम्स् takes असुँङ्, which (ङित्, 1.1.53) replaces
 * its final स्: पुम्स् + सुँ → पुमस् + सुँ, then 7.1.70 नुम् (the उक् it of असुँङ्) and 6.4.10 → पुमान्.
 */
object PumsoAsunSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.1.89", text = "पुंसोऽसुङ्",
    hindiExplanation = "सर्वनामस्थान परे पुंस् शब्द को असुङ् आदेश होता है।",
    type = SutraType.NITYA, chapter = 7, pada = 1, optional = false, kramaValue = 710089,
    role = SutraRole.Vidhi, action = SutraAction.ADESHA, scope = SutraScope.DERIVATION,
), DerivationSutra {
    private val pums = "पुम्स्".toVarnas()

    private fun target(context: DerivationState) = context.terms.getOrNull(context.terms.lastIndex - 1)
        ?.takeIf { stem ->
            val affix = context.terms.last()
            stem.kind == TermKind.PRATIPADIKA && stem.varnas == pums &&
                (context.samjnas.any { it.targetId == affix.id && it.samjna == Samjna.SARVANAMASTHANA } ||
                    (affix.hasSarvanamasthanaSupIdentity() && !HasMorphosyntax(linga = Linga.NAPUMSAKA).matches(context))) &&
                sutra !in stem.establishedBySutras &&
                context.terms.none { it.adeshaTarget?.termId == stem.id }
        }

    override fun matches(context: DerivationState): Boolean = target(context) != null

    override fun apply(context: DerivationState): DerivationChange {
        val stem = requireNotNull(target(context))
        return DerivationChange(
            context.introduceRawAdesha(stem.id,
                "असुँङ्".toVarnas(),
                AdeshaPlacement.NGIT_FINAL, sutra, "asung:${stem.id}", upadesha = "असुँङ्"),
            "7.1.89 introduces raw असुँङ्; इत् processing and 1.1.53 replace the final स् of पुम्स्.")
    }
}
