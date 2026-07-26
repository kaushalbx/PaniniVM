package dev.panini.ashtadhyayi.adhyaya1.pada1

import dev.panini.derivation.BlockSutra
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.TermKind
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraInput
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraStage
import dev.panini.sutra.SutraType

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
