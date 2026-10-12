package dev.panini.ashtadhyayi.adhyaya8.pada4

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.pratyahara.Pratyahara
import dev.panini.shiksha.Vyanjana
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraStage
import dev.panini.sutra.SutraType

/**
 * 8.4.62: jhayo ho'nyatarasyām.
 * After a jhay consonant (1st, 2nd, 3rd, 4th varna stop), 'h' is optionally replaced by
 * the 4th varna (gh, jh, ḍh, dh, bh) corresponding to the preceding stop's class.
 */
object JhayoHonyatarasyamSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.4.62",
    text = "झयो होऽन्यतरस्याम्",
    hindiExplanation = "झय् (क, च, ट, त, प वर्ग के १-४ वर्ण) से उत्तर ह-कार के स्थान पर विकल्प से पूर्वसवर्ण (वर्ग का ४था वर्ण) आदेश होता है।",
    type = SutraType.NITYA,
    chapter = 8,
    pada = 4,
    optional = true,
    kramaValue = 840062,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.PADA_BOUNDARY,
    stage = SutraStage.SANDHI,
), DerivationSutra {

    override fun matches(context: DerivationState): Boolean {
        if (context.terms.size < 2) return false
        return (0 until context.terms.size - 1).any { i ->
            val last = context.terms[i].varnas.lastOrNull() ?: return@any false
            context.terms[i + 1].varnas.firstOrNull() == Vyanjana.HA &&
                Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.JHAY, last)
        }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val prevIndex = (0 until context.terms.size - 1).first { i ->
            val last = context.terms[i].varnas.lastOrNull() ?: return@first false
            context.terms[i + 1].varnas.firstOrNull() == Vyanjana.HA &&
                Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.JHAY, last)
        }

        val prevTerm = context.terms[prevIndex]
        val targetTerm = context.terms[prevIndex + 1]
        val replacement = fourthOfVarga(prevTerm.varnas.last())

        return DerivationChange(
            state = context.replaceTermVarna(targetTerm.id, 0, listOf(replacement), sutra),
            explanation = "8.4.62: Replaced 'h' with ${replacement.devanagari} after jhay stop."
        )
    }

    private fun fourthOfVarga(source: dev.panini.shiksha.Varna): Vyanjana = when (source) {
        Vyanjana.KA, Vyanjana.KHA, Vyanjana.GA, Vyanjana.GHA -> Vyanjana.GHA
        Vyanjana.CA, Vyanjana.CHA, Vyanjana.JA, Vyanjana.JHA -> Vyanjana.JHA
        Vyanjana.TTA, Vyanjana.TTHA, Vyanjana.DDA, Vyanjana.DDHA -> Vyanjana.DDHA
        Vyanjana.TA, Vyanjana.THA, Vyanjana.DA, Vyanjana.DHA -> Vyanjana.DHA
        Vyanjana.PA, Vyanjana.PHA, Vyanjana.BA, Vyanjana.BHA -> Vyanjana.BHA
        else -> Vyanjana.DHA
    }
}
