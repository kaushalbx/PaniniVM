package dev.panini.ashtadhyayi.adhyaya8.pada4

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.VarnaSubstitution
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
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
    scope = SutraScope.VARNA,
), DerivationSutra {
    private data class Match(val termIndex: Int, val replacement: String)

    override fun matches(context: DerivationState): Boolean = findMatch(context) != null

    override fun apply(context: DerivationState): DerivationChange {
        val match = findMatch(context)!!
        val targetTerm = context.terms[match.termIndex]
        val replacement = match.replacement

        val surface = targetTerm.surface
        val newSurface = if (surface.startsWith("ह")) {
            replacement + surface.substring(1)
        } else {
            surface
        }

        return DerivationChange(
            state = context.replaceTerm(targetTerm.id, targetTerm.copy(surface = newSurface)),
            explanation = "8.4.62: Replaced 'h' with $replacement after jhay stop."
        ).let { it.copy(state = it.state.addSubstitution(VarnaSubstitution(targetTerm.id, 'ह', replacement, sutra))) }
    }

    private fun findMatch(context: DerivationState): Match? {
        val terms = context.terms
        for (i in 0 until terms.size - 1) {
            val curr = terms[i].surface
            val next = terms[i + 1].surface

            if (next.startsWith("ह")) {
                val replacement = when {
                    curr.endsWith("क") || curr.endsWith("ख") || curr.endsWith("ग") || curr.endsWith("घ") ||
                    curr.endsWith("क्") || curr.endsWith("ख्") || curr.endsWith("ग्") || curr.endsWith("घ्") -> "घ"

                    curr.endsWith("च") || curr.endsWith("छ") || curr.endsWith("ज") || curr.endsWith("झ") ||
                    curr.endsWith("च्") || curr.endsWith("छ्") || curr.endsWith("ज्") || curr.endsWith("झ्") -> "झ"

                    curr.endsWith("ट") || curr.endsWith("ठ") || curr.endsWith("ड") || curr.endsWith("ढ") ||
                    curr.endsWith("ट्") || curr.endsWith("ठ्") || curr.endsWith("ड्") || curr.endsWith("ढ्") -> "ढ"

                    curr.endsWith("त") || curr.endsWith("थ") || curr.endsWith("द") || curr.endsWith("ध") ||
                    curr.endsWith("त्") || curr.endsWith("थ्") || curr.endsWith("द्") || curr.endsWith("ध्") -> "ध"

                    curr.endsWith("प") || curr.endsWith("फ") || curr.endsWith("ब") || curr.endsWith("भ") ||
                    curr.endsWith("प्") || curr.endsWith("फ्") || curr.endsWith("ब्") || curr.endsWith("भ्") -> "भ"

                    else -> null
                }
                if (replacement != null) {
                    return Match(i + 1, replacement)
                }
            }
        }
        return null
    }
}
