package dev.sanskrit.ashtadhyayi.adhyaya1.pada1

import dev.sanskrit.derivation.BlockSutra
import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.derivation.TermKind
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraInput
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraStage
import dev.sanskrit.sutra.SutraType

/** 1.1.6 carries the preceding guna-vrddhi restriction to the listed roots. */
object DidhivevitamSutra : Sutra<DerivationState, DerivationChange>(
    number = "1.1.6",
    text = "दीधीवेवीटाम्",
    hindiExplanation = "दीधी और वेवी धातुओं में पूर्व निषेध का विस्तार होता है।",
    type = SutraType.NISHEDHA,
    chapter = 1,
    pada = 1,
    optional = false,
    kramaValue = 110006,
    role = SutraRole.Atidesha,
    action = SutraAction.ATIDESHA,
    scope = SutraScope.DHATU,
    inputs = setOf(SutraInput.DHATU),
    dependencies = setOf("1.1.4", "1.1.5"),
    stage = SutraStage.ANGAKARYA,
    blocks = setOf("1.1.3"),
    traceTemplateValue = "{sutra} extends the गुण-वृद्धि prohibition to the listed roots.",
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean = context.terms.any { term ->
        term.kind == TermKind.DHATU && listedRoots.any { root ->
            root in (term.upadesha ?: term.surface)
        }
    } && context.blockedSutras["1.1.3"] != sutra

    override fun apply(context: DerivationState): DerivationChange =
        BlockSutra(
            "1.1.3",
            sutra,
            "$sutra extends the preceding prohibition to दीधी/वेवी roots."
        ).apply(context)
}

private val listedRoots = setOf("दीधी", "वेवी")
