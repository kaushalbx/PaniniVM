package dev.panini.ashtadhyayi.adhyaya2.pada4

import dev.panini.core.ItMarker
import dev.panini.core.Lakara
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 2.4.85: luṭaḥ prathamasyadāraurasaḥ.
 * In luṭ, the prathama-puruṣa endings are replaced by ḍā, rau, ras in both padas.
 */
object LutahPrathamasyaDarauRasahSutra : Sutra<DerivationState, DerivationChange>(
    number = "2.4.85",
    text = "लुटः प्रथमस्य डारौरसः",
    hindiExplanation = "लुट् लकार के प्रथम पुरुष के प्रत्ययों (तिप्, तस्, झि) के स्थान पर क्रमशः डा, रौ, रस् आदेश होते हैं।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 4,
    optional = false,
    kramaValue = 240085,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.PRATYAYA,
), DerivationSutra {
    private val replacements = mapOf(
        "तिप्" to "डा", "तस्" to "रौ", "झि" to "रस्",
        "त" to "डा", "आताम्" to "रौ", "झ" to "रस्",
    )

    override fun matches(context: DerivationState): Boolean {
        val ending = context.terms.lastOrNull() ?: return false
        val replacement = replacements[ending.upadesha] ?: return false
        return context.effectiveContext.rupa.lakara == Lakara.LUT &&
            ending.surface != replacement
    }

    override fun apply(context: DerivationState): DerivationChange {
        val ending = context.terms.last()
        val replacement = requireNotNull(replacements[ending.upadesha])

        // डा has ḍ as it-marker (ṭa-varga initial), which is marked as ItMarker.T
        val itMarkers = if (replacement == "डा") setOf(ItMarker.T) else emptySet()
        val newEnding = ending.copy(surface = replacement, upadesha = replacement, itMarkers = itMarkers)

        return DerivationChange(
            state = context.replaceTerm(ending.id, newEnding),
            explanation = "2.4.85 replaces the ending ${ending.upadesha} with $replacement in लुट्."
        )
    }
}
