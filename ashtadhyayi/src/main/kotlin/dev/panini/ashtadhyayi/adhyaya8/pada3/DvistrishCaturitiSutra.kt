package dev.panini.ashtadhyayi.adhyaya8.pada3

import dev.panini.derivation.*
import dev.panini.shiksha.Ayogavaha
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.sutra.*

/**
 * 8.3.43 द्विस्त्रिश्चतुरिति कृत्वोऽर्थे — the visarga of द्विस्, त्रिस्, चतुर् in the sense of कृत्वसुच्
 * (from सुच्, 5.4.18) optionally becomes ष् before ku/pu: द्विष्करोति beside द्विः करोति. द्विस् and
 * त्रिस् prove सुच् by their own स् (द्वि, त्रि have none). चतुर् is not taken: a plain चतुर् ends in र्
 * too, so its visarga does not show the कृत्वोऽर्थ.
 */
object DvistrishCaturitiSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.3.43", text = "द्विस्त्रिश्चतुरिति कृत्वोऽर्थे",
    hindiExplanation = "कृत्वोऽर्थ में द्विस्, त्रिस्, चतुर् के विसर्ग को कवर्ग-पवर्ग परे विकल्प से षकार होता है।",
    type = SutraType.VIBHASHA, chapter = 8, pada = 3, optional = true, kramaValue = 830043,
    role = SutraRole.Vidhi, action = SutraAction.ADESHA, scope = SutraScope.VARNA,
    stage = SutraStage.SIBILANT_SANDHI, priority = SutraPriority.APAVADA, blocks = setOf("8.3.34", "8.3.37"),
), DerivationSutra {
    private val words = setOf(
        listOf(Vyanjana.DA, Vyanjana.VA, Svara.I, Ayogavaha.VISARGA),
        listOf(Vyanjana.TA, Vyanjana.RA, Svara.I, Ayogavaha.VISARGA),
    )
    private fun target(state: DerivationState) = state.ownSVisargaBeforeKupu()?.takeIf { it.varnas in words }
    override fun matches(context: DerivationState) = target(context) != null
    override fun apply(context: DerivationState) = applyAll(context).first()
    override fun applyAll(state: DerivationState): List<DerivationChange> {
        val left = requireNotNull(target(state))
        return listOf(DerivationChange(state.replaceTermVarna(left.id, left.varnas.lastIndex, listOf(Vyanjana.SSA), sutra),
            "8.3.43 optionally replaces the visarga of ${left.surface} with ष् before ku/pu."),
            DerivationChange(state, "8.3.43 declined.", applied = false))
    }
}
