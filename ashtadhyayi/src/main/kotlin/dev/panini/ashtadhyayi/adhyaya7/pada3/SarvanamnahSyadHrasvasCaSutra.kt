package dev.panini.ashtadhyayi.adhyaya7.pada3

import dev.panini.derivation.*
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.sutra.*

/** 7.3.114: syāṭ and shortening after pronominal āp; apavāda to yāṭ. */
object SarvanamnahSyadHrasvasCaSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.3.114", text = "सर्वनाम्नः स्याड्ढ्रस्वश्च",
    hindiExplanation = "आप्-अन्त सर्वनाम के बाद ङित् विभक्ति को स्याट् और आप् को ह्रस्व होता है।",
    type = SutraType.APAVADA, chapter = 7, pada = 3, optional = false, kramaValue = 730114,
    role = SutraRole.Apavada, action = SutraAction.AGAMA, scope = SutraScope.DERIVATION,
    stage = SutraStage.ANGAKARYA, priority = SutraPriority.APAVADA,
    dependencies = setOf("6.4.1"),
), DerivationSutra {
    private fun target(state: DerivationState) = state.apNgitJunctions().firstOrNull { (stem, _) ->
        state.samjnas.any { it.targetId == stem.id && it.samjna == Samjna.SARVANAMA }
    }

    override fun matches(context: DerivationState) = target(context) != null

    override fun apply(context: DerivationState): DerivationChange {
        val (stem, suffix) = requireNotNull(target(context))
        return DerivationChange(context.introduceApSyat(stem.id, suffix.id, number),
            "7.3.114 shortens the established pronominal āp and introduces raw syāṭ for the ṅit ending.")
    }
}
