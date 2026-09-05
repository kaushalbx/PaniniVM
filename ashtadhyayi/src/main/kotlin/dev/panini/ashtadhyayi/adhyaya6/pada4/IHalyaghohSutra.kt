package dev.panini.ashtadhyayi.adhyaya6.pada4

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.core.Lakara
import dev.panini.core.PadaType
import dev.panini.shiksha.Varnamala
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraStage
import dev.panini.sutra.SutraType

/** 6.4.113: ई हल्यघोः. */
object IHalyaghohSutra : Sutra<DerivationState, DerivationChange>(
    number = "6.4.113", text = "ई हल्यघोः",
    hindiExplanation = "हलादि कित् या ङित् सार्वधातुक के परे श्ना के आकार को ईकार होता है।",
    type = SutraType.APAVADA, chapter = 6, pada = 4, optional = false, kramaValue = 640113,
    role = SutraRole.Apavada, action = SutraAction.ADESHA, scope = SutraScope.PRATYAYA,
    dependencies = setOf("6.4.1", "3.4.113"), stage = SutraStage.ANGAKARYA,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        val shna = shna(context) ?: return false
        val lingAtmanepada = context.effectiveContext.rupa.lakara == Lakara.LING &&
            context.effectiveContext.rupa.pada == PadaType.ATMANEPADA
        return !lingAtmanepada && isKngitSarvadhatuka(context, shna) &&
            nextInitial(context, shna)?.let(Varnamala::isConsonant) == true
    }

    override fun apply(context: DerivationState): DerivationChange {
        val shna = requireNotNull(shna(context))
        return DerivationChange(
            context.substituteTermSurface(shna.id, shna.surface.dropLast(1) + "ी", 'आ', "ई", sutra),
            "6.4.113 substitutes ī for the ā of श्ना before a consonant-initial k/ṅ-it sārvadhātuka.",
        )
    }
}
