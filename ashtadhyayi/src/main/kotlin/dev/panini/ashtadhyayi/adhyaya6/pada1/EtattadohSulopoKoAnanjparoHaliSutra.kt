package dev.panini.ashtadhyayi.adhyaya6.pada1

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.pratyahara.Pratyahara
import dev.panini.shiksha.Ayogavaha
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Varna
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.toDevanagari
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraStage
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
    scope = SutraScope.PADA_BOUNDARY,
    stage = SutraStage.SANDHI,
), DerivationSutra {

    override fun matches(context: DerivationState): Boolean {
        if (context.terms.size < 2) return false
        return (0 until context.terms.size - 1).any { i ->
            val curr = context.terms[i]
            val next = context.terms[i + 1]

            val isSaOrEsha = isSaOrEsha(curr.varnas)
            val first = next.varnas.firstOrNull() as? Vyanjana
            val nextStartsWithHal = first != null && Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.HAL, first)

            isSaOrEsha && nextStartsWithHal
        }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val targetIndex = (0 until context.terms.size - 1).first { i ->
            val curr = context.terms[i]
            val next = context.terms[i + 1]

            val isSaOrEsha = isSaOrEsha(curr.varnas)
            val first = next.varnas.firstOrNull() as? Vyanjana
            val nextStartsWithHal = first != null && Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.HAL, first)

            isSaOrEsha && nextStartsWithHal
        }

        val targetTerm = context.terms[targetIndex]
        val source = targetTerm.varnas.last()
        val newSurface = targetTerm.varnas.dropLast(1).toDevanagari()

        return DerivationChange(
            state = context.substituteTermSurface(targetTerm.id, newSurface, source, emptyList(), sutra),
            explanation = "6.1.132: Elided visarga (su-lopa) from ${targetTerm.surface} before hal."
        )
    }

    private fun isSaOrEsha(varnas: List<Varna>): Boolean = varnas in setOf(
        listOf(Vyanjana.SA, Svara.A, Ayogavaha.VISARGA),
        listOf(Svara.E, Vyanjana.SSA, Svara.A, Ayogavaha.VISARGA),
        listOf(Vyanjana.SA, Svara.A, Vyanjana.SA),
        listOf(Svara.E, Vyanjana.SSA, Svara.A, Vyanjana.SA),
    )
}
