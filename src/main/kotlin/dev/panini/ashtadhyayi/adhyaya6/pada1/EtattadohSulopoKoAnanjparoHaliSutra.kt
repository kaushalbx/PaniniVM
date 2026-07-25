package dev.panini.ashtadhyayi.adhyaya6.pada1

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
 * 6.1.132: etattadoḥ sulopo ko'nañparo hali.
 * The 'su' (s/ḥ) after saḥ and eṣaḥ is elided before a consonant (hal)
 * provided there is no 'ka' (akakaraka) and not preceded by negative 'nañ'.
 */
object EtattadohSulopoKoAnanjparoHaliSutra : Sutra<DerivationState, DerivationChange>(
    number = "6.1.132",
    text = "एतत्तदोः सुलोपो कोरनञ्परो हलि",
    hindiExplanation = "सः तथा एषः के विसर्ग (सुँ) का हल् (व्यंजन) परे होने पर लोप होता है (उदा. स गच्छति, एष विष्णुः)।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 1,
    optional = false,
    kramaValue = 610132,
    role = SutraRole.Vidhi,
    action = SutraAction.LOPA,
    scope = SutraScope.VARNA,
), DerivationSutra {

    override fun matches(context: DerivationState): Boolean {
        if (context.terms.size < 2) return false
        return (0 until context.terms.size - 1).any { i ->
            val curr = context.terms[i].surface
            val next = context.terms[i + 1].surface

            val isSaOrEsha = curr == "सः" || curr == "एषः" || curr == "सस्" || curr == "एषस्"
            val nextStartsWithHal = next.isNotEmpty() && Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.HAL, next.first())

            isSaOrEsha && nextStartsWithHal
        }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val targetIndex = (0 until context.terms.size - 1).first { i ->
            val curr = context.terms[i].surface
            val next = context.terms[i + 1].surface

            val isSaOrEsha = curr == "सः" || curr == "एषः" || curr == "सस्" || curr == "एषस्"
            val nextStartsWithHal = next.isNotEmpty() && Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.HAL, next.first())

            isSaOrEsha && nextStartsWithHal
        }

        val targetTerm = context.terms[targetIndex]
        val surface = targetTerm.surface

        val newSurface = when {
            surface.endsWith("ः") -> surface.dropLast(1)
            surface.endsWith("स्") -> surface.dropLast(2)
            surface.endsWith("स") -> surface.dropLast(1)
            else -> surface
        }

        return DerivationChange(
            state = context.replaceTerm(targetTerm.id, targetTerm.copy(surface = newSurface)),
            explanation = "6.1.132: Elided visarga (su-lopa) from ${targetTerm.surface} before hal."
        ).let { it.copy(state = it.state.addSubstitution(VarnaSubstitution(targetTerm.id, 'ः', "", sutra))) }
    }
}
