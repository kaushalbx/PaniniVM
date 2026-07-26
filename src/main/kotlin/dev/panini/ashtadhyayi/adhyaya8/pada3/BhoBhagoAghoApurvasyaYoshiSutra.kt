package dev.panini.ashtadhyayi.adhyaya8.pada3

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
 * 8.3.17: bho-bhago-agho-apūrvasya yo'śi.
 * The sound 'ru' (s/ḥ) following bhoḥ, bhagoḥ, aghoḥ, or a short/long 'a' (a-pūrva / ā-pūrva)
 * is replaced by 'y' before an aś sound (vowels + voiced consonants).
 */
object BhoBhagoAghoApurvasyaYoshiSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.3.17",
    text = "भोभगोअघोअपूर्वस्य योऽशि",
    hindiExplanation = "भोः, भगोः, अघोः तथा अ/आ पूर्व वाले रुँ (विसर्ग/सकार) के स्थान पर 'अश्' परे होने पर 'य' आदेश होता है।",
    type = SutraType.NITYA,
    chapter = 8,
    pada = 3,
    optional = false,
    kramaValue = 830017,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.VARNA,
), DerivationSutra {
    private val vocativeStems = setOf("भो", "भगो", "अघो")

    override fun matches(context: DerivationState): Boolean {
        if (context.terms.size < 2) return false
        return (0 until context.terms.size - 1).any { i ->
            val curr = context.terms[i].surface
            val next = context.terms[i + 1].surface

            if (curr.isEmpty() || next.isEmpty()) return@any false

            val hasVisargaOrS = curr.endsWith("ः") || curr.endsWith("स्") || curr.endsWith("स")
            val isBhoOrAPurva = vocativeStems.any { curr.startsWith(it) } ||
                    curr.endsWith("ः") || curr.endsWith("स्") || curr.endsWith("ाः") || curr.endsWith("ास्")

            val nextStartsWithAsh = Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.ASH, next.first())

            hasVisargaOrS && isBhoOrAPurva && nextStartsWithAsh
        }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val targetIndex = (0 until context.terms.size - 1).first { i ->
            val curr = context.terms[i].surface
            val next = context.terms[i + 1].surface

            if (curr.isEmpty() || next.isEmpty()) return@first false

            val hasVisargaOrS = curr.endsWith("ः") || curr.endsWith("स्") || curr.endsWith("स")
            val isBhoOrAPurva = vocativeStems.any { curr.startsWith(it) } ||
                    curr.endsWith("ः") || curr.endsWith("स्") || curr.endsWith("ाः") || curr.endsWith("ास्")

            val nextStartsWithAsh = Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.ASH, next.first())

            hasVisargaOrS && isBhoOrAPurva && nextStartsWithAsh
        }

        val targetTerm = context.terms[targetIndex]
        val surface = targetTerm.surface

        val newSurface = when {
            surface.endsWith("स्") -> surface.dropLast(2) + "य्"
            surface.endsWith("ः") || surface.endsWith("स") -> surface.dropLast(1) + "य"
            else -> surface + "य्"
        }

        return DerivationChange(
            state = context.replaceTerm(targetTerm.id, targetTerm.copy(surface = newSurface)),
            explanation = "8.3.17: Replaced ru/visarga with 'y' before aś sound."
        ).let { it.copy(state = it.state.addSubstitution(VarnaSubstitution(targetTerm.id, surface.last(), "य", sutra))) }
    }
}
