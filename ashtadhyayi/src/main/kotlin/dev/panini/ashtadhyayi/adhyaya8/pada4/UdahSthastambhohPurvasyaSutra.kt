package dev.panini.ashtadhyayi.adhyaya8.pada4

import dev.panini.derivation.*
import dev.panini.shiksha.Vyanjana
import dev.panini.sutra.*

/**
 * 8.4.61 उदः स्थास्तम्भोः पूर्वस्य — after उद्, the स of स्था and स्तम्भ is replaced by the savarṇa
 * of what precedes. By 1.1.50 the nearest one to स (aghoṣa, mahāprāṇa) is थ: उद् + स्थानम् →
 * उद्थानम्, then 8.4.55 → उत्थ्थानम् and 8.4.65 → उत्थानम्. It applies only when the following
 * term is that dhātu by its upadeśa, not to any word beginning स्था.
 */
object UdahSthastambhohPurvasyaSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.4.61", text = "उदः स्थास्तम्भोः पूर्वस्य",
    hindiExplanation = "उद् से परे स्था और स्तम्भ के सकार को पूर्वसवर्ण आदेश होता है।",
    type = SutraType.NITYA, chapter = 8, pada = 4, optional = false, kramaValue = 840061,
    role = SutraRole.Vidhi, action = SutraAction.ADESHA, scope = SutraScope.VARNA,
    stage = SutraStage.SANDHI,
), DerivationSutra {
    private val dhatus = setOf("ष्ठा", "ष्टम्भुँ", "स्था", "स्तम्भ्")

    private fun target(state: DerivationState): DerivationTerm? {
        for ((index, left) in state.terms.withIndex()) {
            if (left.upadesha != "उद्" || left.varnas.isEmpty()) continue
            val right = state.terms.drop(index + 1).firstOrNull { it.varnas.isNotEmpty() } ?: continue
            if (right.upadesha in dhatus && right.varnas.first() == Vyanjana.SA) return right
        }
        return null
    }
    override fun matches(context: DerivationState) = target(context) != null
    override fun apply(context: DerivationState): DerivationChange {
        val term = requireNotNull(target(context))
        return DerivationChange(context.replaceTermVarna(term.id, 0, listOf(Vyanjana.THA), sutra),
            "8.4.61 replaces the स of स्था/स्तम्भ after उद् with थ, the savarṇa of the preceding sound.")
    }
}
