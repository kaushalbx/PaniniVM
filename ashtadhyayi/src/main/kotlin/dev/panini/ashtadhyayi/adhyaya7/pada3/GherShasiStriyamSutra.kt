package dev.panini.ashtadhyayi.adhyaya7.pada3

import dev.panini.core.Linga
import dev.panini.core.Vacana
import dev.panini.core.Vibhakti
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Ayogavaha
import dev.panini.shiksha.Svara
import dev.panini.shiksha.toDevanagari
import dev.panini.shiksha.toDirgha
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** Ik-final feminine stem before śas: realize the long īḥ/ūḥ accusative plural. */
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
        val isFeminineIkStem = context.samjnas.any {
            it.targetId == stem.id && it.samjna in setOf(Samjna.GHI, Samjna.NADI)
        }
        return isFeminineIkStem &&
            affix.upadesha == "शस्" &&
            stem.varnas.lastOrNull() in setOf(Svara.I, Svara.II, Svara.U, Svara.UU)
    }

    override fun apply(context: DerivationState): DerivationChange {
        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()
        val source = stem.varnas.last() as Svara
        val replacement = listOf(source.toDirgha(), Ayogavaha.VISARGA)
        val surface = (stem.varnas.dropLast(1) + replacement).toDevanagari()
        return DerivationChange(
            state = context.mergeTermsByVarnaSubstitution(
                stem.id, affix.id, surface, source, replacement, sutra,
            ).copy(stage = DerivationStage.FINAL),
            explanation = "7.3.126: Formed the feminine Ghi accusative-plural ending before शस्.",
        )
    }
}
