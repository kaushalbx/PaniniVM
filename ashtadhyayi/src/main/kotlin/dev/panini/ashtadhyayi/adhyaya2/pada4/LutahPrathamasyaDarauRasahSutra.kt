package dev.panini.ashtadhyayi.adhyaya2.pada4

import dev.panini.core.TingAffix
import dev.panini.core.Lakara
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.hasCurrentAffix
import dev.panini.shiksha.Varna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
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
    private data class Replacement(val upadesha: String, val varnas: List<Varna>, val fresh: Boolean = false)
    private val da = Replacement("डा", listOf(Vyanjana.DDA, Svara.AA), fresh = true)
    private val rau = Replacement("रौ", listOf(Vyanjana.RA, Svara.AU))
    private val ras = Replacement("रस्", listOf(Vyanjana.RA, Svara.A, Vyanjana.SA))
    private val replacements = mapOf(
        TingAffix.TIP to da, TingAffix.TAS to rau, TingAffix.JHI to ras,
        TingAffix.TA to da, TingAffix.ATAM to rau, TingAffix.JHA to ras,
    )

    override fun matches(context: DerivationState): Boolean {
        val ending = context.terms.lastOrNull() ?: return false
        return context.effectiveContext.rupa.lakara == Lakara.LUT &&
            replacements.keys.any(ending::hasCurrentAffix)
    }

    override fun apply(context: DerivationState): DerivationChange {
        val ending = context.terms.last()
        val replacement = replacements.entries.single { ending.hasCurrentAffix(it.key) }.value

        return DerivationChange(
            state = context.replaceWholeAffix(
                ending.id, replacement.varnas, sutra,
                if (replacement.fresh) dev.panini.derivation.WholeAffixDesignationPolicy.FreshUpadesha
                else dev.panini.derivation.WholeAffixDesignationPolicy.Consume,
                upadesha = replacement.upadesha,
            ),
            explanation = "2.4.85 replaces the ending ${ending.upadesha} with ${replacement.upadesha} in लुट्."
        )
    }
}
