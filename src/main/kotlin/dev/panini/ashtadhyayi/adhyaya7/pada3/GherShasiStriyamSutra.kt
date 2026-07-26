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

/** Ghi stem before śas in the feminine: realize the long īḥ accusative plural. */
object GherShasiStriyamSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.3.126",
    text = "घेशसि स्त्रियाम्",
    hindiExplanation = "स्त्रीलिङ्ग द्वितीया बहुवचन शस् के परे घि-अन्त्य इ/उ को दीर्घ कर ः रूप होता है।",
    type = SutraType.NITYA,
    chapter = 7,
    pada = 3,
    optional = false,
    kramaValue = 730126,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
    dependencies = setOf("1.4.7"),
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.terms.size < 2 ||
            context.effectiveContext.rupa.linga != Linga.STRI ||
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
            else -> error("GherShasiStriyamSutra matched a non-ik stem")
        }
        return DerivationChange(
            state = context.copy(
                terms = context.terms.dropLast(2) + stem.copy(surface = stem.surface.dropLast(1) + longVowel + "ः"),
                droppedTerms = context.droppedTerms + affix.copy(surface = ""),
                stage = DerivationStage.FINAL,
            ),
            explanation = "7.3.126: Formed the feminine Ghi accusative-plural ending before शस्.",
        )
    }
}
