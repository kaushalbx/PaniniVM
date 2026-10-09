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
 * 8.4.63: śaś cho'ṭi.
 * After a jhay consonant, 'ś' is optionally replaced by 'ch' when followed by an 'aṭ' sound (vowels, y, v, r, h).
 */
object ShashChoAtiSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.4.63",
    text = "शश्छोऽटि",
    hindiExplanation = "झय् से उत्तर श-कार के स्थान पर अट् परे रहते विकल्प से छ-कार आदेश होता है।",
    type = SutraType.NITYA,
    chapter = 8,
    pada = 4,
    optional = true,
    kramaValue = 840063,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.PADA_BOUNDARY,
    stage = SutraStage.SANDHI,
), DerivationSutra {

    override fun matches(context: DerivationState): Boolean {
        if (context.terms.size < 2) return false
        return (0 until context.terms.size - 1).any { i ->
            val curr = context.terms[i].varnas
            val next = context.terms[i + 1].varnas
            val follower = next.getOrNull(1) ?: return@any false
            Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.JHAY, curr.lastOrNull() ?: return@any false) &&
                next.firstOrNull() == Vyanjana.SHA &&
                Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.AT, follower)
        }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val targetIndex = (0 until context.terms.size - 1).first { i ->
            val curr = context.terms[i].varnas
            val next = context.terms[i + 1].varnas
            val follower = next.getOrNull(1) ?: return@first false
            Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.JHAY, curr.lastOrNull() ?: return@first false) &&
                next.firstOrNull() == Vyanjana.SHA &&
                Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.AT, follower)
        } + 1

        val targetTerm = context.terms[targetIndex]

        return DerivationChange(
            state = context.replaceTermVarna(targetTerm.id, 0, listOf(Vyanjana.CHA), sutra),
            explanation = "8.4.63: Substituted 'ś' with 'ch' after jhay stop."
        )
    }
}
