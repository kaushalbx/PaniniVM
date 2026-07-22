package dev.panini.ashtadhyayi.adhyaya3.pada3

import dev.panini.core.Lakara
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.TermKind
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** 3.3.161: vidhinimantraṇāmantraṇādhīṣṭasaṃpraśnaprārthaneṣu liṅ. */
object VidhinimantranamantranadhistasamprashnaprarthanesuLingSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.3.161",
    text = "विधिनिमन्त्रणामन्त्रणाधीष्टसंप्रश्नप्रार्थनेषु लिङ्",
    hindiExplanation = "विधि, निमन्त्रण, आमन्त्रण, अधीष्ट, संप्रश्न और प्रार्थना के अर्थों में लिङ् होता है।",
    type = SutraType.NITYA,
    chapter = 3,
    pada = 3,
    optional = false,
    kramaValue = 330161,
    role = SutraRole.Vidhi,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        context.effectiveContext.rupa.lakara == Lakara.LING && context.stage == DerivationStage.INITIAL

    override fun apply(context: DerivationState): DerivationChange = DerivationChange(
        context.addTerm(DerivationTerm("ling", "लिङ्", TermKind.PRATYAYA, upadesha = "लिङ्"))
            .copy(stage = DerivationStage.PRATYAYA_SELECTED),
        "3.3.161 selects लिङ्.",
    )
}
