package dev.panini.ashtadhyayi.adhyaya6.pada1

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.ItProcessingPhase
import dev.panini.derivation.TermKind
import dev.panini.derivation.WholeAffixDesignationPolicy
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** 6.1.67: वेरपृक्तस्य. Deletes the processed अपृक्त वि of क्विप्. */
object VerAprktasyaSutra : Sutra<DerivationState, DerivationChange>(
    number = "6.1.67",
    text = "वेरपृक्तस्य",
    hindiExplanation = "अपृक्त वि का लोप होता है।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 1,
    optional = false,
    kramaValue = 610067,
    role = SutraRole.Vidhi,
    action = SutraAction.LOPA,
    scope = SutraScope.PRATYAYA,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean = context.terms.any {
        it.kind == TermKind.PRATYAYA && it.upadesha == "क्विप्" &&
            it.surface == "वि" && it.itProcessingPhase == ItProcessingPhase.PROCESSED
    }

    override fun apply(context: DerivationState): DerivationChange {
        val kvip = context.terms.first { it.upadesha == "क्विप्" && it.surface == "वि" }
        return DerivationChange(
            state = context.replaceWholeAffix(
                id = kvip.id,
                surface = "",
                sutra = sutra,
                policy = WholeAffixDesignationPolicy.Consume,
            ),
            explanation = "6.1.67 deletes the processed अपृक्त वि of क्विप्.",
        )
    }
}
