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
 * 8.3.22: hali sarveṣām.
 * The 'y' following bhoḥ, bhagoḥ, aghoḥ or ā-pūrva is elided before any consonant (hal).
 */
object HaliSarveshamSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.3.22",
    text = "हलि सर्वेषाम्",
    hindiExplanation = "भोः, भगोः, अघोः तथा आकार पूर्व वाले य्-कार का हल् परे होने पर लोप होता है (उदा. भो देवाः, देवा हसन्ति)।",
    type = SutraType.NITYA,
    chapter = 8,
    pada = 3,
    optional = false,
    kramaValue = 830022,
    role = SutraRole.Vidhi,
    action = SutraAction.LOPA,
    scope = SutraScope.VARNA,
), DerivationSutra {

    private val vocativePrefixes = setOf("भो", "भगो", "अघो")

    override fun matches(context: DerivationState): Boolean {
        if (context.terms.size < 2) return false
        return (0 until context.terms.size - 1).any { i ->
            val curr = context.terms[i].surface
            val next = context.terms[i + 1].surface

            val isBhoOrAPurva = vocativePrefixes.any { curr.startsWith(it) } ||
                    curr.endsWith("ाः") || curr.endsWith("ास्") || curr.endsWith("ाय्") || curr.endsWith("ा")

            val nextStartsWithHal = next.isNotEmpty() && Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.HAL, next.first())

            isBhoOrAPurva && nextStartsWithHal
        }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val targetIndex = (0 until context.terms.size - 1).first { i ->
            val curr = context.terms[i].surface
            val next = context.terms[i + 1].surface

            val isBhoOrAPurva = vocativePrefixes.any { curr.startsWith(it) } ||
                    curr.endsWith("ाः") || curr.endsWith("ास्") || curr.endsWith("ाय्") || curr.endsWith("ा")

            val nextStartsWithHal = next.isNotEmpty() && Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.HAL, next.first())

            isBhoOrAPurva && nextStartsWithHal
        }

        val targetTerm = context.terms[targetIndex]
        val surface = targetTerm.surface

        val newSurface = when {
            surface.endsWith("य्") -> surface.dropLast(2)
            surface.endsWith("य") || surface.endsWith("ः") || surface.endsWith("स्") -> surface.dropLast(1)
            else -> surface
        }

        return DerivationChange(
            state = context.replaceTerm(targetTerm.id, targetTerm.copy(surface = newSurface)),
            explanation = "8.3.22: Elided 'y' (hali sarveṣām) before hal consonant."
        ).let { it.copy(state = it.state.addSubstitution(VarnaSubstitution(targetTerm.id, 'य', "", sutra))) }
    }
}
