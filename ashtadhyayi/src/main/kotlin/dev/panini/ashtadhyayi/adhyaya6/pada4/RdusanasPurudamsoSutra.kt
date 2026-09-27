package dev.panini.ashtadhyayi.adhyaya6.pada4

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
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

        if (stem.varnas.lastOrNull() == Svara.AA) return false
        val isArStem = stem.upadeshaVarnas.lastOrNull() == Svara.R ||
            stem.varnas.takeLast(2) == listOf(Svara.A, Vyanjana.RA)
        if (!isArStem) return false

        val isSu = affix.id == "sup-su" || affix.upadesha == "सुँ"
        return isSu
    }

    override fun apply(context: DerivationState): DerivationChange {
        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()

        val source = if (stem.varnas.takeLast(2) == listOf(Svara.A, Vyanjana.RA)) Vyanjana.RA else Svara.R
        val result = if (source == Vyanjana.RA) stem.varnas.dropLast(2) + Svara.AA else stem.varnas.dropLast(1) + Svara.AA
        return DerivationChange(
            state = context.substituteTermVarnas(stem.id, result, source, listOf(Svara.AA), sutra)
                .removeTerm(affix.id, sutra = sutra)
                .copy(stage = DerivationStage.PADA_FORMED),
            explanation = "6.4.11 & 8.2.7 derives the lengthened ṛ-stem before nominative singular su."
        )
    }
}
