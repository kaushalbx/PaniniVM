package dev.panini.ashtadhyayi.adhyaya8.pada4

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.VarnaSubstitution
import dev.panini.pratyahara.Pratyahara
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
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
    scope = SutraScope.VARNA,
), DerivationSutra {
    private data class Match(val termIndex: Int)

    override fun matches(context: DerivationState): Boolean = findMatch(context) != null

    override fun apply(context: DerivationState): DerivationChange {
        val match = findMatch(context)!!
        val targetTerm = context.terms[match.termIndex]
        val surface = targetTerm.surface

        val newSurface = if (surface.startsWith("श्")) {
            "छ्" + surface.drop(2)
        } else if (surface.startsWith("श")) {
            "छ" + surface.drop(1)
        } else {
            surface
        }

        return DerivationChange(
            state = context.replaceTerm(targetTerm.id, targetTerm.copy(surface = newSurface)),
            explanation = "8.4.63: Substituted 'ś' with 'ch' after jhay stop."
        ).let { it.copy(state = it.state.addSubstitution(VarnaSubstitution(targetTerm.id, 'श', "छ", sutra))) }
    }

    private fun findMatch(context: DerivationState): Match? {
        val terms = context.terms
        for (i in 0 until terms.size - 1) {
            val curr = terms[i].surface
            val next = terms[i + 1].surface

            val endsWithJhay = curr.endsWith("त्") || curr.endsWith("द्") || curr.endsWith("क्") || curr.endsWith("ग्") ||
                    curr.endsWith("च्") || curr.endsWith("ज्") || curr.endsWith("ट्") || curr.endsWith("ड्") ||
                    curr.endsWith("प्") || curr.endsWith("ब्")
            val nextStartsWithSha = next.startsWith("श") || next.startsWith("श्")

            if (endsWithJhay && nextStartsWithSha && next.length > 1) {
                val follower = next[1]
                if (Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.AT, follower) || follower in setOf('ि', 'ी', 'ु', 'ू', 'ृ', 'े', 'ै', 'ो', 'ौ')) {
                    return Match(i + 1)
                }
            }
        }
        return null
    }
}
