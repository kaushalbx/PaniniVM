package dev.panini.ashtadhyayi.adhyaya8.pada3

import dev.panini.derivation.*
import dev.panini.shiksha.Ayogavaha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.sutra.*

/**
 * 8.3.40 नमस्पुरसोर्गत्योः — the visarga of नमस् and पुरस् that are गति (1.4.60 ff.) becomes स्
 * before ku/pu: नमस्करोति, पुरस्करोति. Without the गति saṃjñā (नमः करोमि शिवाय, a case form)
 * nothing changes. Known by the word's own स् and the saṃjñā, never by spelling alone.
 */
object NamasPurasorGatyohSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.3.40", text = "नमस्पुरसोर्गत्योः",
    hindiExplanation = "गतिसंज्ञक नमस् और पुरस् के विसर्ग को कवर्ग-पवर्ग परे सकार होता है।",
    type = SutraType.NITYA, chapter = 8, pada = 3, optional = false, kramaValue = 830040,
    role = SutraRole.Vidhi, action = SutraAction.ADESHA, scope = SutraScope.VARNA,
    stage = SutraStage.SIBILANT_SANDHI, priority = SutraPriority.APAVADA, blocks = setOf("8.3.34", "8.3.37"),
), DerivationSutra {
    private val words = setOf(
        listOf(Vyanjana.NA, Svara.A, Vyanjana.MA, Svara.A, Ayogavaha.VISARGA),
        listOf(Vyanjana.PA, Svara.U, Vyanjana.RA, Svara.A, Ayogavaha.VISARGA),
    )
    private fun target(state: DerivationState) = state.ownSVisargaBeforeKupu()?.takeIf {
        it.varnas in words && SamjnaAssignment(it.id, Samjna.GATI) in state.samjnas
    }
    override fun matches(context: DerivationState) = target(context) != null
    override fun apply(context: DerivationState): DerivationChange {
        val left = requireNotNull(target(context))
        return DerivationChange(context.replaceTermVarna(left.id, left.varnas.lastIndex, listOf(Vyanjana.SA), sutra),
            "8.3.40 replaces the visarga of गति ${left.surface} with स् before ku/pu.")
    }
}
