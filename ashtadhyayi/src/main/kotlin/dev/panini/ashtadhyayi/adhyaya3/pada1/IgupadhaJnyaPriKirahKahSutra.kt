package dev.panini.ashtadhyayi.adhyaya3.pada1

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.TermKind
import dev.panini.pratyahara.Pratyahara
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 3.1.135 इगुपधज्ञाप्रीकिरः कः.
 * Prescribes ka (अ) Kṛt affix after igupadhā roots and jñā, prī, kṝ in agent (kartṛ) sense.
 */
object IgupadhaJnyaPriKirahKahSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.1.135", text = "इगुपधज्ञाप्रीकिरः कः",
    hindiExplanation = "इगुपध (इ, उ, ऋ उपधा वाली) धातुओं तथा 'ज्ञा', 'प्री', 'कॄ' धातुओं से कर्त्रादि अर्थ में 'क' (अ) प्रत्यय होता है।",
    type = SutraType.NITYA, chapter = 3, pada = 1, optional = false, kramaValue = 310135,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DERIVATION,
), DerivationSutra {
    private val specialRoots = setOf("ज्ञा", "प्री", "कॄ", "बुध्", "विद्", "मुच्")

    override fun matches(context: DerivationState): Boolean {
        if (context.effectiveContext.rupa.lakara != null) return false
        if (context.allEffectiveTerms.any { it.kind == TermKind.PRATYAYA }) return false
        val rootTerm = context.allEffectiveTerms.firstOrNull { it.kind == TermKind.DHATU } ?: return false
        return rootTerm.upadesha in specialRoots || isIgupadha(rootTerm.surface)
    }

    override fun apply(context: DerivationState): DerivationChange {
        val kaTerm = DerivationTerm("ka", "क", TermKind.PRATYAYA, upadesha = "क", createdBySutra = sutra, itProcessingPhase = dev.panini.derivation.ItProcessingPhase.RAW_UPADESHA)
        return DerivationChange(
            state = context.addTerm(kaTerm),
            explanation = "3.1.135 prescribes क Kṛt agent affix.",
        )
    }

    private fun isIgupadha(text: String): Boolean {
        if (text.length < 2) return false
        val penult = text[text.length - 2]
        return Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.IK, penult)
    }
}
