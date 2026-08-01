package dev.panini.ashtadhyayi.adhyaya8.pada4

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.VarnaSubstitution
import dev.panini.pratyahara.Pratyahara
import dev.panini.shiksha.Varnamala
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
            val curr = context.terms[i].surface
            val next = context.terms[i + 1].surface
            if (curr.isEmpty() || !next.startsWith("ह")) return@any false
            val lastChar = curr.trimEnd('्').lastOrNull() ?: return@any false
            Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.JHAY, lastChar)
        }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val prevIndex = (0 until context.terms.size - 1).first { i ->
            val curr = context.terms[i].surface
            val next = context.terms[i + 1].surface
            if (curr.isEmpty() || !next.startsWith("ह")) return@first false
            val lastChar = curr.trimEnd('्').lastOrNull() ?: return@first false
            Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.JHAY, lastChar)
        }

        val prevTerm = context.terms[prevIndex]
        val targetTerm = context.terms[prevIndex + 1]
        val lastChar = prevTerm.surface.trimEnd('्').last()

        val info = Varnamala.getVargaInfo(lastChar)
        val replacement = if (info != null) {
            Varnamala.getVargaMember(info.first, 3)?.toString() ?: "ध"
        } else {
            "ध"
        }

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
}
