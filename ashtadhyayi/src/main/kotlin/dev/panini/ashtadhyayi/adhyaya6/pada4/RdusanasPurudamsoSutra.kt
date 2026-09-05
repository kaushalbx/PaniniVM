package dev.panini.ashtadhyayi.adhyaya6.pada4

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.sutra.NimittaScope
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 6.4.11: ṛduśanas-purudaṃso 'nehasāṃ ca.
 * Lengthens penultimate vowel of ṛ-stems (becoming 'ār') before nominative singular 'su', yielding 'pita', 'mata', etc. after r-lopa.
 */
object RdusanasPurudamsoSutra : Sutra<DerivationState, DerivationChange>(
    number = "6.4.11",
    text = "ऋदुशनस्पुरुदंसोऽनेहसां च",
    hindiExplanation = "ऋदन्त अङ्ग की उपधा का दीर्घ होता है असम्बुद्धौ सौ विभक्तौ परे (पितार् -> पिता)।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 4,
    optional = false,
    kramaValue = 640011,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
    nimittaScope = NimittaScope.BOTH,
    dependencies = setOf("6.4.1")
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if ("6.4.1" !in context.activeAdhikaras) return false
        if (context.terms.size < 2) return false

        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()

        if (stem.surface.endsWith('ा')) return false
        val isArStem = stem.upadesha.endsWith("ृ") || stem.surface.endsWith("अर्")
        if (!isArStem) return false

        val isSu = affix.id == "sup-su" || affix.upadesha == "सुँ"
        return isSu
    }

    override fun apply(context: DerivationState): DerivationChange {
        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()

        val newSurface = if (stem.surface.endsWith("र्")) stem.surface.dropLast(2) + "ा" else stem.surface.dropLast(1) + "ा"
        val newTerms = context.terms.dropLast(1)

        return DerivationChange(
            state = context.replaceTerm(stem.id, stem.copy(surface = newSurface))
                .copy(terms = newTerms.dropLast(1) + stem.copy(surface = newSurface), stage = DerivationStage.PADA_FORMED)
                .copy(droppedTerms = context.droppedTerms + dev.panini.derivation.consumeAffixForDrop(affix, sutra)),
            explanation = "6.4.11 & 8.2.7: Derived '$newSurface' for ṛ-stem before nominative singular su."
        )
    }
}
