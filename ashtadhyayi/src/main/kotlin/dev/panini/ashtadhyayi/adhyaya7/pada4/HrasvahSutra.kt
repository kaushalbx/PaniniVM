package dev.panini.ashtadhyayi.adhyaya7.pada4

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.toDevanagari
import dev.panini.shiksha.toHrasva
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** 7.4.59: ह्रस्वः. */
object HrasvahSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.4.59", text = "ह्रस्वः",
    hindiExplanation = "अभ्यास में स्थित स्वर का ह्रस्व आदेश होता है।",
    type = SutraType.NITYA, chapter = 7, pada = 4, optional = false, kramaValue = 740059,
    role = SutraRole.Vidhi, action = SutraAction.ADESHA, scope = SutraScope.DHATU,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        val abhyasa = context.terms.firstOrNull { it.id == "abhyasa" } ?: return false
        return context.samjnas.any { it.targetId == abhyasa.id && it.samjna == Samjna.ABHYASA } &&
            abhyasa.varnas.any { it in SHORTENABLE_ABHYASA_VOWELS }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val abhyasa = context.terms.first { it.id == "abhyasa" }
        val source = abhyasa.varnas.first { it in SHORTENABLE_ABHYASA_VOWELS } as Svara
        val replacement = listOf(source.toHrasva())
        val shortened = abhyasa.varnas.map {
            if (it in SHORTENABLE_ABHYASA_VOWELS) (it as Svara).toHrasva() else it
        }.toDevanagari()
        return DerivationChange(
            context.substituteTermSurface(abhyasa.id, shortened, source, replacement, sutra),
            "7.4.59 shortens the vowel of the abhyāsa ${abhyasa.surface}.",
        )
    }

    private val SHORTENABLE_ABHYASA_VOWELS = setOf(Svara.AA, Svara.II, Svara.UU, Svara.RR)
}
