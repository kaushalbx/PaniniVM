package dev.panini.ashtadhyayi.adhyaya7.pada3

import dev.panini.core.Linga
import dev.panini.core.Vacana
import dev.panini.core.Vibhakti
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.shiksha.Samjna
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** Ghi stem before śas: form the lengthened masculine accusative-plural base. */
object GherShasiSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.3.124",
    text = "घेशसि",
    hindiExplanation = "पुंसि द्वितीया बहुवचन शस् परे घि-अन्त्य इ/उ को दीर्घ किया जाता है।",
    type = SutraType.NITYA,
    chapter = 7,
    pada = 3,
    optional = false,
    kramaValue = 730124,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
    dependencies = setOf("1.4.7"),
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.terms.size < 2 ||
            context.effectiveContext.rupa.linga != Linga.PUMS ||
            context.effectiveContext.rupa.vibhakti != Vibhakti.DVITIYA ||
            context.effectiveContext.rupa.vacana != Vacana.BAHUVACANA
        ) return false

        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()
        return context.samjnas.any { it.targetId == stem.id && it.samjna == Samjna.GHI } &&
            affix.upadesha == "शस्" &&
            stem.surface.lastOrNull() in setOf('इ', 'ि', 'उ', 'ु')
    }

    override fun apply(context: DerivationState): DerivationChange {
        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()
        val longVowel = when (stem.surface.last()) {
            'इ' -> "ई"
            'ि' -> "ी"
            'उ' -> "ऊ"
            'ु' -> "ू"
            else -> error("GherShasiSutra matched a non-ik stem")
        }
        val formed = stem.copy(surface = stem.surface.dropLast(1) + longVowel + "स्")
        return DerivationChange(
            state = context.copy(
                terms = context.terms.dropLast(2) + formed,
                droppedTerms = context.droppedTerms + affix.copy(surface = ""),
                stage = DerivationStage.PADA_FORMED,
            ),
            explanation = "7.3.124: Formed the lengthened Ghi base before masculine accusative-plural शस्.",
        )
    }
}
