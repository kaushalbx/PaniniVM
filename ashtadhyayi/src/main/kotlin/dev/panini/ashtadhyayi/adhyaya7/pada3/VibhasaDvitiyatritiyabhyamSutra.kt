package dev.panini.ashtadhyayi.adhyaya7.pada3

import dev.panini.derivation.*
import dev.panini.sutra.*

/** 7.3.115 optionally extends syāṭ/shortening to the two specified ordinal āp stems. */
object VibhasaDvitiyatritiyabhyamSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.3.115", text = "विभाषा द्वितीयातृतीयाभ्याम्",
    hindiExplanation = "द्वितीया और तृतीया के बाद ङित् विभक्ति को विकल्प से स्याट् और आप् को ह्रस्व होता है।",
    type = SutraType.VIBHASHA, chapter = 7, pada = 3, optional = true, kramaValue = 730115,
    role = SutraRole.Apavada, action = SutraAction.AGAMA, scope = SutraScope.DERIVATION,
    stage = SutraStage.ANGAKARYA, priority = SutraPriority.APAVADA,
    dependencies = setOf("6.4.1"),
), DerivationSutra {
    private fun target(state: DerivationState) = state.apNgitJunctions().firstOrNull { (stem, _) ->
        stem.upadesha in setOf("द्वितीय", "तृतीय", "द्वितीया", "तृतीया")
    }
    override fun matches(context: DerivationState) = target(context) != null
    override fun apply(context: DerivationState): DerivationChange {
        val (stem, suffix) = requireNotNull(target(context))
        return DerivationChange(context.introduceApSyat(stem.id, suffix.id, number),
            "7.3.115 optionally introduces raw syāṭ and shortens āp after dvitīyā or tṛtīyā.")
    }
}
